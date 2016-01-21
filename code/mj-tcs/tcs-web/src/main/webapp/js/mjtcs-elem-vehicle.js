// Dependencies:
// 1) jquery.js
// 2) two.js
// 3)mjtcs-elem.js

Vehicle = function(x,y,name,initialPointUUID,two){
    var elemVehicle = this;
    elemVehicle.elem = "Vehicle";
    Elem.call(elemVehicle);

    var pointsArray = [];
    pointsArray.push(x);
    pointsArray.push(y);
    pointsArray = elemVehicle.initXY(pointsArray);
    x = pointsArray[0];
    y = pointsArray[1];

    var vehicleRaw = two.makePath(x-elemVehicle.POINT_RADIUS*4,y-elemVehicle.POINT_RADIUS*2,x+elemVehicle.POINT_RADIUS*2,y-elemVehicle.POINT_RADIUS*2,x+elemVehicle.POINT_RADIUS*4,y,x+elemVehicle.POINT_RADIUS*2,y+elemVehicle.POINT_RADIUS*2,x-elemVehicle.POINT_RADIUS*4,y+elemVehicle.POINT_RADIUS*2,true);
    vehicleRaw.fill = "orangered";
    vehicleRaw.closed = true;
    vehicleRaw.opacity = 0;
    elemVehicle.vehicleRaw = vehicleRaw;

    //标题
    var textTitle =  name.slice(-3);
    var styles={
        size:20*elemVehicle.ZOOM,
        alignment:'center'
    };

    var text = two.makeText(textTitle,x,y,styles);
    text.opacity = 0;
    //elemVehicle.selectedText = false;
    elemVehicle.textOffsetX = x - elemVehicle.vehicleRaw.translation.x;
    elemVehicle.textOffsetY = y - elemVehicle.vehicleRaw.translation.x;

    elemVehicle.text = text;

    //高亮样式
    var circle =  two.makeCircle(x,y,elemVehicle.POINT_RADIUS*7);
    circle.noFill();
    circle.opacity = 0;
    circle.stroke = 'orangered';
    circle.linewidth = 3;
    elemVehicle.circle = circle;

    elemVehicle.group = two.makeGroup(elemVehicle.vehicleRaw,elemVehicle.text,elemVehicle.circle);

    elemVehicle.initialPointUUID = initialPointUUID;
    elemVehicle.currentPointUUID = initialPointUUID;

    elemVehicle.setVehiclePosition = function(x,y,currentPointUUID){
        if(!currentPointUUID){
            elemVehicle.vehicleRaw.opacity = elemVehicle.text.opacity = 0;
            return;
        }
        elemVehicle.vehicleRaw.opacity = 0.75;
        elemVehicle.text.opacity = 0.75;
        //elemVehicle.group.translation.set(x,y);
        elemVehicle.vehicleRaw.translation.set(x,y);
        elemVehicle.text.translation.set(x,y);
        elemVehicle.circle.translation.set(x,y);
        elemVehicle.currentPointUUID = currentPointUUID;
    };
    elemVehicle.setVehicleDirection = function(start,end){
        elemVehicle.vehicleRaw.rotation = angle(start,end);
    };
    elemVehicle.setTextOpacity = function(){
        elemVehicle.text.opacity = elemVehicle.text.opacity==0? 1:0;
    };
    elemVehicle.setHighlight= function(val){
        elemVehicle.circle.opacity = val ? (elemVehicle.vehicleRaw.opacity==0 ? 0:1) : 0;
    };

    var angle = function(start,end){
        var diff_x = end.x - start.x,
            diff_y = end.y - start.y;
        return Math.atan2(diff_y,diff_x);
    };

};