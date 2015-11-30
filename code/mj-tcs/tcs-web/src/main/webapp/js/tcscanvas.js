
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

NS = {
    HTML: 'http://www.w3.org/1999/xhtml',
    MATH: 'http://www.w3.org/1998/Math/MathML',
    SE: 'http://svg-edit.googlecode.com',
    SVG: 'http://www.w3.org/2000/svg',
    XLINK: 'http://www.w3.org/1999/xlink',
    XML: 'http://www.w3.org/XML/1998/namespace',
    XMLNS: 'http://www.w3.org/2000/xmlns/' // see http://www.w3.org/TR/REC-xml-names/#xmlReserved
};

// Class: TCSCanvas
// The main TCSCanvas class that manages all TCS-related functions
//
// Parameters:
// container - The container HTML element that should hold the TCS root element
// config - An object that contains configuration data
$.TCSCanvas = function(container, config) {

    // Default configuration options
    var curConfig = {
        show_outside_canvas: true,
        selectNew: true,
        dimensions: [640, 480]
    };

    // Update config with new one if given
    if (config) {
        $.extend(curConfig, config);
    }

    // Array with width/height of canvas
    var dimensions = curConfig.dimensions;

    var canvas = this;


    // "document" element associated with the container (same as window.document using default svg-editor.js)
    // NOTE: This is not actually a TCS document, but a HTML document.
    var svgdoc = container.ownerDocument;

    // This is a container for the document being edited, not the document itself.
    //var tcsroot = doc.importNode(svgedit.utilities.text2xml(
    //    '<svg id="svgroot" xmlns="' + NS.SVG + '" xlinkns="' + NS.XLINK + '" ' +
    //    'width="' + dimensions[0] + '" height="' + dimensions[1] + '" x="' + dimensions[0] + '" y="' + dimensions[1] + '" overflow="visible">' +
    //    '<defs>' +
    //    '<filter id="canvashadow" filterUnits="objectBoundingBox">' +
    //    '<feGaussianBlur in="SourceAlpha" stdDeviation="4" result="blur"/>'+
    //    '<feOffset in="blur" dx="5" dy="5" result="offsetBlur"/>'+
    //    '<feMerge>'+
    //    '<feMergeNode in="offsetBlur"/>'+
    //    '<feMergeNode in="SourceGraphic"/>'+
    //    '</feMerge>'+
    //    '</filter>'+
    //    '</defs>'+
    //    '</svg>').documentElement, true);
    //container.appendChild(svgroot);

    // The actual element that represents the final output SVG element
    var svgcontent = svgdoc.createElementNS(NS.SVG, 'svg');

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

    // Array with all the currently selected elements
    // default size of 1 until it needs to grow bigger
    var selectedElements = new Array(1);

    // Mouse events
    (function () {
    }());


    // Function: setResolution
    // Changes the document's dimensions to the given size
    //
    // Parameters:
    // x - Number with the width of the new dimensions in user units.
    // Can also be the string "fit" to indicate "fit to content"
    // y - Number with the height of the new dimensions in user units.
    //
    // Returns:
    // Boolean to indicate if resolution change was succesful.
    // It will fail on "fit to content" option with no content to fit to.
    this.setResolution = function(x, y) {
        var res = getResolution();
        var w = res.w, h = res.h;
        var batchCmd;

        if (x == 'fit') {
            // TODO:
            // Get bounding box
            //var bbox = getStrokedBBox();
            //
            //if (bbox) {
            //    batchCmd = new svgedit.history.BatchCommand('Fit Canvas to Content');
            //    var visEls = getVisibleElements();
            //    addToSelection(visEls);
            //    var dx = [], dy = [];
            //    $.each(visEls, function(i, item) {
            //        dx.push(bbox.x*-1);
            //        dy.push(bbox.y*-1);
            //    });
            //
            //    var cmd = canvas.moveSelectedElements(dx, dy, true);
            //    batchCmd.addSubCommand(cmd);
            //    clearSelection();
            //
            //    x = Math.round(bbox.width);
            //    y = Math.round(bbox.height);
            //} else {
            //    return false;
            //}
        }
        if (x != w || y != h) {
            var handle = svgroot.suspendRedraw(1000);
            if (!batchCmd) {
                batchCmd = new svgedit.history.BatchCommand('Change Image Dimensions');
            }

            x = svgedit.units.convertToNum('width', x);
            y = svgedit.units.convertToNum('height', y);

            svgcontent.setAttribute('width', x);
            svgcontent.setAttribute('height', y);

            this.contentW = x;
            this.contentH = y;
            batchCmd.addSubCommand(new svgedit.history.ChangeElementCommand(svgcontent, {'width':w, 'height':h}));

            svgcontent.setAttribute('viewBox', [0, 0, x/current_zoom, y/current_zoom].join(' '));
            batchCmd.addSubCommand(new svgedit.history.ChangeElementCommand(svgcontent, {'viewBox': ['0 0', w, h].join(' ')}));

            addCommandToHistory(batchCmd);
            svgroot.unsuspendRedraw(handle);
            call('changed', [svgcontent]);
        }
        return true;
    };

    // Function: getResolution
    // Returns the current dimensions and zoom level in an object
    var getResolution = this.getResolution = function() {
    //		var vb = svgcontent.getAttribute('viewBox').split(' ');
    //		return {'w':vb[2], 'h':vb[3], 'zoom': current_zoom};

        var width = svgcontent.getAttribute('width')/current_zoom;
        var height = svgcontent.getAttribute('height')/current_zoom;

        return {
            'w': width,
            'h': height,
            'zoom': current_zoom
        };
    };

    // Function: setZoom
    // Sets the zoom to the given level
    //
    // Parameters:
    // zoomlevel - Float indicating the zoom level to change to
    this.setZoom = function(zoomlevel) {
        var res = getResolution();
        svgcontent.setAttribute('viewBox', '0 0 ' + res.w/zoomlevel + ' ' + res.h/zoomlevel);
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
    this.getZoom = function(){return current_zoom;};


    this.contentW = 580;//getResolution().w;
    this.contentH = 400;//getResolution().h;

// Function: updateCanvas
// Updates the editor canvas width/height/position after a zoom has occurred
//
// Parameters:
// w - Float with the new width
// h - Float with the new height
//
// Returns:
// Object with the following values:
// * x - The canvas' new x coordinate
// * y - The canvas' new y coordinate
// * old_x - The canvas' old x coordinate
// * old_y - The canvas' old y coordinate
// * d_x - The x position difference
// * d_y - The y position difference
    this.updateCanvas = function(w, h) {
        //svgroot.setAttribute('width', w);
        //svgroot.setAttribute('height', h);
        var bg = $('#canvasBackground')[0];
        var old_x = svgcontent.getAttribute('x');
        var old_y = svgcontent.getAttribute('y');
        var x = (w/2 - this.contentW*current_zoom/2);
        var y = (h/2 - this.contentH*current_zoom/2);

        //assignAttributes(svgcontent, {
        //    width: this.contentW*current_zoom,
        //    height: this.contentH*current_zoom,
        //    'x': x,
        //    'y': y,
        //    'viewBox' : '0 0 ' + this.contentW + ' ' + this.contentH
        //});
        //
        //assignAttributes(bg, {
        //    width: svgcontent.getAttribute('width'),
        //    height: svgcontent.getAttribute('height'),
        //    x: x,
        //    y: y
        //});

        //var bg_img = svgedit.utilities.getElem('background_image');
        //if (bg_img) {
        //    svgedit.utilities.assignAttributes(bg_img, {
        //        'width': '100%',
        //        'height': '100%'
        //    });
        //}
        //
        //selectorManager.selectorParentGroup.setAttribute('transform', 'translate(' + x + ',' + y + ')');
        //runExtensions('canvasUpdated', {new_x:x, new_y:y, old_x:old_x, old_y:old_y, d_x:x - old_x, d_y:y - old_y});
        return {x:x, y:y, old_x:old_x, old_y:old_y, d_x:x - old_x, d_y:y - old_y};
    };

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
            var zoomlevel = Math.min(w_zoom, h_zoom);
            canvas.setZoom(zoomlevel);
            return {'zoom': zoomlevel, 'bbox': bb};
        };

        if (typeof val == 'object') {
            bb = val;
            if (bb.width == 0 || bb.height == 0) {
                var newzoom = bb.zoom ? bb.zoom : current_zoom * bb.factor;
                canvas.setZoom(newzoom);
                return {'zoom': current_zoom, 'bbox': bb};
            }
            return calcZoom(bb);
        }

        switch (val) {
            case 'selection':
                if (!selectedElements[0]) {return;}
                var sel_elems = $.map(selectedElements, function(n){ if (n) {return n;} });
                bb = getStrokedBBox(sel_elems);
                break;
            case 'canvas':
                var res = getResolution();
                spacer = 0.95;
                bb = {width:res.w, height:res.h , x:0, y:0};
                break;
            case 'content':
                bb = getStrokedBBox();
                break;
            case 'layer':
                bb = getStrokedBBox(getVisibleElements(getCurrentDrawing().getCurrentLayer()));
                break;
            default:
                return;
        }
        return calcZoom(bb);
    };
};