// Dependencies:
// 1) jquery.js
// 2) two.js
// 3)mjtcs-elem.js

Vehicle = function(x,y,name,initialPointUUID,two){
    var elemVehicle = this;
    Elem.call(elemVehicle);

    var pointsArray = [];
    pointsArray.push(x);
    pointsArray.push(y);
    pointsArray = elemVehicle.initXY(pointsArray);
    x = pointsArray[0];
    y = pointsArray[1];

    var vehicleOrigin = two.makePath(x-elemVehicle.POINT_RADIUS*4,y-elemVehicle.POINT_RADIUS*2,x+elemVehicle.POINT_RADIUS*2,y-elemVehicle.POINT_RADIUS*2,x+elemVehicle.POINT_RADIUS*4,y,x+elemVehicle.POINT_RADIUS*2,y+elemVehicle.POINT_RADIUS*2,x-elemVehicle.POINT_RADIUS*4,y+elemVehicle.POINT_RADIUS*2,true);
    vehicleOrigin.fill = "orangered";
    vehicleOrigin.closed = true;
    vehicleOrigin.opacity = 0;
    elemVehicle.vehicleOrigin = vehicleOrigin;

    //标题
    var textTitle =  name.slice(-4);
    var styles={
        size:20*elemVehicle.ZOOM,
        alignment:'center'
    };

    var text = two.makeText(textTitle,x,y,styles);
    text.opacity = 0;
    //elemVehicle.selectedText = false;
    elemVehicle.textOffsetX = x - elemVehicle.vehicleOrigin.translation.x;
    elemVehicle.textOffsetY = y - elemVehicle.vehicleOrigin.translation.x;

    elemVehicle.text = text;

    elemVehicle.group = two.makeGroup(elemVehicle.vehicleOrigin,elemVehicle.text);

    elemVehicle.initialPointUUID = initialPointUUID;
    elemVehicle.currentPointUUID = initialPointUUID;

    elemVehicle.setVehiclePosition = function(x,y,currentPointUUID){
        elemVehicle.vehicleOrigin.opacity = 0.75;
        elemVehicle.text.opacity = 0.75;
        elemVehicle.vehicleOrigin.translation.set(x,y);
        elemVehicle.text.translation.set(x,y);
        elemVehicle.currentPointUUID = currentPointUUID;
    };

    elemVehicle.setVehicleDirection = function(start,end){
        elemVehicle.vehicleOrigin.rotation = angle(start,end);
    };

    var angle = function(start,end){
        var diff_x = end.x - start.x,
            diff_y = end.y - start.y;
        return Math.atan2(diff_y,diff_x);
    };

};