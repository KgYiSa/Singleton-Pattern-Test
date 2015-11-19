$(function(){
	// 监听浏览器窗口变化，重置高度，保持充满整个窗口
	$(window).resize(function(){
		initSize();
	});
	// 初始化
	initSize();
})



// 初始化 页面各模块高度
function initSize(){
	$(".container-fluid").css("height", $(window).height() - $("header").height());
}