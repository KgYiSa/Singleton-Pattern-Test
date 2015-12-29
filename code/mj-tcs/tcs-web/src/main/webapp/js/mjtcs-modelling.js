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