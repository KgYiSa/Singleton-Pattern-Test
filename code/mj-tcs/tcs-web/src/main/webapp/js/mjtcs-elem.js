// Dependencies:
//
//
//

Elem = function(){
    //todo: polish magic number from tcs-canvas.js'code :var axisOffset = tcsCanvas.editorCoordinatesToMouseCoordinates([0, 0]);
    var axisOffsetCopy = [0, 0];

    var elem = this;

    elem.ZOOM = 1;
    elem.lineOpacity = 0.75;//各种线条透明度
    elem.boardOffsetX = 0;
    elem.boardOffsetY = 0;
    elem.POINT_RADIUS = 5*elem.ZOOM;
    elem.LOCATION_RADIUS = 15*elem.ZOOM;

    //将坐标转化成对应的网格坐标 网格边长为POINT_RADIUS*2
    elem.initXY = function(pointsArray){
        elem.display_position_x = pointsArray[0];
        elem.display_position_y = pointsArray[1];
        for(var p in pointsArray) {
            pointsArray[p] = elem.POINT_RADIUS * 2 * ((parseInt(pointsArray[p] ) / (elem.POINT_RADIUS * 2)) + 0.5);
            //pointsArray[p] = elem.POINT_RADIUS * 2 * (parseInt((pointsArray[p] - (p % 2 == 0 ? (axisOffsetCopy[0] + elem.boardOffsetX) : (axisOffsetCopy[1] - elem.boardOffsetY))) / (elem.POINT_RADIUS * 2)) + 0.5);
        }
        return pointsArray;
    }
};