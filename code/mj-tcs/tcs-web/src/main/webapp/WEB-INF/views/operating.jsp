<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/taglib.jsp"%>
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
	<div class="title">AVG Modelling<a href="operating.html">[operating]</a></div>
	<div class="sys-info">
		<!--<p class="sence"><a href="javascript:;">选择场景</a>|</p>-->
		<div class="scene">

			<button type="button" class="btn btn-primary btn-sm" data-toggle="modal"  data-target="#sceneselect">
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
			<span class="label label-info">用户：admin</span>
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
							<input id="zoom" size="3" value="100%" type="text" readonly="readonly" />

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
						<div class="show-labels"><img src="${ctxStatic}/images/document-page-setup.16x16.png" title=""></div>
						<div class="show-comment"><img src="${ctxStatic}/images/comment-add.16.png" title=""></div>
						<div class="show-blocks"><img src="${ctxStatic}/images/block.18x18.png" title=""></div>
						<div class="show-static-routes"><img src="${ctxStatic}/images/staticRoute.18x18.png" title=""></div>

						<span class="show-position">x:0 y:0</span>
					</div>
				<div class="top-panel-to">
					<div class="table-head">
						<table>
						<thead>
							<tr>
								<th>Column1</th>
								<th>Column2</th>
								<th>Column3</th>
								<th>Column4</th>
								<th>Column5</th>
								<th>Column6</th>
								<th>Column7</th>
								<th>Column8</th>
								<th>Column9</th>
								<th>Column10</th>
								
							</tr>
						</thead>
					</table>
				</div>
				<div class="table-body">
					<table >
						<!-- 针对每一行设定样式 -->
						<!-- <colgroup>						
						<col style="width:10%;" />					
						</colgroup> -->
					<tbody>
						<tr>
							<td>1</td>
							<td>column</td><td>column</td><td>column</td><td>column</td><td>column</td>
							<td>column</td><td>column</td><td>column</td><td>column</td>
						</tr>
						<tr>
							<td>2</td>
							<td>column</td><td>column</td><td>column</td><td>column</td><td>column</td>
							<td>column</td><td>column</td><td>column</td><td>column</td>
						</tr>
						<tr>
							<td>3</td>
							<td>我只是用来测试的</td>
						</tr>
						<tr>
							<td>4</td>
							<td>我只是用来测试的</td>
						</tr>
						<tr>
							<td>5</td>
							<td>我只是用来测试的</td>
						</tr>
						<tr>
							<td>6</td>
							<td>我只是用来测试的</td>
						</tr>
						<tr>
							<td>7</td>
							<td>我只是用来测试的</td>
						</tr>
						<tr>
							<td>8</td>
							<td>我只是用来测试的</td>
						</tr>
						<tr>
							<td>9</td>
							<td>我只是用来测试的</td>
						</tr>
						<tr>
							<td>10</td>
							<td>我只是用来测试的</td>
						</tr>
						<tr>
							<td>11</td>
							<td>我只是用来测试的</td>
						</tr>
						<tr>
							<td>12</td>
							<td>我只是用来测试的</td>
						</tr>
						<tr>
							<td>13</td>
							<td>我只是用来测试的</td>
						</tr>
						<tr>
							<td>14</td>
							<td>我只是用来测试的</td>
						</tr>
						
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
							<img src="${ctxStatic}/images/create-order.22.png" alt=""></li>
						<li>
							<img src="${ctxStatic}/images/find-vehicle.22.png" id="find-vehicle" alt="find-vehicle"></li>
						<li>
							<img src="${ctxStatic}/images/pause-vehicles.22.png" alt=""></li>
						<li>
							<img src="${ctxStatic}/images/transport-order.png" class="to" alt=""></li>
					</ul>
				</div>

			</div>
			<div class="bottom-button"></div>
			<div class="bottom-panel">
				<div class="bottom-panel-title">小车列表</div>
				<div class="bottom-panel-list">
					<%--<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">--%>
						<%--<div class="vehicle" data-toggle="modal" data-target="#myModal">--%>
							<%--<div class="name">Vehicle</div>--%>
							<%--<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>--%>
							<%--<div class="status">runing</div>--%>
						<%--</div>--%>
					<%--</div>--%>

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
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">场景列表：</h4>
				</div>
				<div class="modal-body">

					<table class="table table-bordered scenelist">
						<col width="10%" />
						<col width="40%" />
						<col width="18%" />
						<col width="18%" />
						<col width="14%" />
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
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="">小车实时状态</h4>
				</div>
				<div class="modal-body" style="overflow-wrap: break-word">
					...
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
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="title">dd</h4>
				</div>
				<div class="modal-body" style="overflow-wrap: break-word">
					<div class="dropdown">
						<button class="btn btn-default dropdown-toggle" type="button" id="vehicle-dropdownMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
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

	<!-- sidebar 侧边栏 -->
	<div id="left-sidebar">
		<div class="content">
			<span class="simulation ">
				<p class="name">仿真因子：</p>
				<input type="number" name="num" value="0" min="0" max="10" />
				<input type="button" name="set" value="设定" />
			</span>
			<div class="line"><hr></div>
			<div class="adapter">
				<div class="table-head">
					<table>
					<thead>
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
				<tbody>
					<tr>
						<td><a href="javascript:;">小车</a></td>
						<td title="stop">stop</td>
						<td>
							<select>
								<option value="">请选择</option>
								<option value="mo">adapter1</option>
								<option value="lo">adapter2</option>
							</select>
						</td>
						<td><input type="checkbox" name="1" class="startflg" value="1"/></td>
						<td>name</td>
					</tr>
					<tr>
						<td><a href="javascript:;">叉车</a></td>
						<td>stop</td>
						<td>
							<select>
								<option value="">请选择</option>
								<option value="mo">adapter1</option>
								<option value="lo">adapter2</option>
							</select>
						</td>
						<td><input type="checkbox" name="2" class="startflg" value="2"/></td>
						<td>location1</td>
					</tr>
					<tr>
						<td><a href="javascript:;">小车</a></td>
						<td>running</td>
						<td>
							<select>
								<option value="">请选择</option>
								<option value="mo">adapter1</option>
								<option value="lo">adapter2</option>
							</select>
						</td>
						<td><input type="checkbox" name="3" checked="" class="startflg" value="3"/></td>
						<td>location2</td>
					</tr>
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
	<div>页面加载中，请等待...	</div>
</div>

	<script type="text/javascript" src="../js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="../plugin/bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript" src="../plugin/zTree/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="../plugin/bootSideMenu/js/BootSideMenu.js"></script>
	<script type="text/javascript" src="../plugin/draw2d/underscore.js"></script>
	<script type="text/javascript" src="../plugin/draw2d/backbone.js"></script>
	<script type="text/javascript" src="../plugin/draw2d/events.js"></script>
	<script type="text/javascript" src="../plugin/draw2d/two_dev.js"></script>
	<script type="text/javascript" src="../js/init.js"></script>
	<script type="text/javascript" src="../js/mousewheel.js"></script>
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
	  $(document).ready(function(){
	      $('#left-sidebar').BootSideMenu({side:"left", autoClose:true});

		// --------------------------side menu script-------------------------------------------------------
		$("#left-sidebar .content .adapter .table-body table .startflg").on('click', function() {
			$("#left-sidebar .content .settinginfo .tips").html("");

			if($(this)[0].checked) {
				$("#left-sidebar .content .settinginfo").find("input").removeAttr("disabled");
				// setting value
				$("#left-sidebar .content .settinginfo").find("input[name=slaveId]").val($(this).val());
				$("#left-sidebar .content .settinginfo").find("input[name=mark]").val($(this).attr("name"));
			} else {
				$("#left-sidebar .content .settinginfo").find("input").attr('disabled', 'true');
			}

		});

		// 保存数据
		$("#left-sidebar .content .settinginfo div > input[name=save]").click(function() {
			$("#left-sidebar .content .settinginfo .tips").html("");

			var id = $(this).parent().find("input[name=mark]").val();
			var slaveId = $(this).parent().parent().find("input[name=slaveId]").val();
			console.log(slaveId)
			$("#left-sidebar .content .adapter").find("input[name="+id+"]").val(slaveId);

			$("#left-sidebar .content .settinginfo .tips").html("保存成功.....");

		});

		$("#left-sidebar .content .adapter .table-body table a").click(function() {
			$("#left-sidebar .content .settinginfo .tips").html("");
			// console.log($(this)[0]);
			// console.log($(this).parent().parent().find(".startflg")[0])
			var settinginfo = $("#left-sidebar .content .settinginfo");
			if($(this).parent().parent().find(".startflg")[0].checked){
				settinginfo.find("input").removeAttr("disabled");
				settinginfo.find("input[name=slaveId]").val($(this).parent().parent().find(".startflg").val());
				settinginfo.find("input[name=mark]").val($(this).parent().parent().find(".startflg").attr("name"));
			}

		});

	  });

	</script>
</body>
</html>