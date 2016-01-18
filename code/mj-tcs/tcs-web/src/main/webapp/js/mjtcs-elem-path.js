// Dependencies:
// 1) jquery.js
// 2) two.js
// 3)mjtcs-elem.js

Path = function(x1,y1,x2,y2,positiveVal,negativeVal,control_points,two){
    var elemPath = this;
    Elem.call(elemPath);

    for(var p = 0; p < control_points.length-3;p+=2){
        if(control_points[p]==control_points[p+2] && control_points[p+1]==control_points[p+3]){
            control_points.splice(p,1);
            control_points.splice(p,1);
            p-=2;
        }
    }

    control_points.unshift(y1);
    control_points.unshift(x1);
    control_points.push(x2);
    control_points.push(y2);
    var pointArray = elemPath.initXY(control_points);

    var TRIANGLE_RADIUS = 7*elemPath.ZOOM;//PATH箭头半径

    var atan2Angle = angle({x: pointArray[0],y: pointArray[1]},{x: pointArray[pointArray.length-2],y: pointArray[pointArray.length-1]});
    //正/负向速度不为0时，存在正/负向箭头
    var positivePathArrowAdjust = positiveVal==0 ? 0:TRIANGLE_RADIUS;
    var negativePathArrowAdjust = negativeVal==0 ? 0:TRIANGLE_RADIUS;
    //根据正/负向箭头存在与否，调整线段长短，确定端点坐标
    pointArray[0] = x1 = pointArray[0] + (negativePathArrowAdjust+elemPath.POINT_RADIUS)*Math.cos(atan2Angle);
    pointArray[1] = y1 = pointArray[1] + (negativePathArrowAdjust+elemPath.POINT_RADIUS)*Math.sin(atan2Angle);
    pointArray[pointArray.length-2] = x2 = pointArray[pointArray.length-2] - (positivePathArrowAdjust+elemPath.POINT_RADIUS)*Math.cos(atan2Angle);
    pointArray[pointArray.length-1] = y2 = pointArray[pointArray.length-1] - (positivePathArrowAdjust+elemPath.POINT_RADIUS)*Math.sin(atan2Angle);

    var lineCode = "var line = two.makePath(" ;

    for(var p in pointArray){
        lineCode += (pointArray[p]+",");
    }
    eval(lineCode+"true)");

    line.linewidth = 1*elemPath.ZOOM;
    line.opacity = elemPath.lineOpacity;
    line.curved = true;
    line.noFill();
    elemPath.line = line;

    var positiveTriangle = two.makePolygon(x2,y2,TRIANGLE_RADIUS,3);
    positiveTriangle.fill = "black";
    positiveTriangle.opacity = positiveVal==0 ? 0:elemPath.lineOpacity;
    elemPath.positiveTriangle = positiveTriangle;

    var negativeTriangle = two.makePolygon(x1,y1,TRIANGLE_RADIUS,3);
    negativeTriangle.fill = "white";
    negativeTriangle.opacity = negativeVal==0 ? 0:elemPath.lineOpacity;
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