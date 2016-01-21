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
        offset:[50, 50] // x: from left to right; y: from bottom to top
    };

    // Update config with new one if given
    if (config) {
        $.extend(curConfig, config);
    }

    var dimensions = curConfig.dimensions;
    var sceneOffset = [0, 0];
    var contentOffset = calculateContentOffset();

    var tcsCanvas = this;

    tcsCanvas.unit = 1;
    tcsCanvas.flagBBox = false;
    tcsCanvas.flagAlready = true;
    var two;
    var twoAxisGroup, twoAxisPos;
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
                //element.translation.set(axisOffset[0], axisOffset[1]);
                //element.children.forEach(function(child) {
                //    child.translation.subSelf({x:axisOffset[0],y:axisOffset[1]});
                //});
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
        //$.each(selectedElements, function(i, elem) {
        //    if (!elem) {return;}
        //    selectorManager.requestSelector(elem).resize();
        //});

        // update content Offset
        contentOffset = calculateContentOffset();
        //contentOffset[1] = $(container).height() - curConfig.offset[1];

        // update axis position
        axisOffset = tcsCanvas.editorCoordinatesToCanvasCoordinates([0, 0]);
        twoAxisGroup.translation.set(axisOffset[0], axisOffset[1]);

        //two.scene.translation.set(axisOffset[0], axisOffset[1]);
        //pathActions.zoomChange();
        //runExtensions('zoomChanged', zoomlevel);

        //var axisOffset = tcsCanvas.editorCoordinatesToMouseCoordinates([0, 0]);
        //twoCanvas.scene.translation.set(axisOffset[0], axisOffset[1]);
        //twoCanvas.renderer.scale = zoomlevel;
        //twoCanvas.update();
    };
    //var adjustBboxX = 0,adjustBboxY = 0;
    var initAreadyFlag = false;
    tcsCanvas.adjustSceneOffset = function () {
        if(!initAreadyFlag){
            initAreadyFlag = true;
            return;
        }
        if(!!tcsCanvas.minEdittorX && !!tcsCanvas.maxEdittorY && tcsCanvas.flagBBox && initAreadyFlag){
            var canvas = $("#tcs-canvas");

            var bbox = tcsCanvas.editorCoordinatesToMouseCoordinates([tcsCanvas.minEdittorX,tcsCanvas.maxEdittorY]);

            bbox[0] += -30;//预留30的长度，防止scene被标尺遮住
            bbox[1] += -30;

            window.tcsDraw.translate(-bbox[0],-bbox[1]);
            $("#workarea").css({"overflow":"auto"});
            canvas.width((boundingBox.x2 - boundingBox.x)*current_zoom*1.2);
            canvas.height((boundingBox.y2 - boundingBox.y)*current_zoom*1.2);
            tcsCanvas.resizeTwoCanvas(canvas.width(), canvas.height());
        }
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
                bb = tcsCanvas.getBBox(true);
                break;
            default:
                return;
        }
        return calcZoom(bb);
    };

    tcsCanvas.getBBox = function(shallow) {
        var rect = two.scene.getBoundingClientRect(shallow);
        //var left_top = tcsCanvas.editorCoordinatesToMouseCoordinates([rect.left, rect.top]);
        //var right_bottom = tcsCanvas.editorCoordinatesToMouseCoordinates([rect.right, rect.bottom]);
        var left_top = [rect.left, rect.top];
        var right_bottom = [rect.right, rect.bottom];
        var value = {
            width: Math.abs(right_bottom[0] - left_top[0]),
            height: Math.abs(right_bottom[1] - left_top[1]),
            x: left_top[0],
            y: left_top[1],
            x2: right_bottom[0],
            y2: right_bottom[1]
            // center pt of scroll position
            //x: (rect.left + rect.right)/zoom, //($('#workarea').offset().left + w_area.width()/2)/zoom,
            //y: (rect.bottom + rect.top)/zoom, //($('#workarea').offset().top + w_area.height()/2)/zoom,
            //zoom: zoomlevel
        };
      return value;
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

    // from DOM (workarea) Space to EDITOR (RULER) Space
    tcsCanvas.mouseCoordinatesToCanvasCoordinates = function(mouse_coords) {
        return [
            mouse_coords[0] - sceneOffset[0],
            mouse_coords[1] - sceneOffset[1]
        ];
    };

    // from EDITOR (RULER) Space to DOM (workarea) Space
    tcsCanvas.canvasCoordinatesToMouseCoordinates = function(canvas_coords) {
        return [
            canvas_coords[0] + sceneOffset[0],
            canvas_coords[1] + sceneOffset[1]
        ];
    };

    // from DOM (workarea) Space to EDITOR (RULER) Space
    // mouse_coords = [x, y]
    tcsCanvas.mouseCoordinatesToEditorCoordinates = function(mouse_coords) {
        var editor_coords = [0, 0];

        var zoom = current_zoom;

        // todo
        //var units = tcsEdit.units.getTypeMap();
        //var unit = 0.1;//units[curConfig.baseUnit]; // 1 = 1px
        var u_multi = tcsCanvas.unit * zoom;

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

    // from EDITOR (RULER) Space to DOM (workarea) Space
    // editor_coords = [x, y]
    tcsCanvas.editorCoordinatesToMouseCoordinates = function(editor_coords) {
        var mouse_coords = [0, 0];

        var zoom = current_zoom;

        // todo
        //var units = tcsEdit.units.getTypeMap();
        //var unit = 0.1;//units[curConfig.baseUnit]; // 1 = 1px
        var u_multi = tcsCanvas.unit * zoom;

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

    /**
     * from EDITOR (RULER) Space to CANVAS Space
     *
     * @param editor_coords
     */
    tcsCanvas.editorCoordinatesToCanvasCoordinates = function(editor_coords) {
        var temp = tcsCanvas.editorCoordinatesToMouseCoordinates(editor_coords);
        return tcsCanvas.mouseCoordinatesToCanvasCoordinates(temp);
    };

    /**
     * from Canvas Space to Editor (RULER) Space
     *
     * @param canvas_coords
     */
    tcsCanvas.canvasCoordinatesToEditorCoordinates = function(canvas_coords) {
        var temp = tcsCanvas.canvasCoordinatesToMouseCoordinates(canvas_coords);
        return tcsCanvas.mouseCoordinatesToEditorCoordinates(temp);
    };

    var axisOffset = tcsCanvas.editorCoordinatesToCanvasCoordinates([0, 0]);
/////////////////////////////////////////////////  TWO JS  /////////////////////////////////////////////////////////////
    twoAxisPos = tcsCanvas.editorCoordinatesToMouseCoordinates([0, 0]);

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

    tcsCanvas.translateScene = function(dx, dy) {
        sceneOffset[0] += dx;
        sceneOffset[1] += dy;
        contentOffset = calculateContentOffset();
    };

    var uuidToElemMap = {};//根据UUID存放对应point, location, vehicle
    var boundingBox;
    tcsCanvas.buildSceneEditor = function(jsonObject,fill) {

        boundingBox = { x: Infinity,
            y: Infinity,
            x2: -Infinity,
            y2: -Infinity};// left ,top, right, bottom

        function calcInnerBBox(bb, bbox) { // DOM Space
            bbox.x = Math.min(bb.left + sceneOffset[0], bbox.x); // left
            bbox.y = Math.min(bb.top + sceneOffset[1], bbox.y); // top
            bbox.x2 = Math.max(bb.right + sceneOffset[0], bbox.x2); // right
            bbox.y2 = Math.max(bb.bottom + sceneOffset[1], bbox.y2);// bottom
            //var tt = tcsCanvas.mouseCoordinatesToEditorCoordinates([bb.left,bb.top]);
            //var rr = tcsCanvas.mouseCoordinatesToEditorCoordinates([bb.right,bb.bottom]);
            //bbox.x = Math.min(tt[0], bbox.x); // left
            //bbox.y = Math.min(tt[1], bbox.y); // top
            //bbox.x2 = Math.max(rr[0], bbox.x2); // right
            //bbox.y2 = Math.max(rr[1], bbox.y2);// bottom
            return bbox;
        };

        tcsCanvas.pause();

        for(var elem in twoElements){
            twoElements[elem].remove();
        }
        //uuidToElemMap.clear();
        uuidToElemMap = {};
        twoElements = [];
        //twoElements.splice(0,twoElements.length);

        if (!jsonObject) {
            return;
        }

        for(var i= 0; i<jsonObject.points.length; i++){
            var jsonPoint = jsonObject.points[i];
            var pointPositionRelativeZero = tcsCanvas.editorCoordinatesToCanvasCoordinates([jsonPoint.display_position_x, jsonPoint.display_position_y]);
            //console.log("raw: ["+jsonPoint.display_position_x+", " + jsonPoint.display_position_y + "] -> new: [" + pointPositionRelativeZero[0] + ", " + pointPositionRelativeZero[1] + "]");
            var point = new Point(pointPositionRelativeZero[0],pointPositionRelativeZero[1],jsonPoint.type,jsonPoint.name,jsonPoint.label_offset_x,jsonPoint.label_offset_y,two);
            twoElements.push(point.group);
            boundingBox = calcInnerBBox(point.getBoundingClientRect(), boundingBox);
            uuidToElemMap[jsonPoint.UUID] = point;
        }
        for(var i= 0; i<jsonObject.locations.length; i++){
            var jsonLocation = jsonObject.locations[i];
            var locationPositionRelativeZero = tcsCanvas.editorCoordinatesToCanvasCoordinates([jsonLocation.display_position_x, jsonLocation.display_position_y]);
            var location = new Location(locationPositionRelativeZero[0],locationPositionRelativeZero[1],jsonLocation.type,jsonLocation.name,jsonLocation.label_offset_x,jsonLocation.label_offset_y,two);
            for(var j=0; j<jsonLocation.attached_links.length; j++){
                var startX = uuidToElemMap[jsonLocation.attached_links[j].point.UUID].display_position_x;
                var startY = uuidToElemMap[jsonLocation.attached_links[j].point.UUID].display_position_y;
                var link = new Link(startX,startY,locationPositionRelativeZero[0],locationPositionRelativeZero[1],two);
                twoElements.push(link.group);
            }
            twoElements.push(location.group);
            boundingBox = calcInnerBBox(location.getBoundingClientRect(), boundingBox);
            uuidToElemMap[jsonLocation.UUID] = location;

        }
        for(var i= 0; i<jsonObject.paths.length; i++){
            var jsonPath = jsonObject.paths[i];
            var startX = uuidToElemMap[jsonPath.source_point.UUID].display_position_x;
            var startY = uuidToElemMap[jsonPath.source_point.UUID].display_position_y;
            var endX = uuidToElemMap[jsonPath.destination_point.UUID].display_position_x;
            var endY = uuidToElemMap[jsonPath.destination_point.UUID].display_position_y;
            var control_points = [];
            for(var p in jsonPath.control_points){
                var canvasControl_point = tcsCanvas.editorCoordinatesToCanvasCoordinates([jsonPath.control_points[p].x, jsonPath.control_points[p].y]);
                control_points.push(canvasControl_point[0]);
                control_points.push(canvasControl_point[1]);
            }
            var path = new Path(startX,startY,endX,endY,jsonPath.max_velocity,jsonPath.max_reverse_velocity,control_points,two);
            twoElements.push(path.group);
        }
        for(var i= 0; i<jsonObject.vehicles.length; i++){
            var jsonVehicle = jsonObject.vehicles[i];
            var vehicle = new Vehicle(0,0,jsonVehicle.name,jsonVehicle.initial_point,two);
            twoElements.push(vehicle.group);
            uuidToElemMap[jsonVehicle.UUID] = vehicle;
        }

        for(var elem in twoElements){
            tcsCanvas.setElemTranslation(twoElements[elem]);
        }

        tcsCanvas.play();
        two.update();

        //Auto fill [IN DOM SPACE!!]
        //if (!_.isUndefined(fill) && (fill === true)) {
        //    var bbox = boundingBox;//tcsCanvas.getBBox(true);
        //
        //    // Find the minimum left top value comparison with origin of the coordinate system (0, 0)
        //    var left_min = Math.min(bbox.x, axisOffset[0]);
        //    var top_min = Math.min(bbox.y, axisOffset[1]);
        //
        //    // calculate the new width & height
        //    var spacer = 1.2;
        //    var width_new = Math.abs(bbox.x2 - left_min) * spacer;
        //    var height_new = Math.abs(bbox.y2 - top_min) * spacer;
        //
        //    // calculate the new zoom level
        //    var canvas = $("#tcs-canvas");
        //    var w_zoom = Math.round((canvas.width() / width_new) * 100) / 100;
        //    var h_zoom = Math.round((canvas.height() / height_new) * 100) / 100;
        //    var zoomLevel = Math.min(w_zoom, h_zoom);
        //
        //    //calculate the new bbox that already zoomed
        //    left_min = (left_min - axisOffset[0]) * spacer * zoomLevel + axisOffset[0];
        //    top_min = (top_min - axisOffset[1]) * spacer * zoomLevel + axisOffset[1];
        //
        //
        //    // calculate the panning value; 0 for positive value, the absolute value otherwise.
        //    //var dx = -(Math.abs(left_min)-15);// 15 为标尺的宽度，不是长度。
        //    //var dy = -(Math.abs(top_min)-15);
        //    var dx = -left_min + 20;
        //    var dy = -top_min - 20;
        //
        //    // zoom & panning now
        //    sceneOffset = [dx, dy];
        //    //contentOffset = calculateContentOffset();
        //    // TODO: ZOOM ??? In tcs-editor.js??
        //    zoomLevel *= 100;
        //    current_zoom = zoomLevel;
        //    return zoomLevel;
        //}
        //Auto fill [IN DOM SPACE!!]
        if (!_.isUndefined(fill) && (fill === true)) {
            var bbox = boundingBox;//tcsCanvas.getBBox(true);

            // Find the minimum left top value comparison with origin of the coordinate system (0, 0)
            var left_min = Math.min(bbox.x, axisOffset[0]);
            var top_min = Math.max(bbox.y2, axisOffset[1]/*0*/);

            // calculate the new width & height
            var spacer = 1.2;
            var width_new = Math.abs(bbox.x2 - left_min) * spacer;
            var height_new = Math.abs(top_min - bbox.y) * spacer;

            // calculate the new zoom level
            var canvas = $("#tcs-canvas");
            var w_zoom = Math.round((canvas.width() / width_new)*100)/100;
            var h_zoom = Math.round((canvas.height() / height_new)*100)/100;
            var zoomLevel = Math.min(w_zoom, h_zoom);

            // calculate the panning value; 0 for positive value, the absolute value otherwise.
            var dx = ((left_min < 0) ? -left_min : 0)*zoomLevel;
            var dy = ((top_min > axisOffset[1]) ? -(top_min-axisOffset[1]) : 0)*zoomLevel;

            // zoom & panning now
            sceneOffset = [dx, dy];
            // TODO: ZOOM ??? In tcs-editor.js??
            zoomLevel *= 100;
            current_zoom = zoomLevel;
            return zoomLevel;
        }
        //if (!_.isUndefined(fill) && (fill === true)) {
        //    var bbox = tcsCanvas.getBBox(true);
        //
        //    // Find the minimum left top value comparison with (0, 0)
        //    var left_min = Math.min(bbox.x, 0);
        //    var top_min = Math.min(bbox.y, 0);
        //
        //    // calculate the new width & height
        //    var width_new = Math.abs(bbox.x2 - left_min);
        //    var height_new = Math.abs(bbox.y2 - top_min);
        //
        //    // calculate the new zoom level
        //    var canvas = $("#tcs-canvas");
        //    var spacer = 0.85;
        //    var w_zoom = Math.round((width_new / canvas.width())*100 * spacer)/100;
        //    var h_zoom = Math.round((height_new / canvas.height())*100 * spacer)/100;
        //    var zoomLevel = 1.0 / Math.max(w_zoom, h_zoom);
        //
        //    // calculate the panning value; 0 for positive value, the absolute value otherwise.
        //    var dx = (left_min < 0) ? -left_min : 0;
        //    var dy = (top_min < 0) ? -top_min : 0;
        //
        //    // zoom & panning now
        //    //sceneOffset = [dx, dy];
        //    // TODO: ZOOM ??? In tcs-editor.js??
        //    current_zoom = zoomLevel;
        //    return zoomLevel;
        //}

    };

    tcsCanvas.parseVehiclePosition = function(jsonStatusObject){

        if(!jsonStatusObject.uuid)return;
        var currentVehicle = uuidToElemMap[jsonStatusObject.uuid];

        if(!!jsonStatusObject.position_uuid){
            var precisePointUUID,precisePoint,x,y;
            precisePointUUID = jsonStatusObject.position_uuid;
            precisePoint = !!uuidToElemMap[precisePointUUID] ? uuidToElemMap[precisePointUUID] : 0;
            x = !!precisePoint ? precisePoint.pointRaw.translation.x : 0;
            y = !!precisePoint ? precisePoint.pointRaw.translation.y : 0;
            currentVehicle.setVehiclePosition(x,y,precisePointUUID);
        }else{
            currentVehicle.setVehiclePosition(0,0,0);
        }

    };
    tcsCanvas.setElemTranslation = function(elem){
        elem.children.forEach(function(child) {
            child.translation.subSelf({x:axisOffset[0],y:axisOffset[1]});
        });
        //elem.translation.set(axisOffset[0], axisOffset[1]);
    };
    tcsCanvas.autoResetScene = function(jsonObject) {
        // Reset
        current_zoom = 1.0;
        sceneOffset = [0, 0];
        contentOffset = calculateContentOffset();
        two.update();

        if(!jsonObject){
            return;
        }

        var zoomLevel = 0.3;//缩放比例大于50%下 scene显示较为清晰
        var initRulerLength = 1100,
            initRulerWidth = 500;//于tcsCanvas.unit = 1,zoomLevel = 1时，标尺显示的长宽

        var minX = Infinity,
            minY = Infinity,
            maxX = -Infinity,
            maxY = -Infinity;
        for(var p in jsonObject.points){
            var jsonPoint = jsonObject.points[p];
            minX = Math.min(minX,jsonPoint.display_position_x);
            minY = Math.min(minY,jsonPoint.display_position_y);
            maxX = Math.max(maxX,jsonPoint.display_position_x);
            maxY = Math.max(maxY,jsonPoint.display_position_y);
        }
        for(var p in jsonObject.locations){
            var jsonLocation = jsonObject.locations[p];
            minX = Math.min(minX,jsonLocation.display_position_x);
            minY = Math.min(minY,jsonLocation.display_position_y);
            maxX = Math.max(maxX,jsonLocation.display_position_x);
            maxY = Math.max(maxY,jsonLocation.display_position_y);
        }

        if ((minX != Infinity && minX != -Infinity) &&
            (minY != Infinity && minY != -Infinity) &&
            (maxX != Infinity && maxX != -Infinity) &&
            (maxY != Infinity && maxY != -Infinity)) {
            var length = maxX - minX,
                width = maxY - minY;
            tcsCanvas.minEdittorX = minX;
            tcsCanvas.maxEdittorY = maxY;

            tcsCanvas.unit = Math.min(initRulerLength / length / zoomLevel, initRulerWidth / width / zoomLevel);
        }
    };
    tcsCanvas.showText = function () {
        $.each(uuidToElemMap,function(key,value){
            value.setTextOpacity();
        });
    };
    tcsCanvas.showElemInScene = function (uuid) {
        if(!!uuid && !!uuidToElemMap[uuid]){
            $.each(uuidToElemMap,function(key,value){
                value.setHighlight(false);
            });

            var elem = uuidToElemMap[uuid];
            switch (elem.elem) {
                case 'Point':
                    elem.setHighlight(true);
                    break;
                case 'Location':
                    break;
                case 'Vehicle':
                    elem.setHighlight(true);
                    break;
                default:
                    return;
            }
        }

    };
/////////////////////////////////////////////////  TWO JS  /////////////////////////////////////////////////////////////

    function calculateContentOffset() {
        return [
            curConfig.offset[0] + sceneOffset[0],
            $(container).height() - curConfig.offset[1] + sceneOffset[1]
        ];
    };

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
            type: Two.Types.canvas,
            antialias: true,
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

        sceneOffset = [0, 0];

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
        var twoAxisX = two.makeLine(x, y, x + axisLen, y);
        twoAxisX.linewidth = 0.75;
        twoAxisX.stroke = 'red';

        var twoAxisY = two.makeLine(x, y, x, y - axisLen);
        twoAxisY.linewidth = 0.7;
        twoAxisY.stroke = 'green';

        //var test = two.makeRectangle(x,y, 100, 50);
        //test.fill = 'red';
        //twoElements.push(test);

        twoAxisGroup = two.makeGroup(twoAxisX, twoAxisY);
        var corner = {
            x: x,
            y: y
        };
        twoAxisGroup.children.forEach(function(child) {
            child.translation.subSelf(corner);
        });
        twoAxisGroup.translation.set(x, y);
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

        //if(!!flagBbox){
            //var canvas = $("#tcs-canvas");
            //$("#workarea").css({"overflow":"auto"});
            //two.scene.translation.subSelf(((boundingBox.x2 - boundingBox.x)*current_zoom - canvas.width()),((boundingBox.y2 - boundingBox.y)*current_zoom - canvas.height()));
            //two.scene.translation.set(100,100);
            //console.log("width_old = "+canvas.width()+"height_old = "+canvas.height()+"width_new = "+ (boundingBox.x2 - boundingBox.x)*current_zoom*1.2 + "height_new = "+(boundingBox.y2 - boundingBox.y)*current_zoom*1.2 );
            //canvas.css({"width":(boundingBox.x2 - boundingBox.x)*current_zoom*1.2});
            //canvas.css({"height":(boundingBox.y2 - boundingBox.y)*current_zoom*1.2});
            //canvas.width((boundingBox.x2 - boundingBox.x)*current_zoom*1.2);
            //canvas.height((boundingBox.y2 - boundingBox.y)*current_zoom*1.2);


        //}
        two.scene.translation.set(sceneOffset[0], sceneOffset[1]);

        _.each(twoElements, function(element) {

            _.each(twoOperations, function(operation) {
                if (operation.enabled) {
                    operation.update(element);
                }
            });

        });
    };


    (function () {
        initializeScene();

        initializeAxes(axisOffset[0], axisOffset[1]);

    }());
};