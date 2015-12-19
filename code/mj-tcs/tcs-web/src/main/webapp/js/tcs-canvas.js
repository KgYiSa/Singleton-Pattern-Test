
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

    var tcsCanvas = this;

    // The actual scene
    var params = { fullscreen: false, // must be false !!!
                   width: dimensions[0],
                   height: dimensions[1],
                   type: Two.Types.webgl,
                   antialias: true
    };
    var twoCanvas = new Two(params).appendTo(document.querySelector('#tcs-canvas'));

    // two axes
    var contentOffset = curConfig.offset;
    var axisOffsetX = contentOffset[0] - ($('#workarea').offset().left - $('#ruler_y').offset().left);
    var axisOffsetY = contentOffset[1] - ($('#workarea').offset().top - $('#ruler_x').offset().top);
    var axisLen = 100;
    var axisX = twoCanvas.makeLine(axisOffsetX, axisOffsetY, axisOffsetX + axisLen, axisOffsetY);
    axisX.linewidth = 0.5;
    axisX.stroke = 'red';
    var axisY = twoCanvas.makeLine(axisOffsetX, axisOffsetY, axisOffsetX, axisOffsetY - axisLen);
    axisY.linewidth = 0.5;
    axisY.stroke = 'green';

    twoCanvas.update();

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
};