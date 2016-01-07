// Dependencies:
// 1) jquery.js
// 2) two.js
// 3)mjtcs-elem.js

Path = function(x1,y1,x2,y2,positiveVal,negativeVal,two){
    var elemPath = this;
    Elem.call(elemPath);

    var XY = elemPath.initXY(x1,y1,x2,y2);

    var TRIANGLE_RADIUS = 7;//PATH箭头半径

    var atan2Angle = angle({x: XY.x1,y: XY.y1},{x: XY.x2,y: XY.y2});
    //正/负向速度不为0时，存在正/负向箭头
    var positivePathArrowAdjust = positiveVal==0 ? 0:TRIANGLE_RADIUS;
    var negativePathArrowAdjust = negativeVal==0 ? 0:TRIANGLE_RADIUS;
    //根据正/负向箭头存在与否，调整线段长短，确定端点坐标
    x1 = XY.x1 + (negativePathArrowAdjust+elemPath.POINT_RADIUS)*Math.cos(atan2Angle);
    y1 = XY.y1 + (negativePathArrowAdjust+elemPath.POINT_RADIUS)*Math.sin(atan2Angle);
    x2 = XY.x2 - (positivePathArrowAdjust+elemPath.POINT_RADIUS)*Math.cos(atan2Angle);
    y2 = XY.y2 - (positivePathArrowAdjust+elemPath.POINT_RADIUS)*Math.sin(atan2Angle);

    var line = two.makePath(x1,y1,x2,y2,true);

    line.linewidth = 1;
    elemPath.line = line;

    var positiveTriangle = two.makePolygon(x2,y2,TRIANGLE_RADIUS,3);
    positiveTriangle.fill = "black";
    positiveTriangle.opacity = positiveVal==0 ? 0:1;
    elemPath.positiveTriangle = positiveTriangle;

    var negativeTriangle = two.makePolygon(x1,y1,TRIANGLE_RADIUS,3);
    negativeTriangle.fill = "white";
    negativeTriangle.opacity = negativeVal==0 ? 0:1;
    elemPath.negativeTriangle = negativeTriangle;

    elemPath.group = two.makeGroup(elemPath.line,elemPath.positiveTriangle,elemPath.negativeTriangle);

    //设置正/负向箭头角度
    setPositiveTriangle({x:x1,y:y1},{x:x2,y:y2});
    setNegativeTriangle({x:x1,y:y1},{x:x2,y:y2});


    function angle(start,end){
        var diff_x = end.x - start.x,
            diff_y = end.y - start.y;
        return Math.atan2(diff_y,diff_x);
    }
    function setPositiveTriangle(start,end) {
        elemPath.positiveTriangle.translation.set(end.x,end.y);
        elemPath.positiveTriangle.rotation = angle(start,end) + Math.PI/2;
    }
    function setNegativeTriangle(start,end) {
        elemPath.negativeTriangle.translation.set(start.x,start.y);
        elemPath.negativeTriangle.rotation = angle(start,end) - Math.PI/2;
    }

};