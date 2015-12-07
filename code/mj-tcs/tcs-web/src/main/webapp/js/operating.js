

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

    //async:{
    //    enable:true,
    //    url:"/tcs-web/web/scene/12",
    //    type:"get",
    //    contentType:"application/json",
    //    autoParam:["id","name"],
    //    otherParam:{},
    //    dataFilter:filter
    //},
    callback: {
        beforeClick: beforeClick,
        onClick: onClick
    }
};

var eleNodes=[];

var bloNodes =[
    { id:1, pId:0, name:"Block-Block001", title:"vehicle",position:'1111111', open:false},
    { id:22, pId:1, name:"Point-0001 --- Point-0002", t:"p12", icon:"../images/block.18x18.png"},
    { id:12, pId:1, name:"Point-0002 --- Point-0003", t:"p34", icon:"../images/block.18x18.png"},
    { id:13, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:14, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:15, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:16, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:17, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:18, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:19, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
    { id:20, pId:1, name:"Point-0004 --- Point-0005", t:"p45", icon:"../images/block.18x18.png"},
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


$(function(){

    // 初始化树结构数据
    initTree();

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

// get data for tree
var getSceneContent = function (id) {
    $.ajax({
        type: "GET",
        url: "/tcs-web/web/scene/"+id,
        dataType: "json",
        cache: false,//false时，会自动添加时间戳
        timeout: 1000,
        success: function (data) {
            buildTree(data);
            $.fn.zTree.init($("#elements-tree"), setting, eleNodes );
        },
        error: function (xhr) {
           console.log(xhr);
        }
    });
}


var buildTree = function(data) {
    var rootPoint = { id:1, pId:0, name:'Point', t:'Point', open:false};
    eleNodes.push(rootPoint)
    var point_array = data.points;
    if(point_array) {
        $.each(point_array, function(idx, obj) {
            var pointObj = new Object();
            pointObj.id = obj.id;
            pointObj.pId = 1;
            pointObj.name = obj.name;
            pointObj.title = obj.name+obj.type;
            pointObj.click = true;
            pointObj.icon = "../images/point.18x18.png";
            eleNodes.push(pointObj);

        });

    }

    var rootPath = { id:2, pId:0, name:'Path', t:'Path', open:false};
    eleNodes.push(rootPath);
    var path_array = data.paths;
    if(path_array) {
        $.each(path_array, function(idx, obj) {
            var pathObj = new Object();
            pathObj.id = obj.id;
            pathObj.pId = 2;
            pathObj.name = obj.name;
            pathObj.title = obj.name+obj.id;
            pathObj.click = true;
            pathObj.icon = "../images/path.18x18.png";
            eleNodes.push(pathObj);
        });
    }

    var rootLocation = { id:3, pId:0, name:'Location', t:'Location', open:false};
    eleNodes.push(rootLocation);
    var location_array = data.locationDtos;
    if(location_array) {
        $.each(location_array, function(idx, obj) {
            var pathObj = new Object();
            pathObj.id = obj.id;
            pathObj.pId = 2;
            pathObj.name = obj.name;
            pathObj.title = obj.name+obj.id;
            pathObj.click = true;
            pathObj.icon = "../images/location.18x18.png";
            eleNodes.push(pathObj);
        });
    }

    var rootLocationType = { id:4, pId:0, name:'LocationType', t:'LocationType', open:false};
    eleNodes.push(rootLocationType);
    var locationType_array = data.location_types;
    if(locationType_array) {
        $.each(locationType_array, function(idx, obj) {
            var pathObj = new Object();
            pathObj.id = obj.id;
            pathObj.pId = 2;
            pathObj.name = obj.name;
            pathObj.title = obj.name+obj.id;
            pathObj.click = true;
            pathObj.icon = "../images/locationType.18x18.png";
            eleNodes.push(pathObj);
        });
    }

    var rootStaticRoutes = { id:5, pId:0, name:'StaticRoutes', t:'StaticRoutes', open:false};
    eleNodes.push(rootStaticRoutes);

    var staticRoutes_array = data.static_routes;
    if(staticRoutes_array) {
        $.each(staticRoutes_array, function(idx, obj) {
            var pathObj = new Object();
            pathObj.id = obj.id;
            pathObj.pId = 2;
            pathObj.name = obj.name;
            pathObj.title = obj.name+obj.id;
            pathObj.click = true;
            pathObj.icon = "../images/staticRoute.18x18.png";
            eleNodes.push(pathObj);
        });
    }

}



var initTree = function(){
    getSceneContent(12)

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
        if(count < 10){
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
    var cont = $(".properties .table-body tbody tr:not(:nth-child(1))");
     console.log(cont)
     
     cont.remove()

     $(".properties .table-body tbody tr:last").after(str);

}