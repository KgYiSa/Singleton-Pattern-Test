// Dependencies:
// 1) jquery.js
// 2) two.js
// 3)mjtcs-elem.js

Location = function (x,y,type,name,textOffsetXX,textOffsetYY,two){
    var elemLocation = this;
    Elem.call(elemLocation);

    var XY = elemLocation.initXY(x,y,0,0);
    x = XY.x1;
    y = XY.y1;


//var gridx,
//    gridy,
//    selectedText,
//    mouseoverObject,
//    selectedPoint;


    var LOCATION_DIAMETER = 60*elemLocation.ZOOM;

    var locationOrigin =  two.makeRectangle(x,y,LOCATION_DIAMETER,LOCATION_DIAMETER);
    locationOrigin.stroke = 'black';
    locationOrigin.linewidth = 1*elemLocation.ZOOM;
    elemLocation.locationOrigin = locationOrigin;

    //编辑样式
    var rect1 = two.makeRectangle(x+LOCATION_DIAMETER,y+LOCATION_DIAMETER,5,5);
    rect1.opacity = 0;
    rect1.stroke = 'black';
    rect1.linewidth = 1;
    elemLocation.rect1 = rect1;
    var rect2 = two.makeRectangle(x+LOCATION_DIAMETER,y-LOCATION_DIAMETER,5,5);
    rect2.opacity = 0;
    rect2.stroke = 'black';
    rect2.linewidth = 1;
    elemLocation.rect2 = rect2;
    var rect3 = two.makeRectangle(x-LOCATION_DIAMETER,y+LOCATION_DIAMETER,5,5);
    rect3.opacity = 0;
    rect3.stroke = 'black';
    rect3.linewidth = 1;
    elemLocation.rect3 = rect3;
    var rect4 = two.makeRectangle(x-LOCATION_DIAMETER,y-LOCATION_DIAMETER,5,5);
    rect4.opacity = 0;
    rect4.stroke = 'black';
    rect4.linewidth = 1;
    elemLocation.rect4 = rect4;

    //this.selectedPoint = false;
    //标题
    var textTitle;
    var styles={
        size:10*elemLocation.ZOOM,
        linewidth:1,
        alignment:'center'
    };
    textTitle =  name;
    var text = two.makeText(textTitle,x-textOffsetXX,y-textOffsetYY,styles);
    //this.selectedText = false;
    //设置当前标题偏移量
    elemLocation.textOffsetX = x-textOffsetXX - elemLocation.locationOrigin.translation.x;
    elemLocation.textOffsetY = y-textOffsetYY - elemLocation.locationOrigin.translation.x;

    elemLocation.text = text;

    elemLocation.group = two.makeGroup(elemLocation.locationOrigin
        ,elemLocation.circle,elemLocation.rect1,elemLocation.rect2,elemLocation.rect3,elemLocation.rect4,elemLocation.text);

    //textTitleNumber++;
};
//
//Location.prototype = {
//    setPointDefault: function(){
//        this.pointOrigin.fill = this.typeColor;
//        this.rect1.opacity = 0;
//        this.rect2.opacity = 0;
//        this.rect3.opacity = 0;
//        this.rect4.opacity = 0;
//        this.circle.opacity = 0;
//        this.text.fill = 'black';
//        this.two.update();
//    },
//    //元素上发生鼠标点击时触发
//    onclickStyle: function(){
//        this.pointOrigin.fill = 'red';
//        this.two.update();
//    },
//    //元素上发生鼠标双击时触发
//    ondblclickStyle: function(){
//        this.pointOrigin.fill = 'red';
//        this.rect1.opacity = 0.3;
//        this.pointOrigin.fill = 'red';
//        this.rect2.opacity = 0.3;
//        this.pointOrigin.fill = 'red';
//        this.rect3.opacity = 0.3;
//        this.pointOrigin.fill = 'red';
//        this.rect4.opacity = 0.3;
//        this.text.fill = 'red';
//        this.two.update();
//    },
//    //元素被拖动时运行的脚本
//    ondragStyle: function(){
//        this.pointOrigin.fill = 'red';
//        this.circle.opacity = 0.5;
//        this.two.update();
//    },
//    //在拖动操作末端运行的脚本
//    ondragendStyle: function(){
//
//    },
//    //当元素元素已被拖动到有效拖放区域时运行的脚本
//    ondragenterStyle: function(){
//
//    },
//    //当元素离开有效拖放目标时运行的脚本
//    ondragleaveStyle: function(){
//
//    },
//    //当元素在有效拖放目标上正在被拖动时运行的脚本
//    ondragoverStyle: function(){
//
//    },
//    //在拖动操作开端运行的脚本
//    ondragstartStyle: function(){
//
//    },
//    //当被拖元素正在被拖放时运行的脚本
//    ondropStyle: function(){
//
//    },
//    //当元素上按下鼠标按钮时触发
//    onmousedownStyle: function(){
//
//    },
//    //当鼠标指针移动到元素上时触发
//    onmousemoveStyle: function(){
//
//    },
//    //当鼠标指针移出元素时触发
//    onmouseoutStyle: function(){
//        this.pointOrigin.fill = this.typeColor;
//        this.rect1.opacity = 0;
//        this.rect2.opacity = 0;
//        this.rect3.opacity = 0;
//        this.rect4.opacity = 0;
//        this.circle.opacity = 0;
//        this.two.update();
//    },
//    //当鼠标指针移动到元素上时触发
//    onmouseoverStyle: function(){
//        this.pointOrigin.fill = "red";
//        this.two.update();
//    },
//    //当在元素上释放鼠标按钮时触发
//    onmouseupStyle: function(){
//
//    },
//    //当鼠标滚轮正在被滚动时运行的脚本
//    onmousewheelStyle: function(){
//
//    },
//    //当元素滚动条被滚动时运行的脚本
//    onscrollStyle: function(){
//
//    },
//
//    selectPoint: function () {
//        this.selectedPoint = true;
//        this.pointOrigin.fill = 'red';
//        this.selectText();
//    },
//
//    unSelectPoint: function () {
//        this.selectedPoint = false;
//        this.pointOrigin.fill = this.typeColor;
//        this.unSelectText();
//    },
//
//    movePoint: function (x,y) {
//        this.point.translation.set(x, y);
//        this.text.translation.set(x + this.textOffsetX, y + this.textOffsetY);
//        two.update();
//    },
//
//
//    removePoint: function () {
//        this.point.remove();
//        this.text.parent.remove(this.text);
//    },
//
//    isCheckText: function (x, y) {
//        var top = this.point.translation.y + this.textOffsetY - POINT_RADIUS,
//            bottom = this.point.translation.y + this.textOffsetY + POINT_RADIUS,
//            left =  this.point.translation.x + this.textOffsetX - POINT_RADIUS * 3,
//            right =  this.point.translation.x + this.textOffsetX + POINT_RADIUS * 3;
//
//        return (y >= top && y <= bottom && x >= left && x <= right);
//    },
//
//    selectText: function () {
//        this.selectedText = true;
//        this.text.fill = 'red';
//    },
//
//    unSelectText: function () {
//        this.selectedText = false;
//        this.text.fill = 'black';
//    },
//
//    moveText: function (x,y) {
//        this.textOffsetX = x - this.point.translation.x;
//        this.textOffsetY = y - this.point.translation.y;
//        this.text.translation.set(x, y);
//        two.update();
//    }
//
//};