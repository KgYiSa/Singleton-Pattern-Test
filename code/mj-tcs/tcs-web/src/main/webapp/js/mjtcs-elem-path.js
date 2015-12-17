/**
 * Created by Administrator on 2015/12/10.
 */
var PATH_RADIUS = 7,
    POINT_RADIUS = 5 ;
function Path(x1,y1,x2,y2,two){
    x1 = POINT_RADIUS * 2 * (parseInt((x1 - boardOffsetX) / (POINT_RADIUS*2)) + 0.5);
    y1 = POINT_RADIUS * 2 * (parseInt((y1 - boardOffsetX) / (POINT_RADIUS*2)) + 0.5);
    x2 = POINT_RADIUS * 2 * (parseInt((x2 - boardOffsetX) / (POINT_RADIUS*2)) + 0.5);
    y2 = POINT_RADIUS * 2 * (parseInt((y2 - boardOffsetX) / (POINT_RADIUS*2)) + 0.5);
    var atan2Angle = angle({x:x1,y:y1},{x:x2,y:y2});
    x1 = x1 + POINT_RADIUS*Math.cos(atan2Angle);
    y1 = y1 + POINT_RADIUS*Math.sin(atan2Angle);
    x2 = x2 - (PATH_RADIUS+POINT_RADIUS)*Math.cos(atan2Angle);
    y2 = y2 - (PATH_RADIUS+POINT_RADIUS)*Math.sin(atan2Angle);

    var line = two.makePath(x1,y1,x2,y2,true);
    line.linewidth = 1;
    this.line = line;

    var positiveTriangle = two.makePolygon(x2,y2,PATH_RADIUS,3);
    positiveTriangle.fill = "black";
    this.positiveTriangle = positiveTriangle;

    var negativeTriangle = two.makePolygon(x1,y1,PATH_RADIUS,3);
    negativeTriangle.fill = "white";
    negativeTriangle.opacity = 0;
    this.negativeTriangle = negativeTriangle;

    this.path = two.makeGroup(this.line,this.positiveTriangle,this.negativeTriangle);

    this.setPositiveTriangle({x:x1,y:y1},{x:x2,y:y2});
    this.two = two;

    two.update();
}

Path.prototype = {
    setPositiveTriangle: function(start,end) {
        this.positiveTriangle.translation.set(end.x,end.y);
        this.positiveTriangle.rotation = angle(start,end) + Math.PI/2;
    },
    setNegativeTriangle: function (start,end) {
        this.negativeTriangle.translation.set(start.x,start.y);
        this.negativeTriangle,rotation = angle(start,end) - Math.PI/2;
    }
}



function angle(start,end){
    var diff_x = end.x - start.x,
        diff_y = end.y - start.y;
    return Math.atan2(diff_y,diff_x);
}