$(function(){
	// 监听浏览器窗口变化，重置高度，保持充满整个窗口
	$(window).resize(function(){
		initSize();
	});
	// 初始化
	initSize();

	//ruler.constructRulers({container: document.querySelector('.tcs-ruler')});
    editor.constructEditor({container: document.querySelector('.tcs-editor')});

    //// Make an instance of two and place it on the page.
    //var elem = document.querySelector('.editor_wrapper');
    //var params = {width: 285, height: 200, type:Two.Types.webgl};
    //var two = new Two(params).appendTo(elem);
    //// two has convenience methods to create shapes.
    //var circle = two.makeCircle(50, 50, 50);
    //var rect = two.makeRectangle(213, 100, 100, 100);
    //
    //// The object returned has many stylable properties:
    //circle.fill = '#FF8000';
    //circle.stroke = 'orangered'; // Accepts all valid css color
    //circle.linewidth = 5;
    //
    //rect.fill = 'rgb(0, 200, 255)';
    //rect.opacity = 0.75;
    //rect.noStroke();
    //
    //// Don't forget to tell two to render everything
    //// to the screen
    //two.update();
})



// 初始化 页面各模块高度
function initSize(){
	$(".container-fluid").css("height", $(window).height() - $("header").height());
}