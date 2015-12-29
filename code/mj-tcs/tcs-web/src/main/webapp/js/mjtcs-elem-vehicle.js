// Dependencies:
// 1) jquery.js
// 2) two.js
// 3)mjtcs-elem.js

Vehicle = function(x,y,name,two){
    var elemVehicle = this;
    Elem.call(elemVehicle);

    var XY = elemVehicle.initXY(x,y,0,0);
    x = XY.x1;
    y = XY.y1;

    var vehicleOrigin = two.makePath(x-elemVehicle.POINT_RADIUS*4,y-elemVehicle.POINT_RADIUS*2,x+elemVehicle.POINT_RADIUS*2,y-elemVehicle.POINT_RADIUS*2,x+elemVehicle.POINT_RADIUS*4,y,x+elemVehicle.POINT_RADIUS*2,y+elemVehicle.POINT_RADIUS*2,x-elemVehicle.POINT_RADIUS*4,y+elemVehicle.POINT_RADIUS*2,true);
    vehicleOrigin.fill = "orangered";
    vehicleOrigin.closed = true;
    vehicleOrigin.opacity = 0.75;
    elemVehicle.vehicleOrigin = vehicleOrigin;

    //标题
    var textTitle =  name.slice(-4);
    var styles={
        size:10,
        alignment:'center'
    };

    var text = two.makeText(textTitle,x,y,styles);
    //elemVehicle.selectedText = false;
    elemVehicle.textOffsetX = x - elemVehicle.vehicleOrigin.translation.x;
    elemVehicle.textOffsetY = y - elemVehicle.vehicleOrigin.translation.x;

    elemVehicle.text = text;

    elemVehicle.vehicle = two.makeGroup(elemVehicle.vehicleOrigin,elemVehicle.text);

    elemVehicle.two = two;

    //two.update();
};