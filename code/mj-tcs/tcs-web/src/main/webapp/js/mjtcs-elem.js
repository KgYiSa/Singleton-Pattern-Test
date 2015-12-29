// Dependencies:
//
//
//

Elem = function(){
    //todo: polish magic number from tcs-canvas.js'code :var axisOffset = tcsCanvas.editorCoordinatesToMouseCoordinates([0, 0]);
    var axisOffsetCopy = [52, 402];

    //this.ZOOM = 1;
    this.boardOffsetX = 0;
    this.boardOffsetY = 0;
    this.POINT_RADIUS = 5;
    this.LOCATION_RADIUS = 15;

    //将坐标转化成对应的网格坐标 网格边长为POINT_RADIUS*2
    this.initXY = function(x1,y1,x2,y2){
        x1 = this.POINT_RADIUS * 2 * (parseInt((x1 - axisOffsetCopy[0] - this.boardOffsetX) / (this.POINT_RADIUS*2)) + 0.5);
        y1 = this.POINT_RADIUS * 2 * (parseInt((y1 - axisOffsetCopy[1] - this.boardOffsetY) / (this.POINT_RADIUS*2)) + 0.5);
        x2 = this.POINT_RADIUS * 2 * (parseInt((x2 - axisOffsetCopy[0] - this.boardOffsetX) / (this.POINT_RADIUS*2)) + 0.5);
        y2 = this.POINT_RADIUS * 2 * (parseInt((y2 - axisOffsetCopy[1] - this.boardOffsetY) / (this.POINT_RADIUS*2)) + 0.5);
        return {x1:x1, y1:y1, x2:x2, y2:y2};
    }
};