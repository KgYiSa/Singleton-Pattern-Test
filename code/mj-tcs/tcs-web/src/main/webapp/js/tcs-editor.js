/*
 * svg-editor.js
 *
 * Licensed under the MIT License
 *
 * Copyright(c) 2010 Alexis Deveria
 * Copyright(c) 2010 Pavol Rusnak
 * Copyright(c) 2010 Jeff Schiller
 * Copyright(c) 2010 Narendra Sisodiya
 * Copyright(c)  2012 Mark MacKay
 *
 */

// Dependencies:
// 1) mousewheel.js
// 2) tcs-canvas.js
//

(function() {
    if(!window.tcsDraw)
        window.tcsDraw = function($) {
        var tcsCanvas;
        var Editor = {};
        var is_ready = false;
        curConfig = {
            canvas_expansion: 1,
            dimensions: [640,480],
            showRulers: true,
            initFill: {color: 'fff', opacity: 1},
            initStroke: {width: 1.5, color: '000', opacity: 1},
            initOpacity: 1,
            imgPath: 'images/',
            extPath: 'extensions/',
            jGraduatePath: 'jgraduate/images/',
            extensions: [],
            initTool: 'select',
            wireframe: false,
            colorPickerCSS: false,
            gridSnapping: false,
            gridColor: "#000",
            baseUnit: 'px',
            snappingStep: 10,
            //showRulers: (svgedit.browser.isTouch()) ? false : true,
            show_outside_canvas: false,
            no_save_warning: true,
            initFont: 'Helvetica, Arial, sans-serif'
        },
        uiStrings = Editor.uiStrings = {
            common: {
                "ok":"OK",
                "cancel":"Cancel",
                "key_up":"Up",
                "key_down":"Down",
                "key_backspace":"Backspace",
                "key_del":"Del"

            },
            notification: {
                "invalidAttrValGiven":"Invalid value given",
                "noContentToFitTo":"No content to fit to",
                "layerHasThatName":"Layer already has that name",
                "QwantToClear":"<strong>Do you want to clear the drawing?</strong>\nThis will also erase your undo history",
                "QwantToOpen":"Do you want to open a new file?\nThis will also erase your undo history",
                "QerrorsRevertToSource":"There were parsing errors in your SVG source.\nRevert back to original SVG source?",
                "QignoreSourceChanges":"Ignore changes made to SVG source?",
                "featNotSupported":"Feature not supported",
                "enterNewImgURL":"Enter the new image URL",
                "defsFailOnSave": "NOTE: Due to a bug in your browser, this image may appear wrong (missing gradients or elements). It will however appear correct once actually saved.",
                "loadingImage":"Loading image, please wait...",
                "saveFromBrowser": "Select \"Save As...\" in your browser to save this image as a %s file.",
                "noteTheseIssues": "Also note the following issues: ",
                "unsavedChanges": "There are unsaved changes.",
                "enterNewLinkURL": "Enter the new hyperlink URL",
                "errorLoadingSVG": "Error: Unable to load SVG data",
                "URLloadFail": "Unable to load from URL",
                "retrieving": 'Retrieving "%s" ...'
            }
        };

        var curPrefs = {}; //$.extend({}, defaultPrefs);
        var customHandlers = {};
        Editor.curConfig = curConfig;
        Editor.tool_scale = 1;

        Editor.setConfig = function(opts) {
            $.extend(true, curConfig, opts);
            if(opts.extensions) {
                curConfig.extensions = opts.extensions;
            }
        };

        Editor.init = function() {
            Editor.canvas = tcsCanvas = new $.TCSCanvas(document.getElementById('tcs-canvas'), curConfig);

            var workarea = $("#workarea");

            var accumulatedDelta = 0
            $('#workarea').on('mousewheel', function(e, delta, deltaX, deltaY){

                if (e.altKey || e.ctrlKey) {
                    e.preventDefault();

                    zoom = parseInt($("#zoom").val())
                    $("#zoom").val(parseInt(zoom + deltaY*(e.altKey ? 10 : 5))).change()
                }
            });

            $('#zoom').change(function(){
                changeZoom(this)
            });

            var changeZoom = function(ctl) {
                var zoomlevel = ctl.value / 100;
                if(zoomlevel < .001) {
                    ctl.value = .1;
                    return;
                }
                var zoom = tcsCanvas.getZoom();
                var w_area = workarea;
                zoomChanged(window, {
                    width: 0,
                    height: 0,
                    // center pt of scroll position
                    x: (w_area[0].scrollLeft + w_area.width()/2)/zoom,
                    y: (w_area[0].scrollTop + w_area.height()/2)/zoom,
                    zoom: zoomlevel
                }, true);
            };

            var zoomChanged = function(window, bbox, autoCenter) {
                var scrbar = 15,
                    res = tcsCanvas.getResolution(),
                    w_area = workarea;
                var z_info = tcsCanvas.setBBoxZoom(bbox, w_area.width()-scrbar, w_area.height()-scrbar);
                if(!z_info) return;
                var zoomlevel = z_info.zoom,
                    bb = z_info.bbox;

                if(zoomlevel < .001) {
                    changeZoom({value: .1});
                    return;
                };
                if (typeof animatedZoom != 'undefined') window.cancelAnimationFrame(animatedZoom)
                // zoom duration 500ms
                var start = Date.now();
                var duration = 500;
                var diff = (zoomlevel) - (res.zoom)
                var zoom = $('#zoom')[0]
                var current_zoom = res.zoom
                var animateZoom = function(timestamp) {
                    var progress = Date.now() - start
                    var tick = progress / duration
                    tick = (Math.pow((tick-1), 3) +1);
                    tcsCanvas.setZoom(current_zoom + (diff*tick));
                    updateCanvas();
                    if (tick < 1 && tick > -.90) {
                        window.animatedZoom = requestAnimationFrame(animateZoom)
                    }
                    else {
                        var canvas = $("#tcs-canvas");
                        tcsCanvas.resizeTwoCanvas(canvas.width(), canvas.height());
                        tcsCanvas.setZoom(zoomlevel);
                        $("#zoom").val(parseInt(zoomlevel*100) + "%")
                        $("option", "#zoom_select").removeAttr("selected")
                        $("option[value="+ parseInt(zoomlevel*100) +"]", "#zoom_select").attr("selected", "selected")
                    }
                };
                animateZoom();

                //if(autoCenter) {
                //  updateCanvas();
                //} else {
                //  updateCanvas(false, {x: bb.x * zoomlevel + (bb.width * zoomlevel)/2, y: bb.y * zoomlevel + (bb.height * zoomlevel)/2});
                //}

                //if(svgCanvas.getMode() == 'zoom' && bb.width) {
                //    // Go to select if a zoom box was drawn
                //    setSelectMode();
                //}

                zoomDone();
            };

            var updateCanvas = Editor.updateCanvas = function(center, new_ctr) {
                var w = workarea.width(), h = workarea.height();
                var w_orig = w, h_orig = h;
                var zoom = tcsCanvas.getZoom();
                var w_area = workarea;
                var cnvs = $("#tcs-canvas");

                var old_ctr = {
                    x: w_area[0].scrollLeft + w_orig/2,
                    y: w_area[0].scrollTop + h_orig/2
                };

                var multi = curConfig.canvas_expansion;
                w = Math.max(w_orig, tcsCanvas.contentW * zoom * multi);
                h = Math.max(h_orig, tcsCanvas.contentH * zoom * multi);
                //console.log("w_orig: " + w_orig + ", contentW: " + svgCanvas.contentW + ", zoom: " + zoom + ", multi: "+multi);
                //console.log("" + w + " " + h);

                if(w == w_orig && h == h_orig) {
                    workarea.css('overflow','hidden');
                } else {
                    workarea.css('overflow','scroll');
                }

                var old_can_y = cnvs.height()/2;
                var old_can_x = cnvs.width()/2;
                cnvs.width(w).height(h);
                var new_can_y = h/2;
                var new_can_x = w/2;
                //var offset = tcsCanvas.updateCanvas(w, h);

                var ratio = new_can_x / old_can_x;

                var scroll_x = w/2 - w_orig/2;
                var scroll_y = h/2 - h_orig/2;

                if(curConfig.showRulers) {
                    updateRulers(cnvs, zoom);
                    workarea.scroll();
                }
            };

            var zoomDone = function() {
                //    updateBgImage();
                updateWireFrame();
                //updateCanvas(); // necessary?
            };

            function updateRulers(scanvas, zoom) {
                var workarea = document.getElementById("workarea");
                var title_show = document.getElementById("title_show");
                if(!zoom) zoom = tcsCanvas.getZoom();
                if(!scanvas) scanvas = $("#tcs-canvas");

                var limit = 30000;

                // todo
                //var units = tcsEdit.units.getTypeMap();
                var unit = 1;//units[curConfig.baseUnit]; // 1 = 1px
                var u_multi = unit * zoom;

                var multi = Editor.calculateZoomMultiplier(u_multi);

                for(var d = 0; d < 2; d++) {
                    var is_x = (d === 0);
                    var dim = is_x ? 'x' : 'y';
                    var lentype = is_x?'width':'height';
                    // TODO:
                    var canvasOffset = tcsCanvas.getContentOffset()[d];//tcsCanvas.contentOffset[d];

                    var $hcanv_orig = $('#ruler_' + dim + ' canvas:first');

                    // Bit of a hack to fully clear the canvas in Safari & IE9
                    $hcanv = $hcanv_orig.clone();
                    $hcanv_orig.replaceWith($hcanv);

                    var hcanv = $hcanv[0];

                    // Set the canvas size to the width of the container
                    var ruler_len = scanvas[lentype]()*2;
                    var total_len = ruler_len;
                    hcanv.parentNode.style[lentype] = total_len + 'px';
                    var ctx_num = 0;
                    var ctx_arr;
                    var ctx = hcanv.getContext("2d");

                    ctx.fillStyle = "rgb(200,0,0)";
                    ctx.fillRect(0,0,hcanv.width,hcanv.height);

                    // Remove any existing canvasses
                    $hcanv.siblings().remove();

                    // Create multiple canvases when necessary (due to browser limits)
                    if(ruler_len >= limit) {
                        var num = parseInt(ruler_len / limit) + 1;
                        ctx_arr = Array(num);
                        ctx_arr[0] = ctx;
                        for(var i = 1; i < num; i++) {
                            hcanv[lentype] = limit;
                            var copy = hcanv.cloneNode(true);
                            hcanv.parentNode.appendChild(copy);
                            ctx_arr[i] = copy.getContext('2d');
                        }

                        copy[lentype] = ruler_len % limit;

                        // set copy width to last
                        ruler_len = limit;
                    }

                    hcanv[lentype] = ruler_len;

                    var big_int = multi * u_multi;
                    ctx.font = "normal 9px 'Lucida Grande', sans-serif";
                    ctx.fillStyle = "#777";

                    var ruler_d = ((canvasOffset / u_multi) % multi) * u_multi;
                    var label_pos = ruler_d - big_int;
                    for (; ruler_d < total_len; ruler_d += big_int) {
                        label_pos += big_int;
                        var real_d = ruler_d - canvasOffset;

                        var cur_d = Math.round(ruler_d) + .5;
                        if(is_x) {
                            ctx.moveTo(cur_d, 15);
                            ctx.lineTo(cur_d, 0);
                        } else {
                            ctx.moveTo(15, cur_d);
                            ctx.lineTo(0, cur_d);
                        }

                        var num = (label_pos - canvasOffset) / u_multi;
                        num = (is_x ? num : -num);

                        var label;
                        if(multi >= 1) {
                            label = Math.round(num);
                        } else {
                            var decs = (multi+'').split('.')[1].length;
                            label = num.toFixed(decs)-0;
                        }

                        // Change 1000s to Ks
                        if(label !== 0 && label !== 1000 && label % 1000 === 0) {
                            label = (label / 1000) + 'K';
                        }

                        if(is_x) {
                            ctx.fillText(label, ruler_d+1+(""+label).length, 9);
                            ctx.fillStyle = "#777";
                        } else {
                            var str = (label+'').split('');
                            for(var i = 0; i < str.length; i++) {
                                ctx.fillText(str[i], 2, (ruler_d + 10 ) + i * 9);
                                ctx.fillStyle = "#777";
                            }
                        }

                        var part = big_int / 10;
                        for(var i = 1; i < 10; i++) {
                            var sub_d = Math.round(ruler_d + part * i) + .5;
                            if(ctx_arr && sub_d > ruler_len) {
                                ctx_num++;
                                ctx.stroke();
                                if(ctx_num >= ctx_arr.length) {
                                    i = 10;
                                    ruler_d = total_len;
                                    continue;
                                }
                                ctx = ctx_arr[ctx_num];
                                ruler_d -= limit;
                                sub_d = Math.round(ruler_d + part * i) + .5;
                            }

                            var line_num = (i % 5)? 12:7/*(i % 2)?12:10*/;
                            if(is_x) {
                                ctx.moveTo(sub_d, 15);
                                ctx.lineTo(sub_d, line_num);
                            } else {
                                ctx.moveTo(15, sub_d);
                                ctx.lineTo(line_num ,sub_d);
                            }
                        }
                    }
                    ctx.strokeStyle = "#666";
                    ctx.stroke();
                }
            };

            var updateWireFrame = function() {
                // Test support
                //if(supportsNonSS) return;

                var rule = "#workarea.wireframe #svgcontent * { stroke-width: " + 1/tcsCanvas.getZoom() + "px; }";
                $('#wireframe_rules').text(workarea.hasClass('wireframe') ? rule : "");
            };

            (function() {
                workarea.scroll(function() {
                    // TODO:  jQuery's scrollLeft/Top() wouldn't require a null check
                    if ($('#ruler_x').length != 0) {
                        $('#ruler_x')[0].scrollLeft = workarea[0].scrollLeft;
                    }
                    if ($('#ruler_y').length != 0) {
                        $('#ruler_y')[0].scrollTop = workarea[0].scrollTop;
                    }
                });
            }());

            // 鼠标位置信息显示
            $(".tcs-editor").mousemove(function(e){
                var x = e.pageX + $('#workarea').scrollLeft() - $('#ruler_y').offset().left;
                var y = e.pageY + $('#workarea').scrollTop()  - $('#ruler_x').offset().top;

                var edit_pos = Editor.mouseCoordinatesToEditorCoordinates([x, y]);

                $(".left-container .top-panel .top-panel-view .tcs-bottom .show-position").html("X: "+edit_pos[0]+", Y: "+edit_pos[1]);
            });

            $('#rulers').on("dblclick", function(e){
                // TODO: RULER GUIDE
                //$("#base_unit_container").css({
                //    top: e.pageY-10,
                //    left: e.pageX-50,
                //    display: 'block'
                //})
            });

            // INVOKE
            updateCanvas(true);
        };

        // u_multi = unit * zoom
        Editor.calculateZoomMultiplier = function(u_multi) {
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
        Editor.mouseCoordinatesToEditorCoordinates = function(mouse_coords) {
            var editor_coords = [0, 0];

            var zoom = tcsCanvas.getZoom();

            // todo
            //var units = tcsEdit.units.getTypeMap();
            var unit = 1;//units[curConfig.baseUnit]; // 1 = 1px
            var u_multi = unit * zoom;

            var multi = Editor.calculateZoomMultiplier(u_multi);

            for(var d = 0; d < 2; d++) {
                var is_x = (d === 0);

                // TODO:
                var canvasOffset = tcsCanvas.getContentOffset()[d];//tcsCanvas.contentOffset[d];

                var num = (mouse_coords[d] - canvasOffset) / u_multi;
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
        Editor.editorCoordinatesToMouseCoordinates = function(editor_coords) {
            var mouse_coords = [0, 0];

            var zoom = tcsCanvas.getZoom();

            // todo
            //var units = tcsEdit.units.getTypeMap();
            var unit = 1;//units[curConfig.baseUnit]; // 1 = 1px
            var u_multi = unit * zoom;

            var multi = Editor.calculateZoomMultiplier(u_multi);

            for(var d = 0; d < 2; d++) {
                var is_x = (d === 0);

                // TODO:
                var canvasOffset = tcsCanvas.getContentOffset()[d];//tcsCanvas.contentOffset[d];

                var num = (editor_coords[d] * u_multi + canvasOffset);
                num = (is_x ? num : -num);

                var label;
                if(multi >= 1) {
                    label = Math.round(num);
                } else {
                    var decs = (multi+'').split('.')[1].length;
                    label = num.toFixed(decs)-0;
                }

                mouse_coords[d] = label;
            }

            return mouse_coords;
        };

        var callbacks = [];

        Editor.ready = function(cb) {
            if(!is_ready) {
                callbacks.push(cb);
            } else {
                cb();
            }
        };

        Editor.runCallbacks = function() {
            $.each(callbacks, function() {
                this();
            });
            is_ready = true;
        };

        return Editor;
    }(jQuery);

    // Run init once DOM is loaded
    $(tcsDraw.init);


})();