/**
 * Created by Shuai.Wang on 2015/12/7.
 */

//var selectedObject;//存放被选中的对象
//var mouseoverObject;//存放鼠标滑过的对象
var gridlist = [];//存放网格对应的点
var pointList = [];

var svgcanvas=document.querySelector('#svgcanvas');
var lineh=[];
var linew=[];
//var h=$(window).height();
//var w=$(window).width();
var gridlength = 10;
var params = { width: gridlength*200, height: gridlength*200,type: Two.Types.webgl};
var two = new Two(params).appendTo(svgcanvas);
//var gridlist = [];
for(var i=0;i<200;i ++){
    gridlist[i]=new Array;
    for(var j=0;j<200;j++){
        gridlist[i][j]='';
    }
}

for(var i=0;i<200;i++){
    var t=two.makeLine(gridlength*i, 0, gridlength*i, gridlength*200);
    t.stroke = 'black';
    t.opacity = 0.3;
    lineh.push(t);
}
for(var j=0;j<200;j++){
    var t=two.makeLine( 0, gridlength*j, gridlength*200,gridlength*j);
    t.stroke = 'black';
    t.opacity = 0.3;
    linew.push(t);
}
two.update();



$('ul#mouse-newpoint li').click(function(e){
    e.preventDefault();
    //$(window).unbind('mousedown');
    createPoint(this.value);
});
$('#mouse-newpoint').mouseover(function(e){
    e.preventDefault();
    $('#svgcanvas').css({
        cursor: 'default'
    });
    $(window).unbind('mousedown')
        .bind('mousedown', function (e) {
            windowPointMousedown(e);
        })
        //鼠标松开
        .bind('mouseup', function (e) {
            windowPointMouseup(e);
        })
        //鼠标移动
        .bind('mousemove', function (e) {
            windowPointMousemove(e);
        });
});
var pointMousedown,pointMouseup,pointMousemove;

$('#mouse-default').mouseover(function(e){
    e.preventDefault();
    $('#svgcanvas').css({
        cursor: 'default'
    });
    $(window).unbind('mousedown')
        .bind('mousedown', pointMousedown = function (e) {
            windowPointMousedown(e);
        })
        //鼠标松开
        .bind('mouseup', pointMouseup = function (e) {
            windowPointMouseup(e);
        })
        //鼠标移动
        .bind('mousemove', pointMousemove = function (e) {
            windowPointMousemove(e);
        });
});

function createPoint(type){
    $('#svgcanvas').css({
        cursor: 'crosshair'
    });
    $(window)
        .bind('mousedown',function(e) {
            e.preventDefault();
            var x = getCurrentXY(e,true).X;
            var y = getCurrentXY(e,true).Y;
            var point = new Point(x, y, type, two);
            gridlist[x][y] = point;
            pointList.push(point);
        })
}


$('ul#mouse-newpath li').click(function(e){
    e.preventDefault();
    //$(window).unbind('mousedown');
    createPath();
});


function createPath(){
    $('#svgcanvas').css({
        cursor: 'crosshair'
    });
    $(window)
        .unbind('mousedown')
        .unbind('mouseup')
        .unbind('mousemove')
        .bind('mousedown',function(e) {
            e.preventDefault();
            var x1 = getCurrentXY(e,false).X;
            var y1 = getCurrentXY(e,false).Y;
            var path = new Path(x1, y1, x1, y1, two);
            $(window)
                .bind('mousemove',function(e) {
                    e.preventDefault();
                    var x2 = getCurrentXY(e,false).X;
                    var y2 = getCurrentXY(e,false).Y;
                    //setVertices(path.line,x1,y1,x2,y2);
                    var width = x2 - x1;
                    var height = y2 - y1;

                    var w2 = width / 2;
                    var h2 = height / 2;

                    path.line.translation.set(x1 + w2, y1 + h2);
                    path.line.vertices[0].set(-w2,-h2);
                    path.line.vertices[1].set(w2,h2);
                    path.setPositiveTriangle({x:x1,y:y1},{x:x2,y:y2});
                    two.update();
                })
                .bind('mouseup',function(e){
                    $(window).unbind('mousemove') ;
                });

        })
}


$('#mouse-newlocation').click(function(e){
    e.preventDefault();
    //$(window).unbind('mousedown');
    createLocation();
});

function createLocation(){
    $('#svgcanvas').css({
        cursor: 'crosshair'
    });
    $(window)
        .unbind('mousedown')
        .unbind('mouseup')
        .unbind('mousemove')
        .bind('mousedown',function(e) {
            e.preventDefault();
            var x1 = getCurrentXY(e,true).X;
            var y1 = getCurrentXY(e,true).Y;
            var location = new Location(x1, y1, two);
            two.update;

        })
}



var jsondata =   {
                        "id": 5,
                        "version": 0,
                        "name": "test_scene_15_12_08_22_02_33_1",
                        "properties": [
                            {
                                "name": "key",
                                "value": "value"
                            }
                        ],
                        "UUID": "scenedto-b2796efe-ab5f-44b1-aae9-6e16fedf1ccc",
                        "points": [
                            {
                                "id": 35,
                                "version": 0,
                                "name": "test_point_0",
                                "properties": [],
                                "vehicle_orientation_angle": 0,
                                "type": "HALT_POSITION",
                                "attached_links": [
                                    {
                                        "id": 5,
                                        "name": "test_link_3785",
                                        "UUID": "locationlinkdto-94836d25-29fc-44a0-911d-86014dfac8ca"
                                    }
                                ],
                                "UUID": "pointdto-6949b047-97fa-4104-818b-56a5b5c4066e",
                                "position": {
                                    "x": 658,
                                    "y": 482,
                                    "z": 0
                                },
                                "display_position_x": 400,
                                "display_position_y": 200,
                                "label_offset_x": 0,
                                "label_offset_y": -20,
                                "incoming_paths": [],
                                "outgoing_paths": [
                                    {
                                        "id": 17,
                                        "name": "test_path_0",
                                        "UUID": "pathdto-b9066c29-ce55-4b79-b2ee-baa8999658ef"
                                    }
                                ]
                            },
                            {
                                "id": 34,
                                "version": 0,
                                "name": "test_point_1",
                                "properties": [],
                                "vehicle_orientation_angle": 0,
                                "type": "HALT_POSITION",
                                "attached_links": [],
                                "UUID": "pointdto-eacd255a-9bf2-4848-bc65-cdbac41352c5",
                                "position": {
                                    "x": 827,
                                    "y": 264,
                                    "z": 0
                                },
                                "display_position_x": 200,
                                "display_position_y": 300,
                                "label_offset_x": 0,
                                "label_offset_y": 20,
                                "incoming_paths": [
                                    {
                                        "id": 17,
                                        "name": "test_path_0",
                                        "UUID": "pathdto-b9066c29-ce55-4b79-b2ee-baa8999658ef"
                                    }
                                ],
                                "outgoing_paths": []
                            },
                            {
                                "id": 36,
                                "version": 0,
                                "name": "test_point_2",
                                "properties": [],
                                "vehicle_orientation_angle": 0,
                                "type": "HALT_POSITION",
                                "attached_links": [],
                                "UUID": "pointdto-9fae4650-88fd-4731-bc0a-c4e29c2cc2fe",
                                "position": {
                                    "x": 346,
                                    "y": 61,
                                    "z": 0
                                },
                                "display_position_x": 600,
                                "display_position_y": 500,
                                "label_offset_x": 10,
                                "label_offset_y": 20,
                                "incoming_paths": [],
                                "outgoing_paths": []
                            }
                        ],
                        "paths": [
                            {
                                "id": 17,
                                "version": 0,
                                "name": "test_path_0",
                                "properties": [],
                                "control_points": [],
                                "length": 426,
                                "routing_cost": 1,
                                "max_velocity": 1,
                                "max_reverse_velocity": 1,
                                "locked": false,
                                "UUID": "pathdto-b9066c29-ce55-4b79-b2ee-baa8999658ef",
                                "source_point": {
                                    "id": 35,
                                    "name": "test_point_0",
                                    //"UUID": "pointdto-6949b047-97fa-4104-818b-56a5b5c4066e"
                                    //"UUID": "pointdto-eacd255a-9bf2-4848-bc65-cdbac41352c5"
                                    "UUID": "pointdto-9fae4650-88fd-4731-bc0a-c4e29c2cc2fe"
                                },
                                "destination_point": {
                                    "id": 34,
                                    "name": "test_point_1",
                                    "UUID": "pointdto-6949b047-97fa-4104-818b-56a5b5c4066e"
                                    //"UUID": "pointdto-eacd255a-9bf2-4848-bc65-cdbac41352c5"
                                    //"UUID": "pointdto-9fae4650-88fd-4731-bc0a-c4e29c2cc2fe"
                                }
                            },
                            {
                                "id": 18,
                                "version": 0,
                                "name": "test_path_1",
                                "properties": [],
                                "control_points": [],
                                "length": 426,
                                "routing_cost": 1,
                                "max_velocity": 1,
                                "max_reverse_velocity": 1,
                                "locked": false,
                                "UUID": "pathdto-b9066c29-ce55-4b79-b2ee-baa8999658eg",
                                "source_point": {
                                    "id": 35,
                                    "name": "test_point_0",
                                    "UUID": "pointdto-6949b047-97fa-4104-818b-56a5b5c4066e"
                                    //"UUID": "pointdto-eacd255a-9bf2-4848-bc65-cdbac41352c5"
                                    //"UUID": "pointdto-9fae4650-88fd-4731-bc0a-c4e29c2cc2fe"
                                },
                                "destination_point": {
                                    "id": 34,
                                    "name": "test_point_1",
                                    //"UUID": "pointdto-6949b047-97fa-4104-818b-56a5b5c4066e"
                                    "UUID": "pointdto-eacd255a-9bf2-4848-bc65-cdbac41352c5"
                                    //"UUID": "pointdto-9fae4650-88fd-4731-bc0a-c4e29c2cc2fe"
                                }
                            },
                            {
                                "id": 19,
                                "version": 0,
                                "name": "test_path_2",
                                "properties": [],
                                "control_points": [],
                                "length": 426,
                                "routing_cost": 1,
                                "max_velocity": 1,
                                "max_reverse_velocity": 1,
                                "locked": false,
                                "UUID": "pathdto-b9066c29-ce55-4b79-b2ee-baa8999658eh",
                                "source_point": {
                                    "id": 35,
                                    "name": "test_point_0",
                                    //"UUID": "pointdto-6949b047-97fa-4104-818b-56a5b5c4066e"
                                    "UUID": "pointdto-eacd255a-9bf2-4848-bc65-cdbac41352c5"
                                    //"UUID": "pointdto-9fae4650-88fd-4731-bc0a-c4e29c2cc2fe"
                                },
                                "destination_point": {
                                    "id": 34,
                                    "name": "test_point_1",
                                    //"UUID": "pointdto-6949b047-97fa-4104-818b-56a5b5c4066e"
                                    //"UUID": "pointdto-eacd255a-9bf2-4848-bc65-cdbac41352c5"
                                    "UUID": "pointdto-9fae4650-88fd-4731-bc0a-c4e29c2cc2fe"
                                }
                            }
                        ],
                        "locations": [
                            {
                                "id": 5,
                                "version": 0,
                                "name": "test_location_3785",
                                "properties": [],
                                "attached_links": [
                                    {
                                        "id": 5,
                                        "version": 0,
                                        "name": "test_link_3785",
                                        "allowed_operations": [],
                                        "UUID": "locationlinkdto-94836d25-29fc-44a0-911d-86014dfac8ca",
                                        "point": {
                                            "id": 35,
                                            "name": "test_point_0",
                                            "UUID": "pointdto-6949b047-97fa-4104-818b-56a5b5c4066e"
                                        }
                                    }
                                ],
                                "UUID": "locationdto-0206deb6-919c-4911-b1c2-5989655c2b30",
                                "position": {
                                    "x": 100,
                                    "y": 100,
                                    "z": 0
                                },
                                "location_type": {
                                    "id": 5,
                                    "name": "test_lt_174",
                                    "UUID": "locationtypedto-ec1c05be-153a-4e0b-9314-724c0ab03ae4"
                                },
                                "display_position_x": 600,
                                "display_position_y": 150,
                                "label_offset_x": 0,
                                "label_offset_y": 30
                            }
                        ],
                        "location_types": [
                            {
                                "id": 5,
                                "version": 0,
                                "name": "test_lt_174",
                                "properties": [],
                                "allowed_operations": [
                                    "Puts in storage",
                                    "Stock removal"
                                ],
                                "UUID": "locationtypedto-ec1c05be-153a-4e0b-9314-724c0ab03ae4"
                            }
                        ],
                        "blocks": [
                            {
                                "id": 5,
                                "version": 0,
                                "name": "test_block_2435",
                                "properties": [],
                                "members": [
                                    {
                                        "UUID": "pointdto-6949b047-97fa-4104-818b-56a5b5c4066e"
                                    },
                                    {
                                        "UUID": "pointdto-eacd255a-9bf2-4848-bc65-cdbac41352c5"
                                    },
                                    {
                                        "UUID": "pathdto-b9066c29-ce55-4b79-b2ee-baa8999658ef"
                                    },
                                    {
                                        "UUID": "locationdto-0206deb6-919c-4911-b1c2-5989655c2b30"
                                    }
                                ],
                                "UUID": "blockdto-37ea38b3-3b15-4b16-b5bb-d7742d806461"
                            }
                        ],
                        "static_routes": [
                            {
                                "id": 5,
                                "version": 0,
                                "name": "test_static_route_6979",
                                "properties": [],
                                "hops": [
                                    {
                                        "id": 35,
                                        "name": "test_point_0",
                                        "UUID": "pointdto-6949b047-97fa-4104-818b-56a5b5c4066e"
                                    },
                                    {
                                        "id": 34,
                                        "name": "test_point_1",
                                        "UUID": "pointdto-eacd255a-9bf2-4848-bc65-cdbac41352c5"
                                    },
                                    {
                                        "id": 36,
                                        "name": "test_point_2",
                                        "UUID": "pointdto-9fae4650-88fd-4731-bc0a-c4e29c2cc2fe"
                                    }
                                ],
                                "UUID": "staticroutedto-1711913e-fd02-4a8c-80e4-43255d7f7a26"
                            }
                        ],
                        "vehicles": [
                            {
                                "id": 5,
                                "version": 0,
                                "name": "test_vehicle_2403",
                                "properties": [],
                                "length": 1000,
                                "energy_level": 80,
                                "energy_level_critical": 30,
                                "energy_level_good": 80,
                                "recharge_operation": "RECHARGE",
                                "max_velocity": 100,
                                "max_reverse_velocity": 100,
                                "initial_point": null,
                                "orientation_angle": 0,
                                "UUID": "vehicledto-69cb85f1-9aea-4470-8ce2-c19edbef1c36",
                                "precise_position": null
                            }
                        ]
                    };
//buildSceneEditor(jsondata);
//alert(jsonObject.points[2].display_position_x);
function buildSceneEditor(jsonObject){

    for(var i= 0; i<jsonObject.points.length; i++){
        var jsonPoint = jsonObject.points[i];
        new Point(jsonPoint.display_position_x,jsonPoint.display_position_y,jsonPoint.type,jsonPoint.name,jsonPoint.label_offset_x,jsonPoint.label_offset_y,two);
    }
    for(var i= 0; i<jsonObject.locations.length; i++){
        var jsonLocation = jsonObject.locations[i];
        new Location(jsonLocation.display_position_x,jsonLocation.display_position_y,jsonLocation.type,jsonLocation.name,jsonLocation.label_offset_x,jsonLocation.label_offset_y,two);
        for(var j=0; j<jsonLocation.attached_links.length; j++){
            var startX,startY;
            for(var k=0; k<jsonObject.points.length; k++){
                if(jsonObject.points[k].UUID == jsonLocation.attached_links[j].point.UUID){
                    startX = jsonObject.points[k].display_position_x;
                    startY = jsonObject.points[k].display_position_y;
                    break;
                }
            }
            new Link(startX,startY,jsonLocation.display_position_x,jsonLocation.display_position_y,two);
        }
    }
    for(var i= 0; i<jsonObject.paths.length; i++){
        var jsonPath = jsonObject.paths[i];
        var startX,startY,endX,endY;
        var sourceUUID = jsonPath.source_point.UUID,
            destinationUUID = jsonPath.destination_point.UUID;
        for(var j=0; j<jsonObject.points.length; j++){
            startX = jsonObject.points[j].UUID == sourceUUID?jsonObject.points[j].display_position_x : startX ;
            startY = jsonObject.points[j].UUID == sourceUUID?jsonObject.points[j].display_position_y : startY ;
            endX = jsonObject.points[j].UUID == destinationUUID?jsonObject.points[j].display_position_x : endX ;
            endY = jsonObject.points[j].UUID == destinationUUID?jsonObject.points[j].display_position_y : endY ;
        }
        new Path(startX,startY,endX,endY,two);
    }
}
//$(".show-splits div").toggle(function(){
//        gridShowOrHideGrid(true);
//},function(){
//        gridShowOrHideGrid(false);
//});
//function gridShowOrHideGrid(val){
//    for(var p in lineh){
//        lineh[p].opacity = val?0.3:0;
//    }
//    for(var p in linew){
//        linew[p].opacity = val?0.3:0;
//    }
//}