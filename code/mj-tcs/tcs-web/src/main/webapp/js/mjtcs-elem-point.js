// Dependencies:
// 1) jquery.js
// 2) two.js
// 3)mjtcs-elem.js

Point = function(x,y,type,name,textOffsetX,textOffsetY,two){
    var elemPoint = this;
    elemPoint.elem = "Point";
    Elem.call(elemPoint);

    var pointsArray = [];
    pointsArray.push(x);
    pointsArray.push(y);
    pointsArray = elemPoint.initXY(pointsArray);
    x = pointsArray[0];
    y = pointsArray[1];

    //var textTitleNumber = 1;
    var textTitle;
    var styles={
        size:20*elemPoint.ZOOM,
        linewidth:2.0,
        alignment:'center'
    };

    //var gridx,
    //    gridy,
    //    selectedText,
    //    mouseoverObject,
    //    selectedPoint;
//console.log("x = " + x + ", y = " + y);
    var pointRaw =  two.makeCircle(x,y,elemPoint.POINT_RADIUS);
    //console.log(pointOrigin.getBoundingClientRect());
    var typeColor;
    if(type == "REPORT_POSITION"){//Report Point
        typeColor = 'white';
    }else if(type == "PARK_POSITION"){//Park Points
        typeColor = 'blue';
    }else{//Halt Point
        typeColor = 'gray';
    }
    pointRaw.fill = typeColor;
    pointRaw.stroke = 'black';
    pointRaw.linewidth = 1*elemPoint.ZOOM    ;
    elemPoint.typeColor = typeColor;
    elemPoint.pointRaw = pointRaw;

    //高亮样式
    var circle =  two.makeCircle(x,y,elemPoint.POINT_RADIUS*5);
    circle.noFill();
    circle.opacity = 0;
    circle.stroke = 'orangered';
    circle.linewidth = 3;
    elemPoint.circle = circle;

    //编辑样式
    var rect1 = two.makeRectangle(x+elemPoint.POINT_RADIUS,y+elemPoint.POINT_RADIUS,5,5);
    rect1.opacity = 0;
    rect1.stroke = 'black';
    rect1.linewidth = 1;
    elemPoint.rect1 = rect1;
    var rect2 = two.makeRectangle(x+elemPoint.POINT_RADIUS,y-elemPoint.POINT_RADIUS,5,5);
    rect2.opacity = 0;
    rect2.stroke = 'black';
    rect2.linewidth = 1;
    elemPoint.rect2 = rect2;
    var rect3 = two.makeRectangle(x-elemPoint.POINT_RADIUS,y+elemPoint.POINT_RADIUS,5,5);
    rect3.opacity = 0;
    rect3.stroke = 'black';
    rect3.linewidth = 1;
    elemPoint.rect3 = rect3;
    var rect4 = two.makeRectangle(x-elemPoint.POINT_RADIUS,y-elemPoint.POINT_RADIUS,5,5);
    rect4.opacity = 0;
    rect4.stroke = 'black';
    rect4.linewidth = 1;
    elemPoint.rect4 = rect4;

    //elemPoint.selectedPoint = false;
    //标题
    textTitle =  "P-"+name.slice(-3);
    var text = two.makeText(textTitle,x-textOffsetX,y-textOffsetY,styles);
    //console.log(text.getBoundingClientRect());
    //this.selectedText = false;
    elemPoint.textOffsetX = x-textOffsetX - elemPoint.pointRaw.translation.x;
    elemPoint.textOffsetY = y-textOffsetY - elemPoint.pointRaw.translation.x;
    elemPoint.text = text;

    elemPoint.group = two.makeGroup(elemPoint.pointRaw
        ,elemPoint.circle,elemPoint.rect1,elemPoint.rect2,elemPoint.rect3,elemPoint.rect4,elemPoint.text);

    //textTitleNumber++;

    elemPoint.getBoundingClientRect = function () {
        return elemPoint.pointRaw.getBoundingClientRect();
    };
    elemPoint.setTextOpacity = function(){
        elemPoint.text.opacity = elemPoint.text.opacity==0? 1:0;
    };
    elemPoint.setHighlight= function(val){
        elemPoint.circle.opacity = val ? 1 : 0;
    };
};
//
//Point.prototype = {
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
//        var top = this.point.translation.y + this.textOffsetY - elemPoint.POINT_RADIUS,
//            bottom = this.point.translation.y + this.textOffsetY + elemPoint.POINT_RADIUS,
//            left =  this.point.translation.x + this.textOffsetX - elemPoint.POINT_RADIUS * 3,
//            right =  this.point.translation.x + this.textOffsetX + elemPoint.POINT_RADIUS * 3;
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





//
//var pointDrag = function (e) {
//    e.preventDefault();
//    //拖动过程中查看是否变更了位置
//    var newgridx = getCurrentXY(e,true).X;
//    var newgridy = getCurrentXY(e,true).Y;
//    //如果在拖拽的点经过的点和以前不一样，并且经过的点为空
//    if ((newgridx != gridx || newgridy != gridy)
//        && gridlist[newgridx][newgridy] == '') {
//        //变换位置
//        var pointpos = gridlist[gridx][gridy].point.translation;
//        var x = pointpos.x + (newgridx - gridx) * gridlength;
//        var y = pointpos.y + (newgridy - gridy) * gridlength;
//        //文字和圆一起移动，移动到新位置，以前的点清空
//        gridlist[gridx][gridy].movePoint(x, y);
//        gridlist[newgridx][newgridy] = gridlist[gridx][gridy];
//        gridlist[gridx][gridy] = '';
//        gridx = newgridx;
//        gridy = newgridy;
//        two.update();
//    }
//};
//
//var textDrag = function (e) {
//    e.preventDefault();
//    var oX = getCurrentXY(e,false).X,
//        oY = getCurrentXY(e,false).Y;
//    //如果距离可以
//    //if(Math.abs((selectedText.text.translation.x - oX)*(selectedText.text.translation.x - oX) + (selectedText.text.translation.y - oY)*(selectedText.text.translation.y - oY)) > 181.9801 )
//    //{
//        selectedText.moveText(oX, oY);
//        two.update();
//    //}
//
//};
//
//var pointDragEnd = function (e) {
//    e.preventDefault();
//    $(window)
//        .unbind('mousemove', pointDrag)
//        .unbind('mouseup', pointDragEnd);
//};
//
//var textDragEnd = function (e) {
//    e.preventDefault();
//    $(window)
//        .unbind('mousemove', textDrag)
//        .unbind('mouseup', textDragEnd);
//};
//
//function getCurrentXY(e,girdOrMouse){
//    //测量窗口滚动的距离
//    var x, y;
//    if (window.pageYOffset) {    // all except IE
//        y = window.pageYOffset;
//        x = window.pageXOffset;
//    } else if (document.documentElement && document.documentElement.scrollTop) {    // IE 6 Strict
//        y = document.documentElement.scrollTop;
//        x = document.documentElement.scrollLeft;
//    } else if (document.body) {    // all other IE
//        y = document.body.scrollTop;
//        x = document.body.scrollLeft;
//    }
//    x = girdOrMouse ? parseInt((e.clientX + x - boardOffsetX) / (POINT_RADIUS*2)) : e.clientX + x - boardOffsetX;
//    y = girdOrMouse ? parseInt((e.clientY + y - boardOffsetY) / (POINT_RADIUS*2)) : e.clientY + y - boardOffsetY;
//    return {X: x, Y: y};
//}
//
//
//function windowPointMousedown(e){
//    e.preventDefault();
//    var oX = getCurrentXY(e,false).X,
//        oY = getCurrentXY(e,false).Y;
//    var whatSelect = '';
//    //来判断文字，添加选中状态
//    for (var i=0; i< pointList.length; i++) {
//        if (pointList[i].isCheckText(oX, oY)) {
//            selectedText = pointList[i];
//            whatSelect = 'text';
//            selectedText.selectText();
//            break;
//        }
//    }
//    //如果文字选中
//    if (whatSelect == 'text') {
//        $(window).unbind('mousemove')
//            .bind('mousemove', textDrag)
//            .bind('mouseup', textDragEnd);
//    }
//    else {
//        //对点进行选中，并且绑定拖拽的状态
//        gridx = parseInt(oX / gridlength);
//        gridy = parseInt(oY / gridlength);
//        if (gridlist[gridx][gridy]) {
//            selectedPoint = gridlist[gridx][gridy];
//            selectedPoint.selectPoint();
//            //gridlist[gridx][gridy].selectPoint();
//            $(window).unbind('mousemove')
//                .bind('mousemove', pointDrag)
//                .bind('mouseup', pointDragEnd);
//            two.update();
//        }
//        ////进行画圆
//        //else {
//        //    makepoint(gridx, gridy)
//        //}
//    }
//}
//
//function windowPointMouseup(e){
//    e.preventDefault();
//    gridx = getCurrentXY(e,true).X;
//    gridy = getCurrentXY(e,true).Y;
//    //如果有被选中的文本。执行的操作
//    if (selectedText){
//        selectedText.unSelectText();
//        two.update();
//        selectedText = '';
//    }
//    $(window).bind('mousemove', function (e) {
//        e.preventDefault();
//        var ox = getCurrentXY(e,false).X,
//            oy = getCurrentXY(e,false).Y;
//        var gridx = parseInt(ox / gridlength);
//        var gridy = parseInt(oy / gridlength);
//        //如果这点不是空的，并且该点没有滑过的状态
//        if (gridlist[gridx][gridy] != '' && gridlist[gridx][gridy] != mouseoverObject) {
//            //进行清空
//            if (mouseoverObject) {
//                mouseoverObject.setPointDefault();
//                mouseoverObject = '';
//            }
//            mouseoverObject = gridlist[gridx][gridy];
//            mouseoverObject.ondblclickStyle();
//        }//如果不存在
//        else if (!gridlist[gridx][gridy] && mouseoverObject) {
//            mouseoverObject.setPointDefault();
//            mouseoverObject = '';
//        }
//
//        two.update();
//    })
//}
//
//function windowPointMousemove(e){
//    e.preventDefault();
//    var gridx = getCurrentXY(e,true).X;
//    var gridy = getCurrentXY(e,true).Y;
//    //如果滑过的地方不是空并且该点不是被划过状态
//    if (gridlist[gridx][gridy] != '' && gridlist[gridx][gridy] != mouseoverObject) {
//        //如果存在划过状态的点，清空该状态
//        if (mouseoverObject) {
//            mouseoverObject.setPointDefault();
//            mouseoverObject = '';
//        }
//        //将对象存入，添加滑过状态
//        mouseoverObject = gridlist[gridx][gridy];
//        mouseoverObject.ondblclickStyle();
//        //如果不存在滑过 的点，并且鼠标
//    } else if (!gridlist[gridx][gridy] && mouseoverObject) {
//        mouseoverObject.setPointDefault();
//        mouseoverObject = '';
//    }
//
//    two.update();
//}
//
//
//function windowPointDefault(){
//    for (var i=0; i< pointList.length; i++) {
//        pointList.setPointDefault();
//    }
//}
//
//
//
//
//
////如果按住delete键，并且该对象被选中，则删除该对象
//$(document).keydown(function (event) {
//    if (event.keyCode == 46) {
//        if (gridlist[gridx][gridy].selectedPoint) {
//            gridlist[gridx][gridy].removePoint();
//            gridlist[gridx][gridy] = '';
//            this.selectedPoint = '';
//            two.update();
//        }
//    }
//});