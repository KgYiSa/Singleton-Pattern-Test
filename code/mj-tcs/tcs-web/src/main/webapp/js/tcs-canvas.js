
(function () {

    if (!window.console) {
        window.console = {};
        window.console.log = function(str) {};
        window.console.dir = function(str) {};
    }

    if (window.opera) {
        window.console.log = function(str) { opera.postError(str); };
        window.console.dir = function(str) {};
    }

}());

// Class: TCSCanvas
// The main TCSCanvas class that manages all TCS-canvas related functions
//
// Parameters:
// container - The container HTML element that should hold the TCS root element
// config - An object that contains configuration data
$.TCSCanvas = function(container, config) {

    // Default configuration options
    var curConfig = {
        show_outside_canvas: true,
        selectNew: true,
        dimensions: [640, 480], // Array with width/height of canvas
        offset:[50, 400]
    };

    // Update config with new one if given
    if (config) {
        $.extend(curConfig, config);
    }

    var dimensions = curConfig.dimensions;
    var contentOffset = curConfig.offset;

    var tcsCanvas = this;

    // Float displaying the current zoom level (1 = 100%, .5 = 50%, etc)
    var current_zoom = 1;

    // Object containing data for the currently selected styles
    var all_properties = {
        shape: {
            fill: (curConfig.initFill.color == 'none' ? '' : '#') + curConfig.initFill.color,
            fill_paint: null,
            fill_opacity: curConfig.initFill.opacity,
            stroke: '#' + curConfig.initStroke.color,
            stroke_paint: null,
            stroke_opacity: curConfig.initStroke.opacity,
            stroke_width: curConfig.initStroke.width,
            stroke_dasharray: 'none',
            stroke_linejoin: 'miter',
            stroke_linecap: 'butt',
            opacity: curConfig.initOpacity
        }
    };

    all_properties.text = $.extend(true, {}, all_properties.shape);
    $.extend(all_properties.text, {
        fill: '#000000',
        stroke_width: 0,
        font_size: 24,
        font_family: 'serif'
    });


    // Current shape style properties
    var cur_shape = all_properties.shape;

    var ruler_offset = [- ($('#workarea').offset().left - $('#ruler_y').offset().left),
                        - ($('#workarea').offset().top - $('#ruler_x').offset().top)];


    // Array with all the currently selected elements
    // default size of 1 until it needs to grow bigger
    var selectedElements = new Array(1);

    // Mouse events
    (function () {
    }());

    // Function: getResolution
    // Returns the current dimensions and zoom level in an object
    var getResolution = this.getResolution = function() {
        var width = dimensions[0]/current_zoom;
        var height = dimensions[1]/current_zoom;

        return {
            'w': width,
            'h': height,
            'zoom': current_zoom
        };
    };

    this.resizeTwoCanvas = function(w, h) {
        twoCanvas.width = w;
        twoCanvas.height = h;
    };

    // Function: setZoom
    // Sets the zoom to the given level
    //
    // Parameters:
    // zoomlevel - Float indicating the zoom level to change to
    this.setZoom = function(zoomlevel) {
        var res = getResolution();
        //twoContent.width = res.w/zoomlevel;
        //twoContent.height = res.h/zoomlevel;

        current_zoom = zoomlevel;
        $.each(selectedElements, function(i, elem) {
            if (!elem) {return;}
            selectorManager.requestSelector(elem).resize();
        });
        //pathActions.zoomChange();
        //runExtensions('zoomChanged', zoomlevel);
    };

    // Function: getZoom
    // Returns the current zoom level
    this.getZoom = function() {return current_zoom;};

    this.getContentOffset = function() {return contentOffset;};

    // Note: use method to return not function !!!
    this.contentW = getResolution().w;
    this.contentH = getResolution().h;

    // Function: setBBoxZoom
    // Sets the zoom level on the canvas-side based on the given value
    //
    // Parameters:
    // val - Bounding box object to zoom to or string indicating zoom option
    // editor_w - Integer with the editor's workarea box's width
    // editor_h - Integer with the editor's workarea box's height
    this.setBBoxZoom = function(val, editor_w, editor_h) {
        var spacer = 0.85;
        var bb;
        var calcZoom = function(bb) {
            if (!bb) {return false;}
            var w_zoom = Math.round((editor_w / bb.width)*100 * spacer)/100;
            var h_zoom = Math.round((editor_h / bb.height)*100 * spacer)/100;
            var zoomLevel = Math.min(w_zoom, h_zoom);
            tcsCanvas.setZoom(zoomLevel);
            return {'zoom': zoomLevel, 'bbox': bb};
        };

        if (typeof val == 'object') {
            bb = val;
            if (bb.width == 0 || bb.height == 0) {
                var newzoom = bb.zoom ? bb.zoom : current_zoom * bb.factor;
                tcsCanvas.setZoom(newzoom);
                return {'zoom': current_zoom, 'bbox': bb};
            }
            return calcZoom(bb);
        }

        switch (val) {
            case 'content':
                // TODO
                bb = getStrokedBBox();
                break;
            default:
                return;
        }
        return calcZoom(bb);
    };

    // u_multi = unit * zoom
    this.calculateZoomMultiplier = function(u_multi) {
        // Calculate the main number interval
        // Make [1,2,5] array
        var r_intervals = [];
        for(var i = .1; i < 1E5; i *= 10) {
            r_intervals.push(1 * i);
            r_intervals.push(2 * i);
            r_intervals.push(5 * i);
        }

        function _calculateMultiplier(u_multi) {
            // Calculate the main number interval
            var raw_m = 50 / u_multi;
            var multi = 1;
            for(var i = 0; i < r_intervals.length; i++) {
                var num = r_intervals[i];
                multi = num;
                if(raw_m <= num) {
                    break;
                }
            }

            return multi;
        };

        return _calculateMultiplier(u_multi);
    };

    // mouse_coords = [x, y]
    this.mouseCoordinatesToEditorCoordinates = function(mouse_coords) {
        var editor_coords = [0, 0];

        var zoom = current_zoom;

        // todo
        //var units = tcsEdit.units.getTypeMap();
        var unit = 1;//units[curConfig.baseUnit]; // 1 = 1px
        var u_multi = unit * zoom;

        var multi = this.calculateZoomMultiplier(u_multi);

        for(var d = 0; d < 2; d++) {
            var is_x = (d === 0);

            var canvasOffset = contentOffset[d];

            var num = (mouse_coords[d] - ruler_offset[d] - canvasOffset) / u_multi;
            num = (is_x ? num : -num);

            var label;
            if(multi >= 1) {
                label = Math.round(num);
            } else {
                var decs = (multi+'').split('.')[1].length;
                label = num.toFixed(decs)-0;
            }

            editor_coords[d] = label;
        }

        return editor_coords;
    };

    // editor_coords = [x, y]
    this.editorCoordinatesToMouseCoordinates = function(editor_coords) {
        var mouse_coords = [0, 0];

        var zoom = current_zoom;

        // todo
        //var units = tcsEdit.units.getTypeMap();
        var unit = 1;//units[curConfig.baseUnit]; // 1 = 1px
        var u_multi = unit * zoom;

        var multi = this.calculateZoomMultiplier(u_multi);

        for(var d = 0; d < 2; d++) {
            var is_x = (d === 0);

            var canvasOffset = contentOffset[d];

            var num = ((is_x ? 1 : -1) * editor_coords[d] * u_multi + canvasOffset);

            var label;
            if(multi >= 1) {
                label = Math.round(num);
            } else {
                var decs = (multi+'').split('.')[1].length;
                label = num.toFixed(decs)-0;
            }

            mouse_coords[d] = label + ruler_offset[d];
        }

        return mouse_coords;
    };

/////////////////////////////////////////////////  TWO JS  /////////////////////////////////////////////////////////////
    // The actual scene
    var params = { fullscreen: false, // must be false !!!
        width: dimensions[0],
        height: dimensions[1],
        type: Two.Types.webgl,
        //antialias: true
    };
    var twoCanvas = new Two(params).appendTo(document.querySelector('#tcs-canvas'));

    // two axes
    var axisOffset = this.editorCoordinatesToMouseCoordinates([0, 0]);
    var axisLen = 100;
    var axisX = twoCanvas.makeLine(axisOffset[0], axisOffset[1], axisOffset[0] + axisLen, axisOffset[1]);
    axisX.linewidth = 0.5;
    axisX.stroke = 'red';
    var axisY = twoCanvas.makeLine(axisOffset[0], axisOffset[1], axisOffset[0], axisOffset[1] - axisLen);
    axisY.linewidth = 0.5;
    axisY.stroke = 'green';

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    var editor_in = [100, 100];
    var canvas_coord = this.editorCoordinatesToMouseCoordinates(editor_in);

    var text_styles={
        size:10,
        linewidth:1,
        alignment:'center'
    };
    var _x = canvas_coord[0],
        _y = canvas_coord[1];
    var cross_x = twoCanvas.makeLine(_x - 20, _y, _x + 20, _y);
    cross_x.stroke = 'blue';
    cross_x.linewidth = 1.0;
    var cross_y = twoCanvas.makeLine(_x, _y - 20, _x, _y + 20);
    cross_y.stroke = 'blue';
    cross_y.linewidth = 1.0;
    text = twoCanvas.makeText("x: " + editor_in[0] + ", y: " + editor_in[1], _x+45, _y-10, text_styles);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//var earthAngle = 0,
//    moonAngle = 0,
//    distance = 30,
//    radius = 50,
//    padding = 100,
//    orbit = 200,
//    offset = orbit + padding,
//    orbits = twoCanvas.makeGroup();
//
//var earthOrbit = twoCanvas.makeCircle(offset, offset, orbit);
//earthOrbit.noFill();
//earthOrbit.linewidth = 2;
//earthOrbit.stroke = "#EFEFEF";
//orbits.add(earthOrbit);
//twoCanvas.update();
//
//var pos = getPositions(earthAngle++, orbit),
//    earth = twoCanvas.makeCircle(pos.x + offset, pos.y + offset, radius);
//earth.stroke = "#444";
//earth.linewidth = 3;
//earth.fill = "#CCCCCC";
//
//var moonOrbit = twoCanvas.makeCircle(earth.translation.x, earth.translation.y, radius + distance);
//moonOrbit.noFill();
//moonOrbit.linewidth = 2;
//moonOrbit.stroke = "#ccc";
//orbits.add(moonOrbit);
//
//var pos = getPositions(moonAngle, radius + distance),
//    moon = twoCanvas.makeCircle(earth.translation.x + pos.x, earth.translation.y + pos.y, radius / 4);
//moonAngle += 5;
//moon.fill = "#474747";
//twoCanvas.bind("update", function (frameCount) {
//    var pos = getPositions(earthAngle++, orbit);
//    earth.translation.x = pos.x + offset;
//    earth.translation.y = pos.y + offset;
//
//    var moonPos = getPositions(moonAngle, radius + distance);
//    moon.translation.x = earth.translation.x + moonPos.x;
//    moon.translation.y = earth.translation.y + moonPos.y;
//
//    moonAngle += 5;
//    moonOrbit.translation.x = earth.translation.x;
//    moonOrbit.translation.y = earth.translation.y;
//});
//
//function getPositions(angle, orbit) {
//    return {
//        x: Math.cos(angle * Math.PI / 180) * orbit,
//        y: Math.sin(angle * Math.PI / 180) * orbit
//    };
//}
//
//twoCanvas.play();
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    twoCanvas.update();
/////////////////////////////////////////////////  TWO JS  /////////////////////////////////////////////////////////////
};