/**
 * Created by Administrator on 2015/12/16.
 */
var LOCATION_RADIUS = 15,
    POINT_RADIUS = 5 ;
function Link(x1,y1,x2,y2,two){
    x1 = POINT_RADIUS * 2 * (parseInt((x1 - boardOffsetX) / (POINT_RADIUS*2)) + 0.5);
    y1 = POINT_RADIUS * 2 * (parseInt((y1 - boardOffsetX) / (POINT_RADIUS*2)) + 0.5);
    x2 = POINT_RADIUS * 2 * (parseInt((x2 - boardOffsetX) / (POINT_RADIUS*2)) + 0.5);
    y2 = POINT_RADIUS * 2 * (parseInt((y2 - boardOffsetX) / (POINT_RADIUS*2)) + 0.5);
    var atan2Angle = angle({x:x1,y:y1},{x:x2,y:y2});
    x1 = x1 + POINT_RADIUS*Math.cos(atan2Angle);
    y1 = y1 + POINT_RADIUS*Math.sin(atan2Angle);
    x2 = x2 - LOCATION_RADIUS*Math.cos(atan2Angle);
    y2 = y2 - LOCATION_RADIUS*Math.sin(atan2Angle);

    var hypotenuse = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));

    for(var i=0; i<=(hypotenuse-1)/POINT_RADIUS; i++){
        var xx1 = x1 + i*POINT_RADIUS*Math.cos(atan2Angle);
        var yy1 = y1 + i*POINT_RADIUS*Math.sin(atan2Angle);
        var xx2 = x1 + (i+1)*POINT_RADIUS*Math.cos(atan2Angle);
        var yy2 = y1 + (i+1)*POINT_RADIUS*Math.sin(atan2Angle);

        var link = two.makePath(xx1,yy1,xx2,yy2,true);
        link.linewidth = 1;
        link.opacity = (i%2 ==0?0:1);
    }


    this.two = two;

    two.update();
}

Link.prototype = {
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