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
// 2) tcscanvas.js
//

(function() {
    if(!window.tcsDraw)
        window.tcsDraw = function($) {
        var svgCanvas;
        var Editor = {};
        var is_ready = false;
        curConfig = {
            canvas_expansion: 1,
            dimensions: [580,400],
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
        }

        Editor.init = function() {
            Editor.canvas = svgCanvas = new $.TCSCanvas(document.getElementById('svgcanvas'), curConfig);

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
                var zoom = svgCanvas.getZoom();
                var workarea = $("#workarea")
                var w_area = workarea;
                zoomChanged(window, {
                    width: 0,
                    height: 0,
                    // center pt of scroll position
                    x: (w_area[0].scrollLeft + w_area.width()/2)/zoom,
                    y: (w_area[0].scrollTop + w_area.height()/2)/zoom,
                    zoom: zoomlevel
                }, true);
            }

            var zoomChanged = function(window, bbox, autoCenter) {
                var scrbar = 15,
                    res = svgCanvas.getResolution(),
                    w_area = workarea,
                    canvas_pos = $('#svgcanvas').position();
                var z_info = svgCanvas.setBBoxZoom(bbox, w_area.width()-scrbar, w_area.height()-scrbar);
                if(!z_info) return;
                var zoomlevel = z_info.zoom,
                    bb = z_info.bbox;

                if(zoomlevel < .001) {
                    changeZoom({value: .1});
                    return;
                }
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
                    svgCanvas.setZoom(current_zoom + (diff*tick));
                    updateCanvas();
                    if (tick < 1 && tick > -.90) {
                        window.animatedZoom = requestAnimationFrame(animateZoom)
                    }
                    else {
                        $("#zoom").val(parseInt(zoomlevel*100) + "%")
                        $("option", "#zoom_select").removeAttr("selected")
                        $("option[value="+ parseInt(zoomlevel*100) +"]", "#zoom_select").attr("selected", "selected")
                    }
                }
                animateZoom()

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
            }

            var updateCanvas = Editor.updateCanvas = function(center, new_ctr) {
                var w = workarea.width(), h = workarea.height();
                var w_orig = w, h_orig = h;
                var zoom = svgCanvas.getZoom();
                var w_area = workarea;
                var cnvs = $("#svgcanvas");

                var old_ctr = {
                    x: w_area[0].scrollLeft + w_orig/2,
                    y: w_area[0].scrollTop + h_orig/2
                };

                var multi = curConfig.canvas_expansion;
                w = Math.max(w_orig, svgCanvas.contentW * zoom * multi);
                h = Math.max(h_orig, svgCanvas.contentH * zoom * multi);
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
                var offset = svgCanvas.updateCanvas(w, h);

                var ratio = new_can_x / old_can_x;

                var scroll_x = w/2 - w_orig/2;
                var scroll_y = h/2 - h_orig/2;

                if(!new_ctr) {

                    var old_dist_x = old_ctr.x - old_can_x;
                    var new_x = new_can_x + old_dist_x * ratio;

                    var old_dist_y = old_ctr.y - old_can_y;
                    var new_y = new_can_y + old_dist_y * ratio;

                    new_ctr = {
                        x: new_x,
                        y: new_y
                    };

                } else {
                    new_ctr.x += offset.x,
                        new_ctr.y += offset.y;
                }

                //width.val(offset.x)
                //height.val(offset.y)

                if(center) {
                    // Go to top-left for larger documents
                    if(svgCanvas.contentW > w_area.width()) {
                        // Top-left
                        w_area[0].scrollLeft = offset.x - 10;
                        w_area[0].scrollTop = offset.y - 10;
                    } else {
                        // Center
                        w_area[0].scrollLeft = scroll_x;
                        w_area[0].scrollTop = scroll_y;
                    }
                } else {
                    w_area[0].scrollLeft = new_ctr.x - w_orig/2;
                    w_area[0].scrollTop = new_ctr.y - h_orig/2;
                }
                if(curConfig.showRulers) {
                    updateRulers(cnvs, zoom);
                    workarea.scroll();
                }
            }

            var updateWireFrame = function() {
                // Test support
                //if(supportsNonSS) return;

                var rule = "#workarea.wireframe #svgcontent * { stroke-width: " + 1/svgCanvas.getZoom() + "px; }";
                $('#wireframe_rules').text(workarea.hasClass('wireframe') ? rule : "");
            }

            var zoomDone = function() {
                //    updateBgImage();
                updateWireFrame();
                //updateCanvas(); // necessary?
            }

            // Make [1,2,5] array
            var r_intervals = [];
            for(var i = .1; i < 1E5; i *= 10) {
                r_intervals.push(1 * i);
                r_intervals.push(2 * i);
                r_intervals.push(5 * i);
            }

            function updateRulers(scanvas, zoom) {
                var workarea = document.getElementById("workarea");
                var title_show = document.getElementById("title_show");
                var offset_x = 66;
                var offset_y = 48;
                if(!zoom) zoom = svgCanvas.getZoom();
                if(!scanvas) scanvas = $("#svgcanvas");

                var limit = 30000;

                //var c_elem = svgCanvas.getContentElem();

                //var units = svgedit.units.getTypeMap();
                var unit = 1;//units[curConfig.baseUnit]; // 1 = 1px

                for(var d = 0; d < 2; d++) {
                    var is_x = (d === 0);
                    var dim = is_x ? 'x' : 'y';
                    var lentype = is_x?'width':'height';
                    // TODO:
                    //var content_d = c_elem.getAttribute(dim)-0;
                    var content_d = 150;//0.0;

                    var $hcanv_orig = $('#ruler_' + dim + ' canvas:first');

                    // Bit of a hack to fully clear the canvas in Safari & IE9
                    $hcanv = $hcanv_orig.clone();
                    $hcanv_orig.replaceWith($hcanv);

                    var hcanv = $hcanv[0];

                    // Set the canvas size to the width of the container
                    var ruler_len = scanvas[lentype]()*2;
                    var total_len = ruler_len;
                    hcanv.parentNode.style[lentype] = total_len + 'px';
                    console.log("total_len: " +total_len);
                    var canv_count = 1;
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

                    var u_multi = unit * zoom;

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

                    var real_interval = multi * u_multi;
                    ctx.font = "normal 9px 'Lucida Grande', sans-serif";
                    ctx.fillStyle = "#777";

                    var ruler_d = ((content_d / u_multi) % multi) * u_multi;
                    console.log("ruler_d: " + ruler_d + ", content_d: "+content_d+", u_multi: "+u_multi+", multi: "+multi);
                    var label_pos = ruler_d - real_interval;
                    for (; ruler_d < total_len; ruler_d += real_interval) {
                        label_pos += real_interval;
                        var real_d = ruler_d - content_d;

                        var cur_d = Math.round(ruler_d) + .5;
                        if(is_x) {
                            ctx.moveTo(cur_d, 15);
                            ctx.lineTo(cur_d, 0);
                        } else {
                            ctx.moveTo(15, cur_d);
                            ctx.lineTo(0, cur_d);
                        }

                        var num = (label_pos - content_d) / u_multi;
                        var label;
                        if(multi >= 1) {
                            label = Math.round(num);
                        } else {
                            var decs = (multi+'').split('.')[1].length;
                            label = num.toFixed(decs)-0;
                        }

                        // Do anything special for negative numbers?
//            var is_neg = label < 0;
//            real_d2 = Math.abs(real_d2);

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

                        var part = real_interval / 10;
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
            }

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


                // 鼠标位置信息显示
                $(".tcs-editor").on('mousemove', function(e){
                    e.preventDefault();

                    var x = e.pageX;
                    var y = e.pageY;
                    var x2 = e.clientX;
                    var y2 = e.clientY;
                    $(".left-container .operate-content .tcs-bottom .show-position").html("X:"+x+", Y:"+y+"CX: "+x2+", CY: "+y2);
                })
            }());

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