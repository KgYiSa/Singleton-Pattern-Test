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
			<span id="scenename">暂无</span>
			<!-- Modal -->
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
							<button type="button" class="btn btn-primary submmit">确定</button>
						</div>
					</div>
				</div>
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
							<div id="svgcanvas" style="position:relative;min-width: 800px;width: 100%;height: 100%;">
								<%--<svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" style="background-color: indianred">--%>
									<%--<defs>--%>
										<%--<!-- 用一个10*10方形路径填充 -->--%>
										<%--<pattern id="grid1" x="0" y="0" width="10" height="10" patternUnits="userSpaceOnUse">--%>
											<%--<path stroke="#BDBDBD" fill="none" d="M0,0H10V10"></path>--%>
										<%--</pattern>--%>
									<%--</defs>--%>

									<%--<pattern id="grid2" patternUnits="userSpaceOnUse" x="0" y="0" width="100" height="100">--%>
										<%--<image x="0" y="0" width="100" height="100" xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAB80lEQVR4Xu3c0U0DMRCE4UkZdMAzDSK6QCkGiQZ4pgPaQCAUBLGTlWLpfHcfz9ZJ7Hk9+884d0jykuQ11//uknxcXxbr2kUq1eWQ5DHJU6HQD0nerDurwNC6eCH9HTa00ElKz/NCvJBTBUo7prqztrJOh+gQHXJpMNIhOkSH6JC/FZh6mPg6sp6THAvAd5/k3bqzCgytCw2hITSEhqxMQ5iL7S27iPjTEBpCQ2gIDRFkNbqgpEk0ZEINQertlzKUwJOUnqdDJuwQHIJDvitQErm9rXNkObKAITAEhsAQGG5kSCDqE4o6UkfqOKTXmI6sCY8s1gnrxJHlyPqtwNQeGg2hIbwsXhYvi5d1i5eF1JG6sdfYa+y9NEvoEB2iQ3TIvwqUHAKkjtSROlJH6kgdqRdvl1dvoS+1jqgTdaJO1Ik6Ub9F1N06ceuEuchcZC4yF5mLG/jyti/K9Ru59Lvy0USP1JE6UkfqSB2pI3U/+uyehKUwf2+fzKj+v6YsU5Ypy5S1sinL7ff2lkXqnVbe1ZBA1Ik6USfqKxN1mbpMXaYuU5epy9Rl6jL1ZhcsQrijs+2lngcMgSEwBIbAUKYuU5epy9R/KjA0HjBlmbJMWaaslU1ZMnWZOvud/c5+Z7+z3zdiv8vUZepEnagTdaJO1Dcg6p9v/erTOjnYNAAAAABJRU5ErkJggg=="></image>--%>
									<%--</pattern>--%>
									<%--<rect id="rect1" x="20" y="20" width="500" height="300" stroke="green"  fill="url(#grid1)"></rect>--%>
									<%--<rect id="rect2" x="20" y="330" width="500" height="300" stroke="green" fill="url(#grid2)"></rect>--%>

								<%--</svg>--%>

							</div>
						</div>
					</div>
					<div class="tcs-bottom">
						<div class="zoom-label">
							<input id="zoom" size="3" value="100%" type="text" readonly="readonly" />

							<select id="zoom_select">
								<option value="6">70%</option>
								<option value="12">75%</option>
								<option value="16">80%</option>
								<option value="25">85%</option>
								<option value="50">90%</option>
								<option value="75">95%</option>
								<option value="100" selected="selected">100%</option>
								<option value="150">105%</option>
								<option value="200">110%</option>
								<option value="300">115%</option>
								<option value="400">120%</option>
								<option value="600">125%</option>
								<option value="800">130%</option>
								<option value="1600">135%</option>
							</select>
						</div>
						<div class="show-reset selected"><img src="${ctxStatic}/images/zoom-fit.png" title=""></div>
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
							<img src="${ctxStatic}/images/find-vehicle.22.png" alt=""></li>
						<li>
							<img src="${ctxStatic}/images/pause-vehicles.22.png" alt=""></li>
						<li>
							<img src="${ctxStatic}/images/zoom-fit.png" alt=""></li>
						<li>
							<img src="${ctxStatic}/images/view-split.png" alt=""></li>
						<li>
							<img src="${ctxStatic}/images/comment-add.16.png" alt=""></li>
						<li>
							<img src="${ctxStatic}/images/block.18x18.png" alt=""></li>
						<li>
							<img src="${ctxStatic}/images/staticRoute.18x18.png" alt=""></li>
						<li>
							<img src="${ctxStatic}/images/document-page-setup.16x16.png" alt=""></li>
						<li>
							<img src="${ctxStatic}/images/transport-order.png" class="to" alt=""></li>
					</ul>
				</div>
			</div>

			<div class="bottom-panel">
				<div class="bottom-panel-title">小车列表</div>
				<div class="bottom-panel-list">
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
						<div class="vehicle" data-toggle="modal" data-target="#myModal">
							<div class="name">Vehicle</div>
							<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>
							<div class="status">runing</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
						<div class="vehicle" data-toggle="modal" data-target="#myModal">
							<div class="name" data-toggle="modal" data-target="#myModal">Vehicle</div>
							<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>
							<div class="status">runing</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
						<div class="vehicle" data-toggle="modal" data-target="#myModal">
							<div class="name">Vehicle</div>
							<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>
							<div class="status">runing</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
						<div class="vehicle" data-toggle="modal" data-target="#myModal">
							<div class="name" data-toggle="modal" data-target="#myModal">Vehicle</div>
							<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>
							<div class="status">runing</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
						<div class="vehicle" data-toggle="modal" data-target="#myModal">
							<div class="name">Vehicle</div>
							<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>
							<div class="status">runing</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
						<div class="vehicle" data-toggle="modal" data-target="#myModal">
							<div class="name">Vehicle</div>
							<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>
							<div class="status">runing</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
						<div class="vehicle" data-toggle="modal" data-target="#myModal">
							<div class="name">Vehicle</div>
							<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>
							<div class="status">runing</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
						<div class="vehicle" data-toggle="modal" data-target="#myModal">
							<div class="name">Vehicle</div>
							<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>
							<div class="status">runing</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
						<div class="vehicle" data-toggle="modal" data-target="#myModal">
							<div class="name">Vehicle</div>
							<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>
							<div class="status">runing</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
						<div class="vehicle" data-toggle="modal" data-target="#myModal">
							<div class="name">Vehicle</div>
							<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>
							<div class="status">runing</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
						<div class="vehicle" data-toggle="modal" data-target="#myModal">
							<div class="name">Vehicle</div>
							<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>
							<div class="status">runing</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
						<div class="vehicle" data-toggle="modal" data-target="#myModal">
							<div class="name">Vehicle</div>
							<div class="battery"><img src="${ctxStatic}/images/battery/battery-100-2.png" /></div>
							<div class="status">runing</div>
						</div>
					</div>

				</div>
				<!-- Modal -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
				  <div class="modal-dialog" role="document">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				        <h4 class="modal-title" id="myModalLabel">小车实时状态</h4>
				      </div>
				      <div class="modal-body">
				        ...
				      </div>
				      <div class="modal-footer">
				        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				        <!--<button type="button" class="btn btn-primary">Save changes</button>-->
				      </div>
				    </div>
				  </div>
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
						<td><a href="javascript:;">name</a></td>
						<td title="hslhslhslhslhslhsl">hslhslhslhslhslhsl</td>
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
						<td><a href="javascript:;">name</a></td>
						<td>hsl</td>
						<td>
							<select>
								<option value="">请选择</option>
								<option value="mo">adapter1</option>
								<option value="lo">adapter2</option>
							</select>
						</td>
						<td><input type="checkbox" name="2" class="startflg" value="2"/></td>
						<td>name</td>
					</tr>
					<tr>
						<td><a href="javascript:;">name</a></td>
						<td>hsl</td>
						<td>
							<select>
								<option value="">请选择</option>
								<option value="mo">adapter1</option>
								<option value="lo">adapter2</option>
							</select>
						</td>
						<td><input type="checkbox" name="3" checked="" class="startflg" value="3"/></td>
						<td>name</td>
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

	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="${ctxStatic}/plugin/bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript" src="${ctxStatic}/plugin/zTree/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${ctxStatic}/plugin/bootSideMenu/js/BootSideMenu.js"></script>
	<script type="text/javascript" src="${ctxStatic}/plugin/draw2d/backbone.js"></script>
	<script type="text/javascript" src="${ctxStatic}/plugin/draw2d/events.js"></script>
	<script type="text/javascript" src="${ctxStatic}/plugin/draw2d/underscore.js"></script>
	<script type="text/javascript" src="${ctxStatic}/plugin/draw2d/two.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/mjtcs-elem-point.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/mjtcs-elem-path.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/mjtcs-elem-location.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/mjtcs-elem-link.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/mjtcs-modelling.js"></script>
	 <script type="text/javascript" src="${ctxStatic}/js/init.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/operating.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/mousewheel.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tcs-editor.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tcscanvas.js"></script>
	<script type="text/javascript">
	  $(document).ready(function(){
		  startLoading();
	      $('#left-sidebar').BootSideMenu({side:"left", autoClose:true});


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
			var setinginfo = $("#left-sidebar .content .settinginfo");
			if($(this).parent().parent().find(".startflg")[0].checked){
				setinginfo.find("input").removeAttr("disabled");
				setinginfo.find("input[name=slaveId]").val($(this).parent().parent().find(".startflg").val());
				setinginfo.find("input[name=mark]").val($(this).parent().parent().find(".startflg").attr("name"));
			}

		});


	  });

		function startLoading(){
			var _PageHeight = document.documentElement.clientHeight;
			var _PageWidth = document.documentElement.clientWidth;
			//计算loading框距离顶部和左部的距离（loading框的宽度为215px，高度为61px）
			var _LoadingTop = _PageHeight > 61 ? (_PageHeight - 61) / 2 : 0;
			var _LoadingLeft = _PageWidth > 215 ? (_PageWidth - 215) / 2 : 0;
			$("#loadingDiv").attr("display", "block");
			$("#loadingDiv").attr("height", _PageHeight);
			$("#loadingDiv > div").attr("left", _LoadingLeft);
			$("#loadingDiv > div").attr("top", _LoadingTop);

		}

		function endingLoading(){
			$("#loadingDiv").attr("display", "none");
		}
	</script>
</body>
</html>