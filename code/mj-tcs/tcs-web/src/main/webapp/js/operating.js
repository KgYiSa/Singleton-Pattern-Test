// Dependencies:
// 1) jquery-1.11.3.js
// 2) bootstrap.min.js
// 3) jquery.ztree.core-3.5.js
// 4) mousewheel.js
// 5) tcs-editor.js

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


// format json
var fJson = {};

$(function(){
    //startLoading();

    // 初始化树结构数据
    //initTree();

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

    ////平移按钮
    //$(".operation-list .hand").click(function() {
    //    // TODO:
    //    window.tcsDraw.translate(100,100);
    //});
    //
    ////工单按钮
    //$(".operation-list .to").click(function() {
    //	if ($(".top-panel .top-panel-to").css("display") == 'none') {
    //		$(".top-panel .top-panel-to").css("display","block");
    //	} else {
    //		$(".top-panel .top-panel-to").css("display","none");
    //	}
    //
    //})


    // reset zoom
    $(".left-container .top-panel .top-panel-view .tcs-bottom .reset-zoom").click(function(){
        $(".zoom-label #zoom").val(100).change();
    });



    // operating，针对editor的共通操作eg：缩放，显示/隐藏网格线,title,blocks等
    $(".left-container .top-panel .top-panel-view .tcs-bottom div[class^=show-]").click(function(){
        //console.log(this.className);

        if($(this).hasClass("selected")) {

            $(this).removeClass("selected");
            switch(this.className){
                //case 'show-reset':
                //
                //    // TODO
                //    break;
                case 'show-splits':
                    showGrid(true);
                    break;
                case 'show-comment':
                    showText(true);
                    break;

            }
        } else {
            switch(this.className){
                //case 'show-reset':
                //    $(".zoom-label #zoom").val(100).change();
                //    // TODO
                //    break;
                case 'show-splits':
                    showGrid(false);
                    break;
                case 'show-comment':
                    showText(false);
                    break;

            }
            $(this).addClass("selected");
        }

    })


    // Already moved to tcs-editor.js
    //// 鼠标位置信息显示
    //$(".tcs-editor").mousemove(function(e){
    //    var x = e.pageX - $(this).offset().left;
    //    var y = e.pageY - $(this).offset().top;
    //    $(".left-container .top-panel .top-panel-view .tcs-bottom .show-position").html("X:"+x+", Y:"+y);
    //})


    // select 与 input数据一致
    $(".zoom-label #zoom_select").change(function(){
        $(".zoom-label #zoom").val($(this).val())
        $(".zoom-label #zoom").change();
    })


    // 绑定双击事件 依据固定不变的父层
    $('.properties .table-body tbody').on('dblclick', 'tr td:nth-child(2)', function(){
        //把双击事件注释，让右侧小车栏位下方的Attribute和Value只读
        //var value = $(this).text();
        //$(this).text("");
        //$(this).append("<input type='text' value='"+value+"' />");
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

    $("#sceneselect .modal-footer .submit").click(function(){
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
            //$('#scenename').html(sceneTitle);
            //$('#scenename').attr("title", sceneTitle);
            $('header .title').text(sceneTitle);
            $('header .title').attr("title", sceneTitle);
            startLoading();
            getSceneContent(selectRadio.val());
        }
    });

    // hidden vehicle list
    $(".left-container .bottom-button").click(function(){
        if($(".left-container .bottom-panel").css("display") == "none"){
            $(".left-container .top-panel").css("height", "80%");
            $(".left-container .bottom-panel").css("display", "");

        } else {
            $(".left-container .bottom-panel").css("display", "none");
            $(".left-container .top-panel").css("height", $(".left-container").height() - 15);
        }

    });

    //hidden .right-container
    $(".right-container .side-button").click(function(){

        if($(".right-container .elements-blocks").css("display") == "none"){
            $(".left-container").width("75%");
            $(".right-container .elements-blocks").css("display",'');
            $(".right-container .side-bottom-button").css("display",'');
            $(".right-container .properties").css("display",'');
            $(".right-container").css("width","25%");

        } else {
            $(".right-container .elements-blocks").css("display","none");
            $(".right-container .properties").css("display","none");
            $(".right-container .side-bottom-button").css("display","none");
            //$(".right-container .side-button").css("display", "none");
            $(".right-container").css("width",15);
            $(".left-container").css("width", $(".container-fluid").width() - 10 - 30);
        }

    });


    // hidden .right-container > .properties
    $(".right-container .side-bottom-button").click(function(){

        if($(".right-container .properties").height() == 0 || $(".right-container .properties").css("display") == "none") {

            $(".right-container .elements-blocks").css("height","70%");
            $(".right-container .properties").css("display",'');
            $(".right-container .properties").css("height", $(".right-container").height() - $(".right-container .elements-blocks").height());
        
        } else {
            $(".right-container .properties").css("height",0);

            $(".right-container .elements-blocks").css("height","100%");
        }


    });



    // show vehicle base info
    //不让点击后出现弹框
    //$(".bottom-panel .bottom-panel-list").on("click", ".vehicle", (function(){
    //    var uuid = $(this).attr("vehicle-uuid");
    //    var vehicleDtail = fJson[uuid];
    //    $("#vehicleDetail .modal-body").html("");
    //    var vData = JSON.stringify(vehicleDtail)+ "<br><button type='button' class='btn btn-primary btn-sm' onclick=dispatchVehicle('"+uuid+"')>Dispatch Vehicle </button>";
    //    $("#vehicleDetail .modal-body").html(vData);
    //    $("#vehicleDetail").modal("show");
    //}));


    // tcs-editor  right operate menu
    $(".left-container .top-panel .top-panel-operation .operation-list img").click(function(){
        // find vehicle from a vihicle list
        if($(this)[0].id == "find-vehicle") {
            $("#operate-popup #title").html("");
            $("#operate-popup #title").html("请选择小车");

            if(supportLocalStorage()) {
                var data = localStorage.getItem("sceneJson");
                var vehicleList = JSON.parse(data)["vehicles"];
                console.log(vehicleList)
                if(vehicleList){
                    var dropdownItem = "";
                    $.each(vehicleList, function (index, attr) {
                        dropdownItem += "<li><a href='#' vehicle-uuid='" + attr.UUID + "' title='" + attr.name + "'>" + attr.name + "</a></li>";
                    })
                    $("#operate-popup .modal-body .dropdown-menu").html("");
                    $("#operate-popup .modal-body .dropdown-menu").html(dropdownItem);
                }
            }
            $("#operate-popup").modal("show");

        } else if($(this)[0].id == "create-torder"){


            $("#create-to table tbody").html("");
            $("#create-to").modal("show");
            // TODO other operate
        } else if ($(this)[0].id == "pan") {
            window.tcsDraw.translate(100,100);
        } else {

        }

    });

    // 显示所有工位和 所有状态
    $("#show-location-list").click(function(){

        if(supportLocalStorage()) {
            var data = localStorage.getItem("sceneJson");
            var locationList = JSON.parse(data)["locations"];
            //console.log(locationList)
            if(locationList){
                var dropdownItem = "";
                $.each(locationList, function (index, attr) {
                    dropdownItem += "<li><a href='#' location-uuid='" + attr.UUID + "' title='" + attr.name + "'>" + attr.name + "</a></li>";
                })
                $("#select-location .modal-body .location .dropdown-menu").html("");
                $("#select-location .modal-body .location .dropdown-menu").html(dropdownItem);
            }
        }

        $("#select-location .modal-body .state .dropdown-menu").html("<li><a href='#'>NOP</a></li>");
        $("#select-location").modal("show");
    });

    // id: select-location dropdownmenu click
    $("#select-location .modal-body .location .dropdown-menu").on("click", "a", function(){
        $("#select-location .modal-body #location-dropdownMenu").html($(this).text()+"<span class='caret'></span>");
        $("#select-location .modal-footer .submit").attr("location-uuid", $(this).attr("location-uuid"));
        $("#select-location .modal-footer .submit").attr("location-name", $(this).attr("title"));
    });

    $("#select-location .modal-body .state .dropdown-menu").on("click", "a", function(){
        $("#select-location .modal-body #location-state-dropdownMenu").html($(this).text()+"<span class='caret'></span>");
        $("#select-location .modal-footer .submit").attr("state", $(this).text());
    });

    // id: select-location submit
    $("#select-location .modal-footer .submit").on("click", function(){
        var location_uuid = $(this).attr("location-uuid");
        var location_name = $(this).attr("location-name");
        var state = $(this).attr("state");
        if(location_uuid == undefined || state == undefined || location_name == undefined) {
            alert("please select the right location and state");
            return
        }
        $("#create-to table tbody").append("<tr><td location-uuid='"+ location_uuid +"'>"+location_name+"</td><td state='"+state+"'>"+ state +"</td></tr>");

        $("#select-location .modal-footer .submit").removeAttr("location-uuid");
        $("#select-location .modal-footer .submit").removeAttr("location-name");
        $("#select-location .modal-body #location-dropdownMenu").html("请选择工位<span class='caret'></span>");
        $("#select-location .modal-body #location-state-dropdownMenu").html("请选择状态<span class='caret'></span>");
        $("#select-location .modal-footer .submit").removeAttr("state");
        $("#select-location").modal("hide");
    })

    // id: create-to modal submit
    $("#create-to .modal-footer .submit").on("click", function(){
        var tbodyContent = $("#create-to table tbody");
        if(tbodyContent.html() != ''){
            var location_uuid = tbodyContent.find("td:eq(0)").attr("location-uuid");
            var state = tbodyContent.find("td:eq(1)").attr("state");

            createTransport(location_uuid, state);
            $("#create-to").modal("hide");

        } else {
            $("#create-to").modal("hide");
            return
        }

    })


    // tcs-editor  right operate menu --- dropdownmenu
    $("#operate-popup .modal-body .dropdown-menu").on("click", "a", function(){
        $("#operate-popup .modal-body #vehicle-dropdownMenu").html($(this).text()+"<span class='caret'></span>");
        $("#operate-popup .modal-footer .submit").attr("vehicle-uuid", $(this).attr("vehicle-uuid"));
    });

    // tcs-editor  right operate menu --- submit
    $("#operate-popup .modal-footer .submit").on("click", function(){


        $("#operate-popup").modal("hide");
        var treeObj = $.fn.zTree.getZTreeObj("elements-tree");
        console.log(treeObj)
        var node = treeObj.getNodeByParam("id",1,null);
        // open node
        treeObj.expandNode(node,true,true,true );
        var vehicleNode = treeObj.getNodeByParam("elemId",$(this).attr("vehicle-uuid"),null)
        // select node
        treeObj.selectNode(vehicleNode);

        onClick(event, "elements-tree", vehicleNode, true);
    });

})


// 查询所有场景（id,name,status）
var getSceneBaseList = function() {
    $.ajax({
        type: "GET",
        url: "/rest/scenes/profile",
        dataType: "json",
        cache: false,//false时，会自动添加时间戳
        //timeout: 1000,
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
                if(window.localStorage && localStorage.getItem("currentScene")){
                    var currentScene = localStorage.getItem("currentScene");
                    if(currentScene == obj.id) {
                        trContent += "<td><span><input type='radio' name='selectId' id='selectId' checked='check' value='"+obj.id+"'></span></td>";
                    } else {
                        trContent += "<td><span><input type='radio' name='selectId' id='selectId' value='"+obj.id+"'></span></td>";
                    }
                } else {
                    trContent += "<td><span><input type='radio' name='selectId' id='selectId' value='"+obj.id+"'></span></td>";

                }
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
        //timeout: 1000,
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
    //var rect1 = document.getElementById("rect1");
    //var rect2 = document.getElementById("rect2");
    //if(flg == 1) {
    //    rect1.setAttribute("fill", "url(#grid1)");
    //    rect2.setAttribute("fill", "url(#grid2)");
    //} else {
    //    rect1.setAttribute("fill", "none");
    //    rect2.setAttribute("fill", "none");
    //}

    for (var p in lineh) {
        lineh[p].opacity = flg ? 0 : 0.3;
    }


    for (var p in linew) {
        linew[p].opacity = flg ? 0 : 0.3;
    }
    //two.update();
};
var showText = function (flag) {
    window.tcsDraw.canvas.showText();
};


// get data for tree
var getSceneContent = function (id) {
    $.ajax({
        type: "GET",
        url: "/web/scenes/"+id,
        dataType: "json",
        cache: false,//false时，会自动添加时间戳
        //timeout: 1000,
        success: function (data) {
            if(data){
               // buildTree(data);
                //disconnect();
                //connect();
                // 存到本地
                if(supportLocalStorage()){
                    localStorage.setItem("currentScene", data.id);
                    localStorage.setItem("updateTime", data.updated_at);
                    localStorage.setItem("sceneJson", JSON.stringify(data));
                    localStorage.setItem("fSceneJson", JSON.stringify(fJson));
                }
                disconnect();
                connect();
                endingLoading();

                //window.tcsDraw.canvas.buildSceneEditor(data);
                window.tcsDraw.loadScene(data, true);
            }

        },
        error: function (xhr) {
            console.log("xhr")
            console.log(xhr)
            alert(xhr.responseText);
            endingLoading();
        }
    });
}


var buildTree = function(data) {

    var eleNodes=[];
    //locations --> attached_links
    var initElementsObj = ["vehicles", "points", "paths", "locations", "location_types", "attached_links", "static_routes"];
    var initBlocksObj = ["blocks"];

    // init vehicle list
    if(data[initElementsObj[0]]) {
        initVehicleList(data[initElementsObj[0]])
    }


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
        //更新右侧的列表
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
                fJson[attr.UUID] = attr;


            })

        }
        else if (elem == 'attached_links') {
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
                                fJson[attr.UUID] = attr;
                        });
                    }
                )

            }
        }
        else {
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
                    fJson[attr.UUID] = attr;

                })
            }

        }

    }
    //console.log(fJson)
    //console.log(eleNodes)
    $.fn.zTree.init($("#elements-tree"), setting, eleNodes);

    var bloNodes =[];
    // blocks tree   diff with others
    var elem = initBlocksObj[0];
    var array = data[elem];

    if(array) {
        // iterator blocks elements
        $.each(array, function (idx, attr) {

            var rootBlocks = { id:1, pId:0, name:'Blocks', click:false, title:'Blocks',icon:"../images/block.18x18.png", open:false, elemId:''};
            rootBlocks.id = idx + 1;
            rootBlocks.elemId = attr.UUID;
            bloNodes.push(rootBlocks);

            if(attr.members && attr.members.length > 0) {
                for(var index in attr.members) {
                    var uuid = attr.members[index]["UUID"];
                    var mem = fJson[uuid];
                    var blockObj = {id: 0, pId: 0, name: "", title: "", click: true, icon: "", icon:"../images/block.18x18.png", open: false, elemId: 0};
                    blockObj.id = rootBlocks.id + "" + mem.id;
                    blockObj.pId = rootBlocks.id;
                    blockObj.name = mem.name;
                    blockObj.title = mem.name;
                    blockObj.click = true;
                    blockObj.icon = "./images/" + "block" + ".18x18.png";
                    blockObj.elemId = mem.UUID;
                    bloNodes.push(blockObj);
                    fJson[attr.UUID] = mem;
                }

            }

        })
        }
    $.fn.zTree.init($("#blocks-tree"), setting, bloNodes);
}


/**
 * 如果本地有缓存，则验证加载数据，若没有数据，继续
 */
//var initTree = function(){
//    if(supportLocalStorage()) {
//        var currentSceneId = localStorage.getItem("currentScene");
//        if(currentSceneId) {
//            checkSceneStatusAndUpdateTimeById(currentSceneId)
//
//        } else {
//            endingLoading();
//            return
//        }
//    } else {
//        endingLoading();
//        return
//    }
//
//}

//init vehicle list
var initVehicleList = function(vehicleArray){
    var vehicleListStr ="";
    for(var i = 0 ; i < vehicleArray.length ; i ++ ) {
        vehicleListStr += "<div class='col-xs-6 col-sm-4 col-md-3 col-lg-2'>";
        vehicleListStr += "<div class='vehicle' vehicle-uuid='" + vehicleArray[i].UUID + "'>";
        vehicleListStr += "<div class='name'>" + vehicleArray[i].name.slice(-2) + "</div>";
        var el = vehicleArray[i].energy_level;
        switch (el){
            case el = 0:
                el = "000-2";
                break;
            case el > 0 && el <= 30:
                el = "030-2";
                break;
            case el > 0 && el <= 30:
                el = "030-2";
                break;
            case el > 30 && el <= 60:
                el = "060-2";
                break;
            case el > 60 && el <= 100:
                el = "100-2";
                break;
            default: el = "100-2";

        }
        vehicleListStr += " <div class='battery'><img src='/images/battery/battery-"+el+".png' /></div>";
        vehicleListStr += "<div class='status'>UNKNOWN</div>";
        //把Dispatch Vehicle放在外面
        vehicleListStr += "<div class='dispatch'><button type='button' class='btn btn-primary btn-sm' onclick=dispatchVehicle('"+vehicleArray[i].UUID+"')>Dispatch Vehicle </button></div>";
        vehicleListStr += "</div>";
        vehicleListStr += "</div>";
    }

    $(".left-container .bottom-panel .bottom-panel-list").html(vehicleListStr)

}

var pro, className = "dark";

function beforeClick(treeId, treeNode, clickFlag) {
    className = (className === "dark" ? "" : "dark");

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
    var treeNodeInfo = fJson[uuid];
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
                    $.each(value, function(id, obj){
                        proStr += obj.name+":"+obj.value + " ";
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

     showAttr(str);
    window.tcsDraw.canvas.showElemInScene(uuid);
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

var supportLocalStorage = function(){
    if(window.localStorage) {
        return true
    } else {
        alert("this browser does not support localStorage!")
        return
    }

}


function startLoading(){
    var _PageHeight = document.documentElement.clientHeight;
    var _PageWidth = document.documentElement.clientWidth;
    //计算loading框距离顶部和左部的距离（loading框的宽度为215px，高度为61px）
    var _LoadingTop = _PageHeight > 61 ? (_PageHeight - 61) / 2 : 0;
    var _LoadingLeft = _PageWidth > 215 ? (_PageWidth - 215) / 2 : 0;
    $("#loadingDiv").css("display", "block");
    $("#loadingDiv").css("height", _PageHeight);
    $("#loadingDiv > div").css("left", _LoadingLeft);
    $("#loadingDiv > div").css("top", _LoadingTop);

}

function endingLoading(){
    $("#loadingDiv").css("display", "none");
}


