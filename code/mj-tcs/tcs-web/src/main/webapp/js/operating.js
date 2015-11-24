'use strict';

$(function(){

    initTree()

	$(".elements-blocks-title li").each(function(){

        $(this).click(function(){
            $(".elements-blocks-title li").removeClass("active");
            $(this).addClass("active");
            // alert($(this).index());
            if($(this).index() == 0){
            	$(".elements-blocks .elements-content").css("display","block");
            	$(".elements-blocks .blocks-content").css("display","none");

            } else if($(this).index() == 1) {
            	
				$(".elements-blocks .elements-content").css("display","none");
            	$(".elements-blocks .blocks-content").css("display","block");
            } else {

            }
        })
     
    });

    $(".operation-list .to").click(function() {
    	if ($(".top-panel .top-panel-to").css("display") == 'none') {
    		$(".top-panel .top-panel-to").css("display","block");
    	} else {
    		$(".top-panel .top-panel-to").css("display","none");
    	}
    	
    })


    // 绑定双击事件 依据固定不变的父层
    $('.properties .table-body tbody').on('dblclick', 'tr td:nth-child(2)', function(){
        var value = $(this).text();
        $(this).text("");
        $(this).append("<input type='text' value='"+value+"' />");
        // alert($(this).text())
    })


    // 绑定插入input框的blur事件
    $('.properties .table-body tbody').on('blur', 'input', function(){
        $(this).parent().text($(this).val());

        // TODO 
    })

})


//基本设定
var setting = {
        data: {
            key: {
                title:"treeNodes"
            },
            simpleData: {
                enable: true
            }
        },
        callback: {
            beforeClick: beforeClick,
            onClick: onClick
        }
    };

// 初始化结构
var eleNodes =[
    { id:1, pId:0, name:"Vehicles", t:"vehicle", open:false},
    { id:11, pId:1, name:"vehicle - 1", t:"vehicle2", icon:"../images/vehicle.18x18.png"},
    { id:12, pId:1, name:"vehicle - 2", t:"vehicle3", icon:"../images/vehicle.18x18.png"},
    { id:13, pId:1, name:"vehicle - 3", t:"vehicle4", icon:"../images/vehicle.18x18.png"},
    { id:2, pId:0, name:"Points", t:"point", open:true},
    { id:21, pId:2, name:"point - 1", t:"point1..", click:false, icon:"../images/point.18x18.png"},
    { id:22, pId:2, name:"point - 2", t:"point2..", click:false, icon:"../images/point.18x18.png"},
    { id:23, pId:2, name:"point - 3", t:"point3..", click:false, icon:"../images/point.18x18.png"},
    { id:3, pId:0, name:"Path", t:"Path...", open:true, click:false },
    { id:31, pId:3, name:"Path3 - 1", t:"path1", icon:"../images/path.18x18.png"},
    { id:32, pId:3, name:"Path3 - 2", t:"path2", icon:"../images/path.18x18.png"},
    { id:33, pId:3, name:"Path3 - 3", t:"path3", icon:"../images/path.18x18.png"}
];

var bloNodes =[
    { id:1, pId:0, name:"Block-Block001", t:"vehicle", open:false},
    { id:11, pId:1, name:"Point-0001 --- Point-0002", t:"p12", icon:"../images/block.18x18.png"},
    { id:12, pId:1, name:"Point-0002 --- Point-0003", t:"p34", icon:"../images/block.18x18.png"},
    { id:13, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:14, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:15, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:16, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:17, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:18, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:19, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:20, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:2, pId:0, name:"PBlock-Block002", t:"point", open:true},
    { id:21, pId:2, name:"Point-0006 --- Point-0007", t:"p67..", click:false, icon:"../images/block.18x18.png"},
    { id:22, pId:2, name:"Point-0008 --- Point-0009", t:"989..", click:false, icon:"../images/block.18x18.png"},
    { id:23, pId:2, name:"Point-0010 --- Point-0011", t:"p1011..", click:false, icon:"../images/block.18x18.png"},
    { id:3, pId:0, name:"Block-Block003", t:"Path...", open:true, click:false },
    { id:31, pId:3, name:"Point-0004 --- Point-0005", t:"path1", icon:"../images/block.18x18.png"},
    { id:32, pId:3, name:"Point-0004 --- Point-0005", t:"path2", icon:"../images/block.18x18.png"},
    { id:33, pId:3, name:"Point-0004 --- Point-0005", t:"path3", icon:"../images/block.18x18.png"},
    { id:34, pId:3, name:"Point-0004 --- Point-0005", t:"path1", icon:"../images/block.18x18.png"},
    { id:35, pId:3, name:"Point-0004 --- Point-0005", t:"path2", icon:"../images/block.18x18.png"},
    { id:36, pId:3, name:"Point-0004 --- Point-0005", t:"path3", icon:"../images/block.18x18.png"},
    { id:4, pId:0, name:"Block-Block003", t:"Path...", open:true, click:false },
    { id:41, pId:4, name:"Point-0004 --- Point-0005", t:"path1", icon:"../images/block.18x18.png"},
    { id:42, pId:4, name:"Point-0004 --- Point-0005", t:"path2", icon:"../images/block.18x18.png"},
    { id:43, pId:4, name:"Point-0004 --- Point-0005", t:"path4", icon:"../images/block.18x18.png"},
    { id:44, pId:4, name:"Point-0004 --- Point-0005", t:"path1", icon:"../images/block.18x18.png"},
    { id:45, pId:4, name:"Point-0004 --- Point-0005", t:"path2", icon:"../images/block.18x18.png"},
    { id:46, pId:4, name:"Point-0004 --- Point-0005", t:"path3", icon:"../images/block.18x18.png"}
];


var initTree = function(){
   $.fn.zTree.init($("#elements-tree"), setting, eleNodes);
   $.fn.zTree.init($("#blocks-tree"), setting, bloNodes);
}

var pro, className = "dark";

function beforeClick(treeId, treeNode, clickFlag) {

    className = (className === "dark" ? "" : "dark");
    //howpro("[ " + getTime() + " beforeClick ]&nbsp;&nbsp;" + treeNode.name);
    
    return (treeNode.click != false);
}

function onClick(event, treeId, treeNode, clickFlag) {
     // alert(event, treeId, treeNode, clickFlag)
     console.log(treeNode)

     var count = 1;
     var str = "";
     $.each(treeNode, function(key, value) {
        if(count < 5){
            console.log(key+"--"+value);
            str+="<tr><td>"+key+"</td><td>"+value+"</td></tr>"
            count ++;
        } else {
            return;
        }
        
    });

     showAttr(str)
}

// function showPro(str) {
//     if (!pro) pro = $("#pro");
//     pro.append("<li class='" + className + "'>" + str + "</li>");
//     if (pro.children("li").length > 8) {
//         pro.get(0).removeChild(pro.children("li")[0]);
//     }
// }

// function getTime() {
//     var now = new Date(),
//         h = now.getHours(),
//         m = now.getMinutes(),
//         s = now.getSeconds();
//     return (h + ":" + m + ":" + s);
// }

var showAttr = function(str){
    // $("#table1 tr th:not(:nth-child(1))").remove();  
    var cont = $(".properties .table-body tbody tr:not(:nth-child(1)");
     console.log(cont)
     
     cont.remove()

     $(".properties .table-body tbody tr:last").after(str);

}