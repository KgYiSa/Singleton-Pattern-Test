// Dependencies:
//
//
//

Elem = function(){
    //todo: polish magic number from tcs-canvas.js'code :var axisOffset = tcsCanvas.editorCoordinatesToMouseCoordinates([0, 0]);
    var axisOffsetCopy = [0, 0];

    var elem = this;

    elem.ZOOM = 1;
    elem.boardOffsetX = 0;
    elem.boardOffsetY = 0;
    elem.POINT_RADIUS = 10*elem.ZOOM;
    elem.LOCATION_RADIUS = 30*elem.ZOOM;

    //将坐标转化成对应的网格坐标 网格边长为POINT_RADIUS*2
    elem.initXY = function(x1,y1,x2,y2){

        elem.display_position_x = x1;
        elem.display_position_y = y1;

        x1 = elem.POINT_RADIUS * 2 * (parseInt((x1 - axisOffsetCopy[0] - elem.boardOffsetX) / (elem.POINT_RADIUS*2)) + 0.5);
        y1 = elem.POINT_RADIUS * 2 * (parseInt((y1 - axisOffsetCopy[1] - elem.boardOffsetY) / (elem.POINT_RADIUS*2)) + 0.5);
        x2 = elem.POINT_RADIUS * 2 * (parseInt((x2 - axisOffsetCopy[0] - elem.boardOffsetX) / (elem.POINT_RADIUS*2)) + 0.5);
        y2 = elem.POINT_RADIUS * 2 * (parseInt((y2 - axisOffsetCopy[1] - elem.boardOffsetY) / (elem.POINT_RADIUS*2)) + 0.5);

        return {x1:x1, y1:y1, x2:x2, y2:y2};
    }
};