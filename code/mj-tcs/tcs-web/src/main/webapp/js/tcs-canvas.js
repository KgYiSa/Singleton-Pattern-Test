"use strict";
// Dependencies:
// 1) underscore.js
// 2) has.js
// 3) two.js

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

    var two;
    var twoAxisX, twoAxisY, twoAxisPos;
    var twoElements = [];
    var twoFlags = [
        '_flagMatrix',
        '_flagScale',
        '_flagVertices',
        '_flagFill',
        '_flagStroke',
        '_flagLinewidth',
        '_flagOpacity',
        '_flagVisible',
        '_flagCap',
        '_flagJoin',
        '_flagMiter'
    ];
    var velocity_x = 1;
    var twoOperations = {
        //TODO
        translation: {
            enabled: true,
            update: function(element) {

                //var w = two.width;
                //var h = two.height;
                //
                //var range_hi = tcsCanvas.mouseCoordinatesToEditorCoordinates([w, h]);
                //var range_lo = tcsCanvas.mouseCoordinatesToEditorCoordinates([0, 0]);
                //
                ////console.log("x: " + element.translation.x + ", y: " + element.translation.y);
                //element.translation.x += velocity_x * 1;
                //if (element.translation.x > range_hi[0] - 10) {
                //    velocity_x = -1;
                //} else if (element.translation.x < range_lo[0] + 10) {
                //    velocity_x = 1;
                //};

                //if ((element.translation.y < h && element.velocity.y < 0)
                //    || (element.translation.y > two.height - h && element.velocity.y > 0)) {
                //    element.velocity.y *= -1;
                //}
            }
        },
        scale: {
            enabled: true,
            update: function(element) {
                element.translation.set(axisOffset[0], axisOffset[1]);
                element.scale = current_zoom;
            }
        }
    };


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

    // Function: getResolution
    // Returns the current dimensions and zoom level in an object
    var getResolution = tcsCanvas.getResolution = function() {
        var width = dimensions[0]/current_zoom;
        var height = dimensions[1]/current_zoom;

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
    tcsCanvas.setZoom = function(zoomlevel) {
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

        //var axisOffset = tcsCanvas.editorCoordinatesToMouseCoordinates([0, 0]);
        //twoCanvas.scene.translation.set(axisOffset[0], axisOffset[1]);
        //twoCanvas.renderer.scale = zoomlevel;
        //twoCanvas.update();
    };

    // Function: getZoom
    // Returns the current zoom level
    tcsCanvas.getZoom = function() {return current_zoom;};

    tcsCanvas.getContentOffset = function() {return contentOffset;};

    // Note: use method to return not function !!!
    tcsCanvas.contentW = getResolution().w;
    tcsCanvas.contentH = getResolution().h;

    // Function: setBBoxZoom
    // Sets the zoom level on the canvas-side based on the given value
    //
    // Parameters:
    // val - Bounding box object to zoom to or string indicating zoom option
    // editor_w - Integer with the editor's workarea box's width
    // editor_h - Integer with the editor's workarea box's height
    tcsCanvas.setBBoxZoom = function(val, editor_w, editor_h) {
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
    tcsCanvas.calculateZoomMultiplier = function(u_multi) {
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
    tcsCanvas.mouseCoordinatesToEditorCoordinates = function(mouse_coords) {
        var editor_coords = [0, 0];

        var zoom = current_zoom;

        // todo
        //var units = tcsEdit.units.getTypeMap();
        var unit = 0.1;//units[curConfig.baseUnit]; // 1 = 1px
        var u_multi = unit * zoom;

        var multi = tcsCanvas.calculateZoomMultiplier(u_multi);

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
    tcsCanvas.editorCoordinatesToMouseCoordinates = function(editor_coords) {
        var mouse_coords = [0, 0];

        var zoom = current_zoom;

        // todo
        //var units = tcsEdit.units.getTypeMap();
        var unit = 0.1;//units[curConfig.baseUnit]; // 1 = 1px
        var u_multi = unit * zoom;

        var multi = tcsCanvas.calculateZoomMultiplier(u_multi);

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

    var axisOffset = tcsCanvas.editorCoordinatesToMouseCoordinates([0, 0]);
/////////////////////////////////////////////////  TWO JS  /////////////////////////////////////////////////////////////
    twoAxisPos = tcsCanvas.editorCoordinatesToMouseCoordinates([0, 0]);

    function initializeScene() {
        if (!has.webgl) {
            alert('This browser does not support WebGL.');
            return;
        }

        // Remove any previous instances
        _.each(Two.Instances, function(two) {
            Two.Utils.release(two);
            $(two.renderer.domElement).remove();
        });

        // Create a new instance
        var params = {
            fullscreen: false, // must be false !!!
            width: dimensions[0],
            height: dimensions[1],
            type: Two.Types.webgl,
            //antialias: true,
            autostart: true
        };
        two = new Two(params).appendTo(document.querySelector('#tcs-canvas'));

        // add grid line
        //var gridlist = [];
        //var gridlength = 10;
        //for(var i=0;i<200;i ++){
        //    gridlist[i]=new Array;
        //    for(var j=0;j<200;j++){
        //        gridlist[i][j]='';
        //    }
        //}
        //
        //for(var i=0;i<200;i++){
        //    var t=two.makeLine(gridlength*i, 0, gridlength*i, gridlength*200);
        //    t.stroke = 'black';
        //    t.opacity = 0.3;
        //    lineh.push(t);
        //    //twoElements.push(t);
        //}
        //for(var j=0;j<200;j++){
        //    var t=two.makeLine( 0, gridlength*j, gridlength*200,gridlength*j);
        //    t.stroke = 'black';
        //    t.opacity = 0.3;
        //    linew.push(t);
        //    //twoElements.push(t);
        //}

        // Setup the size
        _.extend(two.renderer.domElement.style, {
            //position: 'absolute',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0
        });

        // Reset flags and private variables
        _.each(twoElements, function(element) {

            element._renderer = { type: element._renderer.type };

            _.each(twoFlags, function(flag) {
                if (_.isUndefined(element[flag])) {
                    return;
                }
                element[flag] = true;

            });

        });

        two.bind('update', update);

        // TEST
        //var editor_in = [200, 100];
        //var canvas_coord = tcsCanvas.editorCoordinatesToMouseCoordinates(editor_in);
        //var text_styles={
        //    size:10,
        //    linewidth:1,
        //    alignment:'center'
        //};
        //var _x = 0;//canvas_coord[0],
        //    _y = 0;//canvas_coord[1];
        //var cross_x = two.makeLine(_x - 2000, _y, _x + 2000, _y);
        //cross_x.stroke = 'blue';
        //cross_x.linewidth = 1.0;
        //var cross_y = two.makeLine(_x, _y - 2000, _x, _y + 2000);
        //cross_y.stroke = 'blue';
        //cross_y.linewidth = 1.0;
        //var text = two.makeText("x: " + editor_in[0] + ", y: " + editor_in[1], _x+45, _y-10, text_styles);
        //var group = two.makeGroup(cross_x, cross_y, text);
        ////group.add(cross_x);
        ////group.add(cross_y);
        ////group.add(text);
        //group.translation.set(canvas_coord[0], canvas_coord[1]);
        //
        //twoElements.push(group);
    };

    function initializeAxes(x, y) {
        // two axes
        var axisLen = 100;
        twoAxisX = two.makeLine(x, y, x + axisLen, y);
        twoAxisX.linewidth = 0.75;
        twoAxisX.stroke = 'red';

        twoAxisY = two.makeLine(x, y, x, y - axisLen);
        twoAxisY.linewidth = 0.7;
        twoAxisY.stroke = 'green';

        //two.update();
    };

    function update() {
        //// TODO
        //if (shouldAdd) {
        //    addParticle();
        //}

        //// TODO
        //if (shouldRemove) {
        //    removeParticle();
        //}

        _.each(twoElements, function(element) {

            _.each(twoOperations, function(operation) {
                if (operation.enabled) {
                    operation.update(element);
                }
            });

        });
    };

    tcsCanvas.play = function() {
        two.play();
    };

    tcsCanvas.pause = function() {
        two.pause();
    };

    tcsCanvas.resizeTwoCanvas = function(w, h) {
        two.renderer.setSize(w, h);
        two.width = two.renderer.width;
        two.height = two.renderer.height;
    };
    var uuidToElemMap = new Map();//根据UUID存放对应point, location, vehicle
    tcsCanvas.buildSceneEditor = function(jsonObject) {

        tcsCanvas.pause();

        for(var elem in twoElements){
            twoElements[elem].remove();
        }
        uuidToElemMap.clear();
        twoElements.splice(0,twoElements.length);

        if (!jsonObject) {
            return;
        }

        for(var i= 0; i<jsonObject.points.length; i++){
            var jsonPoint = jsonObject.points[i];
            var pointPositionRelativeZero = tcsCanvas.editorCoordinatesToMouseCoordinates([jsonPoint.display_position_x, jsonPoint.display_position_y]);
            var point = new Point(pointPositionRelativeZero[0],pointPositionRelativeZero[1],jsonPoint.type,jsonPoint.name,jsonPoint.label_offset_x,jsonPoint.label_offset_y,two);
            twoElements.push(point.point);
            //uuidToElemMap[jsonPoint.UUID] = point;
            uuidToElemMap.set(jsonPoint.UUID,point);
        }
        for(var i= 0; i<jsonObject.locations.length; i++){
            var jsonLocation = jsonObject.locations[i];
            var locationPositionRelativeZero = tcsCanvas.editorCoordinatesToMouseCoordinates([jsonLocation.display_position_x, jsonLocation.display_position_y]);
            var location = new Location(locationPositionRelativeZero[0],locationPositionRelativeZero[1],jsonLocation.type,jsonLocation.name,jsonLocation.label_offset_x,jsonLocation.label_offset_y,two);
            for(var j=0; j<jsonLocation.attached_links.length; j++){
                //var startX = uuidToElemMap[jsonLocation.attached_links[j].point.UUID].display_position_x;
                //var startY = uuidToElemMap[jsonLocation.attached_links[j].point.UUID].display_position_y;
                var startX = uuidToElemMap.get(jsonLocation.attached_links[j].point.UUID).display_position_x;
                var startY = uuidToElemMap.get(jsonLocation.attached_links[j].point.UUID).display_position_y;
                var link = new Link(startX,startY,locationPositionRelativeZero[0],locationPositionRelativeZero[1],two);
                twoElements.push(link.link);
            }
            twoElements.push(location.location);
            //uuidToElemMap[jsonLocation.UUID] = location;
            uuidToElemMap.set(jsonLocation.UUID,location);

        }
        for(var i= 0; i<jsonObject.paths.length; i++){
            var jsonPath = jsonObject.paths[i];
            //var startX = uuidToElemMap[jsonPath.source_point.UUID].display_position_x;
            //var startY = uuidToElemMap[jsonPath.source_point.UUID].display_position_y;
            //var endX = uuidToElemMap[jsonPath.destination_point.UUID].display_position_x;
            //var endY = uuidToElemMap[jsonPath.destination_point.UUID].display_position_y;
            var startX = uuidToElemMap.get(jsonPath.source_point.UUID).display_position_x;
            var startY = uuidToElemMap.get(jsonPath.source_point.UUID).display_position_y;
            var endX = uuidToElemMap.get(jsonPath.destination_point.UUID).display_position_x;
            var endY = uuidToElemMap.get(jsonPath.destination_point.UUID).display_position_y;
            var path = new Path(startX,startY,endX,endY,jsonPath.max_velocity,jsonPath.max_reverse_velocity,two);
            twoElements.push(path.path);
        }
        for(var i= 0; i<jsonObject.vehicles.length; i++){
            var jsonVehicle = jsonObject.vehicles[i];
            //var pointX = uuidToElemMap[jsonVehicle.initial_point].display_position_x;
            //var pointY = uuidToElemMap[jsonVehicle.initial_point].display_position_y;
            if(jsonVehicle.initial_point == "" || jsonVehicle.initial_point == null)continue;
            var pointX = uuidToElemMap.get(jsonVehicle.initial_point).display_position_x;
            var pointY = uuidToElemMap.get(jsonVehicle.initial_point).display_position_y;
            var vehicle = new Vehicle(pointX,pointY,jsonVehicle.name,jsonVehicle.initial_point,two);
            twoElements.push(vehicle.vehicle);
            //uuidToElemMap[jsonVehicle.UUID] = vehicle;
            uuidToElemMap.set(jsonVehicle.UUID,vehicle);
        }

        tcsCanvas.play();

    };

    tcsCanvas.parseVehiclePosition = function(jsonStatusObject){

        var currentVehicle = uuidToElemMap.get(jsonStatusObject.UUID);

        var initialPointUUID;
        var currentPointUUID;
        var precisePointUUID = jsonStatusObject.precise_position;

        initialPointUUID = currentVehicle.initialPointUUID;
        currentPointUUID = currentVehicle.currentPointUUID;

        var initialPoint,currentPoint,precisePoint;

        initialPoint = uuidToElemMap.get(initialPointUUID);
        currentPoint = uuidToElemMap.get(currentPointUUID);
        precisePoint = uuidToElemMap.get(precisePointUUID);

        currentVehicle.setVehicleDirection(currentPoint.pointOrigin.translation,precisePoint.pointOrigin.translation);

        //相对于初始点的translation
        var x,y;
        x = precisePoint.pointOrigin.translation.x-initialPoint.pointOrigin.translation.x;
        y = precisePoint.pointOrigin.translation.y-initialPoint.pointOrigin.translation.y;
        currentVehicle.setVehiclePosition(x,y,precisePointUUID);

    };
/////////////////////////////////////////////////  TWO JS  /////////////////////////////////////////////////////////////

    // Mouse events
    (function () {
        initializeScene();

        initializeAxes(axisOffset[0], axisOffset[1]);

    }());
};