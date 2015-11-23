/* dependencies:
    ruler.js
    two.js
*/

"use strict";

var editor = ( function(){
    var options,
        theEditorDOM = document.createElement('div'),
        defaultOptions = {
            // RULER
            rulerVisible: true,
            rulerHeight: 20,
            // TWO JS
            two: {
                fullscreen: true,
                type: Two.Types.webgl
            },
            // GLOBAL
            zoom: 1.0
        },
        two,
        // TODO
        defaultExtensions = [
            '../js/tcs-editor-utils.js',
            '../js/ruler.js',
            '../plugin/draw2d/two.js'
        ];

    /**
     *
     * @param curOptions
     */
    var constructEditor = function(curOptions) {
        options = editorUtils.extend(defaultOptions, curOptions);

        // Load extensions
        //extFunc();

        //
        theEditorDOM = constructDomElement(options);
        two = new Two(options.two).appendTo(theEditorDOM);

        options.container.addEventListener('mousemove', function(e){
            var posX = e.clientX;
            var posY = e.clientY;
            var result = clientToEditorCoord(posX, posY);
            drawTest(posX, posY)
            console.log(result);
        });

    }

    /**
     *
     * @param type
     *    HALT, PARK, REPORT
     */
    var drawPoint = function(type) {
    }

    var drawPath = function() {

    }

    var drawTest = function(x, y) {
        two.clear();

        // two has convenience methods to create shapes.
        var circle_pos = [x,y];//editorToClientCoord(x, y);
        var circle = two.makeCircle(circle_pos[0], circle_pos[1], 50);
        //var rect = two.makeRectangle(213, 100, 100, 100);

        // The object returned has many stylable properties:
        circle.fill = '#FF8000';
        circle.stroke = 'orangered'; // Accepts all valid css color
        circle.linewidth = 5;

        //rect.fill = 'rgb(0, 200, 255)';
        //rect.opacity = 0.75;
        //rect.noStroke();

        // Don't forget to tell two to render everything
        // to the screen
        two.update();
    }

    // PRIVATE FUNCTIONS
    var extFunc = function() {
        $.each(defaultExtensions, function() {
            var extname = this;
            //if (!extname.match(/^ext-.*\.js/)) { // Ensure URL cannot specify some other unintended file in the extPath
            //    return;
            //}
            var element = document.createElement("script");
            element.type = "text/javascript";
            element.src = extname;
            document.head.appendChild(element);
            //$.getScript(extname, function(d) {
            //    // Fails locally in Chrome 5
            //    if (!d) {
            //        var s = document.createElement('script');
            //        s.src = curConfig.extPath + extname;
            //        document.querySelector('head').appendChild(s);
            //    }
            //});
        });
    };

    var constructDomElement = function(curOptions) {
        return ruler.constructRulers(theEditorDOM, curOptions);
    }

    var clientToEditorCoord = function(x, y) {
        if (options.rulerVisible) {
            return [x - editorUtils.offsetLeft(options.container) - options.rulerHeight,
                y - editorUtils.offsetTop(options.container)/* - options.rulerHeight*/];
        } else {
            return [x - editorUtils.offsetLeft(options.container),
                y - editorUtils.offsetTop(options.container)];
        }
    }

    var editorToClientCoord = function(x, y) {
        if (options.rulerVisible) {
            return [x + editorUtils.offsetLeft(options.container) + options.rulerHeight,
                y + editorUtils.offsetTop(options.container)/* + options.rulerHeight*/];
        } else {
            return [x + editorUtils.offsetLeft(options.container),
                y + editorUtils.offsetTop(options.container)];
        }
    }

    return{
        constructEditor: constructEditor,
        drawPoint: drawPoint,
        drawPath: drawPath
    }
})();