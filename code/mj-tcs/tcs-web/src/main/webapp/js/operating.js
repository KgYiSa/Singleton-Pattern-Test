

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


// format json
var fJosn = {};


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

    // operating，针对editor的共通操作eg：缩放，显示/隐藏网格线,title,blocks等
    $(".left-container .top-panel .top-panel-view .tcs-bottom div[class^=show-]").click(function(){
        //console.log(this.className);
        if($(this).hasClass("selected")) {

            $(this).removeClass("selected");
            switch(this.className){
                case 'show-reset':

                    // TODO
                    break;
                case 'show-splits':
                    showGrid(0);
                    break;

            }
        } else {
            switch(this.className){
                case 'show-reset':
                    $(".zoom-label #zoom").val(100).change();
                    // TODO
                    break;
                case 'show-splits':
                    showGrid(1);
                    break;

            }
            $(this).addClass("selected");
        }

    })


    // 鼠标位置信息显示
    $(".tcs-editor").mousemove(function(e){
        var x = e.pageX;
        var y = e.pageY;
        $(".left-container .top-panel .top-panel-view .tcs-bottom .show-position").html("X:"+x+", Y:"+y);
    })


    // select 与 input数据一致
    $(".zoom-label #zoom_select").change(function(){
        alert($(this).val())
        $(".zoom-label #zoom").val($(this).val())
        $(".zoom-label #zoom").change();
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


    // 场景选择相关
    $('#sceneselect').on('show.bs.modal', function () {
        getSceneBaseList();

    })

    $('#sceneselect').on('hide.bs.modal', function () {
        $("#sceneselect .modal-body tbody").html("");
    })

    // 选中 radio事件
    $('#sceneselect .modal-body tbody').on('click', 'td #selectId', function(){
        var status = $(this).parent().parent().parent().find("td:nth-child(3) span").text();
        if(status != "running"){
            alert("请先启动场景...");
        }
    })

    // start/stop scene状态
    $('#sceneselect .modal-body tbody').on('click', 'td button', function(){
        var operateId = $(this).val();
        var operate = $(this).text();
        operateSceneById(operateId, operate,this);

    });

    $("#sceneselect .modal-footer .submmit").click(function(){
        var selectRadio = $("#sceneselect .modal-body tbody input[name=selectId]:checked");
        if(selectRadio.val() == undefined) {
            if (confirm("您还未选择任何场景，确定继续？")) {
                $('#sceneselect').modal("hide");
            }
        }else if(selectRadio.parent().parent().parent().find("td:nth-child(3) span").text() != "running"){
            alert("请先启动场景...");
        } else {
            $('#sceneselect').modal("hide");
            var  sceneTitle = selectRadio.parent().parent().parent().find("td:nth-child(2) span").text();
            $('#scenename').html(sceneTitle);
            $('#scenename').attr("title", sceneTitle);
            $('header .title').text(sceneTitle);
            $('header .title').attr("title", sceneTitle);
            getSceneContent(selectRadio.val());
        }
    });



    //hidden .right-container
    $(".right-container .side-button").click(function(){
        if($(".right-container .elements-blocks").css("display") == "none"){
            $(".left-container").width("75%");
            $(".right-container .elements-blocks").css("display","block");
            $(".right-container .properties").css("display","block");
            $(".right-container").css("width","25%");
        } else {
            $(".right-container .elements-blocks").css("display","none");
            $(".right-container .properties").css("display","none");
            $(".right-container").css("width",15);
            $(".left-container").css("width", $(".container-fluid").width() - 15);
        }

    });



})


// 查询所有场景（id,name,status）
var getSceneBaseList = function() {
    $.ajax({
        type: "GET",
        url: "/rest/scenes/profile",
        dataType: "json",
        cache: false,//false时，会自动添加时间戳
        timeout: 1000,
        success: function (data) {

            var trContent;
            $.each(data.body, function(idx, obj){
                trContent += "<tr style='vertical-align: inherit;'>";
                trContent += "<td><span>" +obj.id +"</span></td>";
                trContent += "<td><span>"+obj.name+"</span></td>";

                if(obj.status == 'running') {
                    trContent += "<td><span class='label label-success'>"+obj.status+"</span></td>"
                    trContent += "<td><button value='"+obj.id+"'>stop</button></td>"
                } else {
                    trContent += "<td><span class='label label-default'>"+obj.status+"</span></td>"
                    trContent += "<td><button value='"+obj.id+"'>start</button></td>"
                }

                trContent += "<td><span><input type='radio' name='selectId' id='selectId' value='"+obj.id+"'></span></td>";
                trContent += "</tr>"
            });
            $("#sceneselect .modal-body tbody").append(trContent);
        },
        error: function (xhr) {
            alert("error！")
        }
    });
}

// 根据id启动/关闭场景
var operateSceneById = function(id, operate, obj){
    $.ajax({
        type: "GET",
        url: "/rest/scenes/" + id + "/actions/" + operate,
        dataType: "text",
        cache: false,//false时，会自动添加时间戳
        timeout: 1000,
        success: function (data, jqXHR, textStatus) {
            if(textStatus.status == 200){
                var trCont =  $(obj).parent().parent();
                console.log(trCont)
                if(operate == "start") {
                    trCont.find("td:nth-child(3)").html("<span class='label label-success'>running</span>");
                    trCont.find("td:nth-child(4) button").text("stop");
                } else {
                    trCont.find("td:nth-child(3)").html("<span class='label label-default'>stopped</span>");
                    trCont.find("td:nth-child(4) button").text("start");
                }

            }

        },
        error: function (xhr) {
            alert("error！")
        }
    });
}

//基准网格的显示/隐藏 1:显示 0不显示
var showGrid = function(flg){
    var rect1 = document.getElementById("rect1");
    var rect2 = document.getElementById("rect2");
    if(flg == 1) {
        rect1.setAttribute("fill", "url(#grid1)");
        rect2.setAttribute("fill", "url(#grid2)");
    } else {
        rect1.setAttribute("fill", "none");
        rect2.setAttribute("fill", "none");
    }

}


// get data for tree
var getSceneContent = function (id) {
    $.ajax({
        type: "GET",
        url: "/web/scenes/"+id,
        dataType: "json",
        cache: false,//false时，会自动添加时间戳
        timeout: 1000,
        success: function (data) {
            if(data){
                buildTree(data);
            }

        },
        error: function (xhr) {
            alert("error!");
        }
    });
}


var buildTree = function(data) {

    // 保存数据到localstorage
    if(window.localStorage) {
        console.log(data)
        //console.log(data.id)
        if(data){
            if(localStorage.getItem("currentScene")) {

                localStorage.setItem("currentScene", data.id);
                localStorage.setItem("sceneJson", JSON.stringify(data));
            } else {
                localStorage.setItem("currentScene", data.id);
                localStorage.setItem("sceneJson", JSON.stringify(data));
            }


            $("header .title").text(data.name);
            $("header .title").attr("title", data.name);
        } else {
            return
        }


    }



    var eleNodes=[];
    //locations --> attached_links
    var initElementsObj = ["vehicles", "points", "paths", "locations", "location_types", "attached_links", "static_routes"];
    var initBlocksObj = ["blocks"];

    //--------------------------elements-----------------------------------------
    // elements[vehicles & Layout VLayout-01]
    var rootVehicles = { id:1, pId:0, name:'Vehicles', click:false, title:'Vehicles', open:false};
    var rootLayout = { id:2, pId:0, name:'Layout one', click:false, title:'Layout one', icon:"../images/figure.18x18.png", open:true};
    eleNodes.push(rootVehicles);
    eleNodes.push(rootLayout);

    //init sub folder
    for(var idx in initElementsObj) {
        var treeNode = {id: 0, pId: 0, name: "", title: "", click: false, icon: "", open: false};

        var elem = initElementsObj[idx];
        var array = data[elem];
        if (elem == "vehicles") {
            //// *100 主要为了将id于pId区分
            //treeNode.id = rootVehicles.id * 100;
            //treeNode.pId = rootVehicles.id;
            //treeNode.name = elem;
            //treeNode.title = elem;
            //treeNode.click = true;
            //treeNode.icon = "";
            //treeNode.open = false;
            //eleNodes.push(treeNode);
            $.each(array, function (index, attr) {
                var pointObj = {id: 0, pId: 0, name: "", title: "", click: true, icon: "", open: false, elemId: 0};
                pointObj.id = parseInt(rootVehicles.id + "" + attr.id);
                pointObj.pId = rootVehicles.id;
                pointObj.name = attr.name;
                pointObj.title = elem;
                pointObj.click = true;
                pointObj.icon = "./images/" + elem + ".18x18.png";
                pointObj.elemId = attr.UUID;
                eleNodes.push(pointObj);
                fJosn[attr.UUID] = attr;


            })

        } else if (elem == 'attached_links') {
            // *100 主要为了将id于pId区分
            treeNode.id = (idx + 1) * 100;
            treeNode.pId = rootLayout.id;
            treeNode.name = elem;
            treeNode.title = elem;
            treeNode.click = false;
            treeNode.icon = "";
            treeNode.open = false;
            eleNodes.push(treeNode);


            var array = data[initElementsObj[3]]

            if (array) {
                $.each(array, function (index, attr) {
                        $.each(attr.attached_links, function (id, link) {
                            var pointObj = { id: 0, pId: 0, name: "", title: "", click: true, icon: "", open: false, elemId: 0 };
                                pointObj.id = treeNode.id + "" + link.id;
                                pointObj.pId = treeNode.id;
                                pointObj.name = link.name;
                                pointObj.title = elem;
                                pointObj.click = true;
                                pointObj.elemId = attr.UUID
                                pointObj.icon = "./images/"+elem+".18x18.png";
                                eleNodes.push(pointObj);
                                fJosn[attr.UUID] = attr;
                        });
                    }
                )

            }
        } else {
            // *100 主要为了将id于pId区分
            treeNode.id = (idx + 1) * 100;
            treeNode.pId = rootLayout.id;
            treeNode.name = elem;
            treeNode.title = elem;
            treeNode.click = false;
            treeNode.icon = "";
            treeNode.open = false;
            eleNodes.push(treeNode);
            if(array) {
                $.each(array, function (index, attr) {
                    var pointObj = {id: 0, pId: 0, name: "", title: "", click: true, icon: "", open: false, elemId: 0};
                    pointObj.id = treeNode.id + "" + attr.id;
                    pointObj.pId = treeNode.id;
                    pointObj.name = attr.name;
                    pointObj.title = elem;
                    pointObj.click = true;
                    pointObj.icon = "./images/" + elem + ".18x18.png";
                    pointObj.elemId = attr.UUID;
                    eleNodes.push(pointObj);
                    fJosn[attr.UUID] = attr;

                })
            }

        }

    }
console.log(fJosn)
    console.log(eleNodes)
    $.fn.zTree.init($("#elements-tree"), setting, eleNodes);

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

    $.fn.zTree.init($("#blocks-tree"), setting, bloNodes);
}


var initTree = function(){
    if(window.localStorage) {
        if(localStorage.getItem("currentScene")) {
            var data = localStorage.getItem("sceneJson");
            buildTree(JSON.parse(data));

        } else {
            return
        }
    } else {
        alert("this browser does not support localStorage!")
    }

}

var pro, className = "dark";

function beforeClick(treeId, treeNode, clickFlag) {

    className = (className === "dark" ? "" : "dark");
    //howpro("[ " + getTime() + " beforeClick ]&nbsp;&nbsp;" + treeNode.name);
    
    return (treeNode.click != false);
}


// 树节点响应信息
function onClick(event, treeId, treeNode, clickFlag) {
     // alert(event, treeId, treeNode, clickFlag)
    var comProNotDis = ["id", "version", "UUID", "outgoing_paths", "incoming_paths", "attached_links"];
    var reArryProValueDis = ["hops"];   //array
    var reJsonProValueDis = ["location_type", "source_point", "destination_point"];  //object
    var reProDis = ["properties", "locked"];


    var uuid = treeNode.elemId;
    var treeNodeInfo = fJosn[uuid];
    var str = "";

     $.each(treeNodeInfo, function(key, value) {

        if(comProNotDis.indexOf(key) < 0 ) {

            if(reArryProValueDis.indexOf(key) >= 0) {
                var hopsStr = "";

                $.each(value, function(id, obj){
                    hopsStr += obj.name + ",";
                })
                value = hopsStr;
            } else if(key == "properties") {
                var proStr = "";
                if(value && value.length > 0 ) {
                    $.each(value, function(obj, id){
                        proStr += obj.key+":"+obj.value;
                    })
                    value = proStr;
                } else {
                    value = "";
                }
            } else if(reJsonProValueDis.indexOf(key) >= 0) {
                value = value.name;
            } else if(key == "locked") {
                if(!value){
                    value = "<input type='checkbox' name='locked' value='locked' checked='checked' disabled='disabled'/>";
                } else {
                    value = "<input type='checkbox' name='locked' value='unlocked' disabled='disabled' />";
                }

            } else {
                if(!value){
                    value = "";
                } else if( isJson(value)) {
                    value = JSON.stringify(value)
                }

            }
            str+="<tr><td>"+key+"</td><td>"+value+"</td></tr>";

        }



    });

     showAttr(str)
}

// 判断数组
function isArray(obj) {
    return Object.prototype.toString.call(obj) === '[object Array]';
}

// 判断是否是json对象
function isJson(obj) {
    return typeof(obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length;
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
    //var cont = $(".properties .table-body tbody tr:not(:nth-child(1))");
    // console.log(cont)
    //var cont = $(".properties .table-body tbody");
    // cont.remove();
    $(".properties .table-body tbody ").html("")
     $(".properties .table-body tbody ").append(str);

}