// Dependencies:
// 1) jquery.js
// 2) two.js
// 3)mjtcs-elem.js

Link = function (x1,y1,x2,y2,two) {
    var elemLink = this;
    Elem.call(elemLink);

    var pointsArray = [];
    pointsArray.push(x1);
    pointsArray.push(y1);
    pointsArray.push(x2);
    pointsArray.push(y2);
    pointsArray = elemLink.initXY(pointsArray);


    var atan2Angle = angle({x: pointsArray[0], y: pointsArray[1]}, {x: pointsArray[2], y: pointsArray[3]});
    //两端减去POINT和LOCATION半径后的坐标
    x1 = pointsArray[0] + elemLink.POINT_RADIUS * Math.cos(atan2Angle);
    y1 = pointsArray[1] + elemLink.POINT_RADIUS * Math.sin(atan2Angle);
    x2 = pointsArray[2] - elemLink.LOCATION_RADIUS * Math.cos(atan2Angle);
    y2 = pointsArray[3] - elemLink.LOCATION_RADIUS * Math.sin(atan2Angle);

    //斜长
    var hypotenuse = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

    var link = two.makeGroup();
    //以POINT_RADIUS长度划线段
    for (var i = 0; i <= (hypotenuse - 2) / elemLink.POINT_RADIUS; i+=2) {
        var xx1 = x1 + i * elemLink.POINT_RADIUS * Math.cos(atan2Angle);
        var yy1 = y1 + i * elemLink.POINT_RADIUS * Math.sin(atan2Angle);
        var xx2 = x1 + (i + 1) * elemLink.POINT_RADIUS * Math.cos(atan2Angle);
        var yy2 = y1 + (i + 1) * elemLink.POINT_RADIUS * Math.sin(atan2Angle);

        var dotted = two.makePath(xx1, yy1, xx2, yy2, true);
        dotted.linewidth = 1*elemLink.ZOOM;
        dotted.stroke = "orangered";
        dotted.opacity = elemLink.lineOpacity;
        link.add(dotted);
    }
    elemLink.group = link;


    function angle(start, end) {
        var diff_x = end.x - start.x,
            diff_y = end.y - start.y;
        return Math.atan2(diff_y, diff_x);
    }

};