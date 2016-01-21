<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/plugin/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/default.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/operating.css">
    <link rel="stylesheet" href="${ctxStatic}/plugin/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/plugin/bootSideMenu/css/BootSideMenu.css" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/tcs-editor.css" type="text/css">
    <title>Operating Model</title>
</head>
<body>
<header>
    <!--<h1>AVG Modelling-Mode<a href="operating.html">[operating]</a></h1>-->
    <div class="mj-logo"><img src="../images/mj-logo.png" alt="" class=""></div>
    <div class="title">AGV Modelling
        <%--TODO:如果有modeling的功能，需要改，取消下面注释--%>
        <%--<a href="operating.html">[operating]</a>--%>
    </div>
    <div class="sys-info">
        <!--<p class="sence"><a href="javascript:;">选择场景</a>|</p>-->
        <div class="scene">

            <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#sceneselect">
                选择场景
            </button>
            <div class="dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="model-switch" data-toggle="dropdown">
                    Operating
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" aria-labelledby="model-switch">
                    <li><a href="./modelling">Modelling</a></li>
                </ul>
            </div>
        </div>
        <div class="userinfo">
			<span class="label label-info">
			<sec:authorize ifAllGranted="ROLE_ADMIN">
                管理员：
            </sec:authorize>
			<sec:authorize ifNotGranted="ROLE_ADMIN">
                用户：
            </sec:authorize>
			<sec:authentication property="principal.username"/></span>
            <a href="/logout">注销</a>
        </div>
    </div>
</header>

<div class="container-fluid">

    <div class="col-md-9 left-container">
        <div class="top-panel">
            <div class="top-panel-view">
                <div id="tcs-editor" class="tcs-editor">
                    <div id="rulers">
                        <div id="ruler_corner"></div>
                        <div id="ruler_x">
                            <div>
                                <canvas height="150"></canvas>
                            </div>
                        </div>
                        <div id="ruler_y">
                            <div>
                                <canvas width="150"></canvas>
                            </div>
                        </div>
                    </div>
                    <div class="workarea" id="workarea">
                        <div id="tcs-canvas" style="position:relative;min-width: 800px;width: 100%;height: 100%;">
                        </div>
                    </div>
                </div>
                <div class="tcs-bottom">
                    <div class="zoom-label">
                        <input id="zoom" size="3" value="100%" type="text" readonly="readonly"/>

                        <select id="zoom_select">
                            <option value="40">40%</option>
                            <option value="50">50%</option>
                            <option value="60">60%</option>
                            <option value="70">70%</option>
                            <option value="80">80%</option>
                            <option value="90">90%</option>
                            <option value="100" selected="selected">100%</option>
                            <option value="110">110%</option>
                            <option value="120">120%</option>
                            <option value="150">150%</option>
                            <option value="170">170%</option>
                            <option value="200">200%</option>
                            <option value="220">220%</option>
                            <option value="250">250%</option>
                        </select>
                    </div>
                    <div class="reset-zoom"><img src="${ctxStatic}/images/zoom-fit.png" title=""></div>
                    <div class="show-splits selected"><img src="${ctxStatic}/images/view-split.png" title=""></div>
                    <div class="show-labels"><img src="${ctxStatic}/images/document-page-setup.16x16.png" title="">
                    </div>
                    <div class="show-comment"><img src="${ctxStatic}/images/comment-add.16.png" title=""></div>
                    <div class="show-blocks"><img src="${ctxStatic}/images/block.18x18.png" title=""></div>
                    <div class="show-static-routes"><img src="${ctxStatic}/images/staticRoute.18x18.png" title=""></div>
						<span>
							<button id="connect" onclick="connect();" style="display: none">Connect</button>
							<button id="disconnect" onclick="disconnect();" disabled="" style="display: none">
                                Disconnect
                            </button>
						</span>
                    <span class="show-position">x:0 y:0</span>
                </div>
                <div class="top-panel-to">
                    <div class="table-head">
                        <table>
                            <thead>
                            <tr>
                                <th>UUID</th>
                                <th>Type</th>
                                <th>Execute Vehicle</th>
                                <th>Order State</th>
                                <th>Column5</th>
                                <th>Column6</th>
                                <th>Column7</th>


                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div class="table-body">
                        <table>
                            <!-- 针对每一行设定样式 -->
                            <!-- <colgroup>
                            <col style="width:10%;" />
                            </colgroup> -->
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="top-panel-operation">
                <ul class="operation-list">
                    <li>
                        <img src="${ctxStatic}/images/select-2.png" alt="1111"></li>
                    <li>
                        <img src="${ctxStatic}/images/cursor-opened-hand.png" alt=""></li>
                    <li>
                        <img src="${ctxStatic}/images/groups.png" alt=""></li>
                    <li>
                        <img src="${ctxStatic}/images/create-order.22.png" id="create-torder" alt=""></li>
                    <li>
                        <img src="${ctxStatic}/images/find-vehicle.22.png" id="find-vehicle" alt="find-vehicle"></li>
                    <li>
                        <img src="${ctxStatic}/images/pause-vehicles.22.png" alt=""></li>
                    <li>
                        <img src="/images/transport-order.png" class="to" id="torder-list" alt="create transport order">
                    </li>
                </ul>
            </div>

        </div>
        <div class="bottom-button"></div>
        <div class="bottom-panel">
            <div class="bottom-panel-title">小车列表</div>
            <div class="bottom-panel-list">

            </div>

        </div>
    </div>
    <div class="col-md-3 right-container">
        <div class="elements-blocks">

            <ul class="nav nav-tabs nav-justified elements-blocks-title">
                <li role="presentation" class="active">
                    <a href="#">Elements</a>
                </li>
                <li role="presentation">
                    <a href="#">Blocks</a>
                </li>
            </ul>
            <div class="elements-content">

                <ul id="elements-tree" class="ztree"></ul>
            </div>
            <div class="blocks-content">

                <ul id="blocks-tree" class="ztree"></ul>
            </div>
        </div>
        <div class="side-bottom-button"></div>
        <div class="properties">
            <div class="table-head">
                <table>
                    <thead>
                    <tr>
                        <th>Attribute</th>
                        <th>Value</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="table-body">
                <table>
                    <tbody>
                    </tbody>
                </table>
            </div>

        </div>
        <div class="side-button">
            <!--<span class="glyphicon glyphicon-chevron-right" style="display: block;">&nbsp;</span>-->
            <!--<span class="glyphicon glyphicon-chevron-left" style="display: inline-block;">&nbsp;</span>-->
        </div>
    </div>
</div>


<!-- show sceneselect Modal -->
<div class="modal fade" id="sceneselect" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">场景列表：</h4>
            </div>
            <div class="modal-body">

                <table class="table table-bordered scenelist">
                    <col width="10%"/>
                    <col width="40%"/>
                    <col width="18%"/>
                    <col width="18%"/>
                    <col width="14%"/>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>NAME</th>
                        <th>STATUS</th>
                        <th>OPERATE</th>
                        <th>SELECT</th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary submit">确定</button>
            </div>
        </div>
    </div>
</div>

<!-- vehcile detail Modal -->
<div class="modal fade" id="vehicleDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="vehicle-title">小车实时状态</h4>
            </div>
            <div class="modal-body" style="overflow-wrap: break-word">
                loading...
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <!--<button type="button" class="btn btn-primary">Save changes</button>-->
            </div>
        </div>
    </div>
</div>

<!-- find vehicle Modal -->
<div class="modal fade" id="operate-popup" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="title"></h4>
            </div>
            <div class="modal-body" style="overflow-wrap: break-word">
                <div class="dropdown">
                    <button class="btn btn-default dropdown-toggle" type="button" id="vehicle-dropdownMenu"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                        请选择小车
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="vehicle-dropdownMenu">
                        <li><a href="#">--</a></li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary submit">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>

            </div>
        </div>
    </div>
</div>


<!-- create transportorder Modal -->
<div class="modal fade" id="create-to" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="crate-to-title">创建调拨单</h4>
            </div>
            <div class="modal-body" style="overflow-wrap: break-word">

                <div class="panel panel-default">
                    <!-- Default panel contents -->
                    <div class="panel-heading">Drive Orders</div>

                    <!-- Table -->
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>location</th>
                            <th>action</th>
                        </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
                <button type="button" class="btn btn-default location-list" id="show-location-list">添加</button>
            </div>
            <div class="modal-body" style="overflow-wrap: break-word">
                <div class="panel panel-default">
                    <div class="panel-heading">Deadline</div>
                    <label for="date">Date:</label><input id="date" type="text">
                    <label for="time">Time:</label><input id="time" type="text">
                </div>
            </div>
            <div class="modal-body" style="overflow-wrap: break-word">
                <div class="panel panel-default">
                    <div class="panel-heading">Vehicle</div>
                    <div class="dropdown">
                        <button class="btn btn-default dropdown-toggle" type="button" id="vehicle-dropdownMenu2"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                            请选择小车
                            <span class="caret"></span>
                        </button>
                        <ul id="torderVehicle" class="dropdown-menu" aria-labelledby="vehicle-dropdownMenu2">
                            <li><a href="#">--</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary submit">创建</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <!--<button type="button" class="btn btn-primary">Save changes</button>-->
            </div>
        </div>
    </div>
</div>

<!-- create transportorder Modal -->
<div class="modal fade" id="select-location" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="select-location-title">选择工位与状态</h4>
            </div>
            <div class="modal-body">
                <div class="dropdown location">
                    <button class="btn btn-default dropdown-toggle" type="button" id="location-dropdownMenu"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                        请选择工位
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="location-dropdownMenu">
                        <li><a href="#">--</a></li>

                    </ul>
                </div>
                <div class="dropdown state">
                    <button class="btn btn-default dropdown-toggle" type="button" id="location-state-dropdownMenu"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                        <%--把“请选择状态”改为“请选择操作”--%>
                        请选择操作
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="state-dropdownMenu">
                        <li><a href="#">--</a></li>

                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary submit">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<!-- sidebar 侧边栏 -->
<div id="left-sidebar">
    <div class="content">
			<span class="simulation ">
				<p class="name">仿真因子：</p>
				<input type="number" name="num" value="0" min="0" max="10"/>
				<input type="button" name="set" value="设定"/>
			</span>

        <div class="line">
            <hr>
        </div>
        <div class="adapter">
            <div class="table-head">
                <table>
                    <thead>
                    <col width="100px"/>
                    <col width="65px"/>
                    <col width="140px"/>
                    <col width="30px"/>
                    <col width="75px"/>
                    <tr>
                        <th>名称</th>
                        <th>状态</th>
                        <th>适配器</th>
                        <th>启动</th>
                        <th>位置</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="table-body">
                <table>
                    <col width="100px"/>
                    <col width="65px"/>
                    <col width="140px"/>
                    <col width="30px"/>
                    <col width="75px"/>
                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="settinginfo">
            <div class="">
                <p>IP ：</p>
                <input type="text" name="ip" class="ip" value="127.0.0.1"/>
            </div>
            <div class="port">
                <p>Port ：</p>
                <input type="text" name="port" class="port" value="8080"/>
            </div>
            <div class="">
                <p>SlaveId ：</p>
                <input type="text" name="slaveId" class="slaveId" value="502"/>
            </div>
            <div class="">
                <input type="hidden" name="mark" value=""/>
                <input type="button" name="reset" class="slaveId" value="reset"/>
                <input type="button" name="save" class="slaveId" value="save"/>
            </div>
            <div class="tips" style="color:red"></div>
        </div>

    </div>
</div>
<div id="loadingDiv">
    <div>页面加载中，请等待...</div>
</div>

<script type="text/javascript" src="../js/jquery-1.11.3.js"></script>
<script src="../plugin/sockjs/sockjs.js"></script>
<script src="../plugin/stomp/stomp.min.js"></script>
<script type="text/javascript" src="../plugin/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="../plugin/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../plugin/bootSideMenu/js/BootSideMenu.js"></script>
<script type="text/javascript" src="../plugin/draw2d/underscore.js"></script>
<script type="text/javascript" src="../plugin/draw2d/backbone.js"></script>
<script type="text/javascript" src="../plugin/draw2d/events.js"></script>
<script type="text/javascript" src="../plugin/draw2d/two_dev.js"></script>
<script type="text/javascript" src="../js/init.js"></script>
<script type="text/javascript" src="../js/mousewheel.js"></script>
<script type="text/javascript" src="../js/mjtcs-elem.js"></script>
<script type="text/javascript" src="../js/mjtcs-elem-point.js"></script>
<script type="text/javascript" src="../js/mjtcs-elem-path.js"></script>
<script type="text/javascript" src="../js/mjtcs-elem-location.js"></script>
<script type="text/javascript" src="../js/mjtcs-elem-link.js"></script>
<script type="text/javascript" src="../js/mjtcs-elem-vehicle.js"></script>
<script type="text/javascript" src="../js/has.js"></script>
<script type="text/javascript" src="../js/tcs-canvas.js"></script>
<script type="text/javascript" src="../js/tcs-editor.js"></script>
<script type="text/javascript" src="../js/operating.js"></script>
<!--<script type="text/javascript" src="../js/mjtcs-modelling.js"></script>-->
<script type="text/javascript">
    $(document).ready(function () {
        $('#left-sidebar').BootSideMenu({side: "left", autoClose: true});
        // 保存数据
        $("#left-sidebar .content .settinginfo div > input[name=save]").click(function () {
            var destinations = [];
            var trContent = $("#left-sidebar .content .adapter .table-body table tbody tr");
            for (var i = 0; i < trContent.length; i++) {
                var destination = {
                    "vehicle_uuid": null,
                    "adapter_name": null,
                    initial_position_uuid: "",
                    enable: "false"
                }
                destination.vehicle_uuid = $(trContent[i]).find("td:eq(0) a").attr("vehicle-uuid");
                destination.adapter_name = $(trContent[i]).find(".adapterSelect").val();
                destination.initial_position_uuid = $(trContent[i]).find(".pointSelect").val();
                destination.enable = $(trContent[i]).find("input[type=checkbox]").prop("checked");
                destinations.push(destination);
            }

            attachAdapter(destinations);
        });
    });


    var wsUrl = "http://localhost:8080/stomp";
    var stompClient = null;

    function setConnected(connected) {
        document.getElementById('connect').disabled = connected;
        document.getElementById('disconnect').disabled = !connected;
    }


    var ACTIONS = {
        // Scene
        SCENE_PROFILE: "SCENE_PROFILE",
        SCENE_CREATE: "SCENE_CREATE",
        SCENE_FIND: "SCENE_FIND",
        SCENE_DELETE: "SCENE_DELETE",
        SCENE_START: "SCENE_START",
        SCENE_STOP: "SCENE_STOP",
        SCENE_SPECIFIC_PROFILE: "SCENE_SPECIFIC_PROFILE",
        // Transport Order
        TO_NEW: "TO_NEW",
        TO_WITHDRAW: "TO_WITHDRAW",
        // Vehicle
        VEHICLE_PROFILE: "VEHICLE_PROFILE",
        VEHICLE_ADAPTER_ATTACH: "VEHICLE_ADAPTER_ATTACH",
        VEHICLE_DISPATCH: "VEHICLE_DISPATCH",
        VEHICLE_STOP_TO: "VEHICLE_STOP_TO",
        VEHICLE_PARK: "VEHICLE_PARK"
    };
    var subscriberMap = new Map();// sceneId -> subscriberLists
    var sceneId = localStorage.getItem("currentScene");

    var REQ_UUID = uuid();


    function connect() {
        var socket = new SockJS(wsUrl);
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
            //场景改变后，更新调拨单状态和页面下方小车的状态
            sceneIdChange();
            getSceneProfile();
            getVehicleProfile();

            stompClient.subscribe("/user/topic/actions/response", function (message) {
                var response = JSON.parse(message.body);
                // 返回id对应场景信息
                if (response.uuid == REQ_UUID + "_" + ACTIONS.SCENE_SPECIFIC_PROFILE) {
                    if (supportLocalStorage()) {
                        var currentSceneId = localStorage.getItem("currentScene");
                        if (currentSceneId) {
                            // 根据id获取场景的状态以及更新时间
                            initSceneByStatusAndUpdateTime(response)
                        }

                    }
                    // 获取小车列表适配信息
                } else if (response.uuid == REQ_UUID + "_" + ACTIONS.VEHICLE_PROFILE) {
                    if (response.body) {
                        initVehicleAdapterList(response.body);
                    }

                } else if (response.uuid == REQ_UUID + "_" + ACTIONS.VEHICLE_ADAPTER_ATTACH) {
                    $("#left-sidebar .content .settinginfo .tips").text("has submit!");
                }

                endingLoading();

            });

        });
    }

    function sceneIdChange() {
        //场景改变后要更改sceneId这个全局变量
        if (supportLocalStorage()) {
            var currentSceneId = localStorage.getItem("currentScene");
            if (currentSceneId) {
                sceneId = currentSceneId;
            }

        }
        // update subscribers for the new scene
        if (subscriberMap.has(sceneId)) {
            var prevSubscribers = subscriberMap.get(sceneId);
            for (var i = 0; i < prevSubscribers.length; i++) {
                prevSubscribers[i].unsubscribe();
            }
            subscriberMap.clear();
        }

        // add new subscribers
        var transportSubscriber = stompClient.subscribe("/topic/status/scenes/" + sceneId + "/torders",
                function (message) {
//                    console.log("在showTOrderMessage前面显示message:" + message);
                    showTOrderMessage(JSON.parse(message.body))
//					  document.getElementById('transportStatus').innerHTML = "<b>TransportOrder:</b><br>" + message.body;
                });

        var vehicleSubscriber = stompClient.subscribe("/topic/status/scenes/" + sceneId + "/vehicles",
                function (message) {
                    var response = JSON.parse(message.body);
                    showVehicleMessage(JSON.parse(message.body));
                    window.tcsDraw.canvas.parseVehiclePosition(response);
//					  document.getElementById("vehicleStatus").innerHTML = "<b>VehicleStatus:</b><br>" + message.body;

                });

        subscriberMap.set(sceneId, [transportSubscriber, vehicleSubscriber]);
    }
    ;

    function disconnect() {
        if (stompClient != null) {
            stompClient.disconnect();
        }
        setConnected(false);
        console.log("Disconnected");
    }

    function showTOrderMessage(message) {

        // 移除上次完成的工单
        $(".top-panel-to .table-body tbody .FINISHED").remove();

        // 添加更新的工单
        console.log("showTOrderMessage里面的message:" + message);
//		  var toContent = "<tr class='"+message.order_state+"'>";
//		  toContent += "<td>" + message.uuid + "</td>";
//		  toContent += "<td>" + message.type + "</td>";
//		  toContent += "<td>" + message.executing_vehicle + "</td>";
//		  toContent += "<td>" + message.order_state + "</td>";
//		  toContent += "<td>Column5</td>";
//		  toContent += "<td>Column6</td>";
//		  toContent += "<td>"+new Date().getTime()+"</td>";
//		  toContent += "</tr>";
//		  $(".top-panel-to .table-body tbody").append(toContent);
        hasUUid(message);
        $(".left-container .top-panel .top-panel-to .table-body").scrollTop(2000000);

    }
    //判断调拨单是否存在，如果存在则修改他的值，如果不存在则新建调拨单
    function hasUUid(message) {
        var trList = $(".top-panel-to .table-body tbody").children("tr");
        var has = false;
        console.log("message.uuid为：" + message.UUID);
        for (var i = 0, j = trList.length; i < j; i++) {
            var tdText = trList.eq(i).find("td").eq(0).text();
            if (tdText == message.UUID) {
                has = true;
                trList.eq(i).find("td").eq(1).text(message.type);
                trList.eq(i).find("td").eq(2).text(message.executing_vehicle);
                trList.eq(i).find("td").eq(3).text(message.order_state);
                trList.eq(i).find("td").eq(4).text("Column5");
                trList.eq(i).find("td").eq(5).text("Column6");
                trList.eq(i).find("td").eq(6).text(new Date().getTime());
                return;
            }
        }
        if (has == false) {
            var toContent = "<tr class='" + message.order_state + "'>";
            toContent += "<td>" + message.UUID + "</td>";
            toContent += "<td>" + message.type + "</td>";
            toContent += "<td>" + message.executing_vehicle + "</td>";
            toContent += "<td>" + message.order_state + "</td>";
            toContent += "<td>Column5</td>";
            toContent += "<td>Column6</td>";
            toContent += "<td>" + new Date().getTime() + "</td>";
            toContent += "</tr>";
            $(".top-panel-to .table-body tbody").append(toContent);
        }
    }

    function showVehicleMessage(message) {
        $(".bottom-panel-list [vehicle-uuid='" + message.uuid + "'] .status").text(message.state);
    }


    // 获取小车列表适配信息
    function getVehicleProfile() {
        var request = {
            "uuid": REQ_UUID + "_" + ACTIONS.VEHICLE_PROFILE, // SPECIAL !!!
            "action_code": ACTIONS.VEHICLE_PROFILE,
            "body": {}
        };

        stompClient.send("/app/topic/actions/scenes/" + Number(sceneId) + "/vehicles/request", {},
                JSON.stringify(request));
    }

    // 创建工单
    function createTransport(deadline,intended_vehicle,location, operation) {
        var transport = {
            "uuid": REQ_UUID,
            "action_code": ACTIONS.TO_NEW,
            "body": {
                "uuid": REQ_UUID,
                "deadline": deadline,
                "intended_vehicle": intended_vehicle,
                "destinations": [
                    {
                        "location_uuid": location,
                        "operation": operation
                    }
                ],
                "dependencies": []
            }
        };

        stompClient.send("/app/topic/actions/scenes/" + Number(sceneId) + "/torders/request", {}, JSON.stringify(transport));
    }

    // 获取当前场景状态和更新时间等信息
    function getSceneProfile() {

        var sceneProfile = {
            "uuid": REQ_UUID + "_" + ACTIONS.SCENE_SPECIFIC_PROFILE,
            "action_code": ACTIONS.SCENE_SPECIFIC_PROFILE,
            "body": {}
        };

        stompClient.send("/app/topic/actions/scenes/" + Number(sceneId) + "/request", {},
                JSON.stringify(sceneProfile));
    }

    // 根据id获取场景的状态以及更新时间
    function initSceneByStatusAndUpdateTime(response) {
//		  alert(response.body.updated_at +"=="+ localStorage.getItem("updateTime"))

        if (response.status != "stopped" && response.body.updated_at == localStorage.getItem("updateTime")) {
            var sceneJson = JSON.parse(localStorage.getItem("sceneJson"))
            $("header .title").text(sceneJson.name);
            buildTree(sceneJson);
        }

    }

    // 初始化小车以及适配列表
    function initVehicleAdapterList(response) {

        var trContent = "";
        for (var i = 0; i < response.length; i++) {
            trContent += "<tr>";
//			  trContent += "<td><a href='javascript:;' vehicle-uuid='" + response[i].uuid + "'>" + response[i].name + "</a></td>";
//			  trContent += "<td title='" + response[i].state + "'>" + response[i].state + "</td>";
            trContent += "<td><a href='javascript:;' vehicle-uuid='" + response[i]["uuid"] + "'>" + response[i]["name"] + "</a></td>";
            trContent += "<td title='" + response[i].state + "'>" + response[i].state + "</td>";
            var adapterContent = "<select class='adapterSelect'>";
            adapterContent += "<option value=''>请选择</option>";
            $.each(response[i]["adapters"], function (n, value) {
                if (value == response[i].adapter) {
                    adapterContent += "<option value='" + value + "' selected='selected'>" + value + "</option>";
                } else {
                    adapterContent += "<option value='" + value + "'>" + value + "</option>";
                }
            });

            adapterContent += "</select>";
            trContent += "<td>" + adapterContent + "</td>";
            if (response[i].enable == "true") {
                trContent += "<td><input type='checkbox' class='startflg' checked='checked' value='true'/></td>";
            } else {
                trContent += "<td><input type='checkbox' class='startflg' value='false'/></td>";
            }

            var pointArray = JSON.parse(localStorage.getItem("sceneJson"))["points"];
            var pointSelectContent = "<select class='pointSelect'>";
            pointSelectContent += "<option value=''>请选择</option>";
            $.each(pointArray, function (n, value) {
                if (value.UUID == response[i].position_uuid) {
                    pointSelectContent += "<option value='" + value.UUID + "' selected='selected>" + value.name + "</option>";
                } else {
                    pointSelectContent += "<option value='" + value.UUID + "'>" + value.name + "</option>";
                }

            });
            pointSelectContent += "</select>";

            trContent += "<td>" + pointSelectContent + "</td>";
            trContent += "</tr>";
        }
        $("#left-sidebar .content .adapter .table-body tbody").html("");
//		$("#left-sidebar .content .adapter .table-body tbody").empty();
        $("#left-sidebar .content .adapter .table-body tbody").append(trContent);
    }

    function attachAdapter(content) {
        var request = {
            "uuid": REQ_UUID + "_" + ACTIONS.VEHICLE_ADAPTER_ATTACH,
            "action_code": ACTIONS.VEHICLE_ADAPTER_ATTACH,
            "body": content
        };
        stompClient.send("/app/topic/actions/scenes/" + Number(sceneId) + "/vehicles/request", {},
                JSON.stringify(request));

    }


    function dispatchVehicle(uuid) {
        var request = {
            "uuid": REQ_UUID,
            "action_code": ACTIONS.VEHICLE_DISPATCH,
            "body": uuid
        };

        stompClient.send("/app/topic/actions/scenes/" + Number(sceneId) + "/vehicles/request", {},
                JSON.stringify(request));
    }

    function uuid() {
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";

        var uuid = s.join("");
        return uuid;
    }

</script>
</body>
</html>