<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,user-scalable=no">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/plugin/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/default.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/modelling.css">
	<link rel="stylesheet" href="${ctxStatic}/plugin/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<link rel="stylesheet" href="${ctxStatic}/css/tcs-editor.css" type="text/css">
	<title>Modelling Model</title>
</head>
<body>
<header>
	<!--<h1>AVG Modelling-Mode<a href="operating.html">[operating]</a></h1>-->
	<div class="mj-logo"><img src="../images/mj-logo.png" alt="" class=""></div>
	<div class="title">AVG Modelling</div>

	<div class="sys-info">
		<!--<p class="sence"><a href="javascript:;">选择场景</a>|</p>-->
		<div class="scene">

			<button type="button" class="btn btn-primary btn-sm" data-toggle="modal"  data-target="#sceneselect">
				选择场景
			</button>
			<div class="dropdown">
				<button class="btn btn-default dropdown-toggle" type="button" id="model-switch" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
					Modelling
					<span class="caret"></span>
				</button>
				<ul class="dropdown-menu" aria-labelledby="model-switch">
					<li><a href="./operating">Operating</a></li>
				</ul>
			</div>
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
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
							<button type="button" class="btn btn-primary">确定</button>
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
		<div class="operate-list">
			<ul class="operation-list">
				<li><img src="../images/save_edit.png" alt="1111"></li>
				<li><img src="../images/select-2.png" alt="1111"></li>
				<li><img src="../images/cursor-opened-hand.png" alt=""></li>
				<li>
					<img src="../images/point-halt-arrow.22.png" alt="" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<ul class="dropdown-menu point-item">
						<li><a href="#"><img src="../images/point-halt.22.png"/>Halt point</a></li>
						<li><a href="#"><img src="../images/point-report.22.png"/>Report point</a></li>
						<li><a href="#"><img src="../images/point-park.22.png"/>Park point</a></li>
					</ul>
				</li>
				<li><img src="../images/location.22.png" alt=""></li>
				<li>
					<img src="../images/path-direct-arrow.22.png" alt="" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<ul class="dropdown-menu path-item">
						<li><a href="#"><img src="../images/path-direct.22.png"/>Direct</a></li>
						<li><a href="#"><img src="../images/point-report.22.png"/>Bezier</a></li>
					</ul>
				</li>
				<li><img src="../images/link.22.png" alt=""></li>
				<li><img src="../images/locationType.22.png" alt=""></li>
				<li><img src="../images/car.png" alt=""></li>
				<li><img src="../images/blockdevice-3.22.png" alt=""></li>
				<li><img src="../images/format-connect-node.22.png" alt=""></li>
				<li><img src="../images/groups.png" alt=""></li>
				<li><img src="../images/draw-arrow-back.png" alt=""></li>
				<li><img src="../images/draw-arrow-forward.png" alt=""></li>
				<li><img src="../images/draw-arrow-up.png" alt=""></li>
				<li><img src="../images/draw-arrow-down.png" alt=""></li>
				<li><img src="../images/object-order-front.png" alt=""></li>
				<li><img src="../images/object-order-back.png" alt=""></li>
				<li><img src="../images/transport-order.png" alt=""></li>
				<li><img src="../images/transport-order.png" alt=""></li>
				<li><img src="../images/transport-order.png" alt=""></li>
			</ul>
		</div>
		<div class="operate-content">
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
						<svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" style="background-color: indianred">
							<defs>
								<!-- 用一个10*10方形路径填充 -->
								<pattern id="grid1" x="0" y="0" width="10" height="10" patternUnits="userSpaceOnUse">
									<path stroke="#BDBDBD" fill="none" d="M0,0H10V10"></path>
								</pattern>
							</defs>

							<pattern id="grid2" patternUnits="userSpaceOnUse" x="0" y="0" width="100" height="100">
								<image x="0" y="0" width="100" height="100" xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAB80lEQVR4Xu3c0U0DMRCE4UkZdMAzDSK6QCkGiQZ4pgPaQCAUBLGTlWLpfHcfz9ZJ7Hk9+884d0jykuQ11//uknxcXxbr2kUq1eWQ5DHJU6HQD0nerDurwNC6eCH9HTa00ElKz/NCvJBTBUo7prqztrJOh+gQHXJpMNIhOkSH6JC/FZh6mPg6sp6THAvAd5/k3bqzCgytCw2hITSEhqxMQ5iL7S27iPjTEBpCQ2gIDRFkNbqgpEk0ZEINQertlzKUwJOUnqdDJuwQHIJDvitQErm9rXNkObKAITAEhsAQGG5kSCDqE4o6UkfqOKTXmI6sCY8s1gnrxJHlyPqtwNQeGg2hIbwsXhYvi5d1i5eF1JG6sdfYa+y9NEvoEB2iQ3TIvwqUHAKkjtSROlJH6kgdqRdvl1dvoS+1jqgTdaJO1Ik6Ub9F1N06ceuEuchcZC4yF5mLG/jyti/K9Ru59Lvy0USP1JE6UkfqSB2pI3U/+uyehKUwf2+fzKj+v6YsU5Ypy5S1sinL7ff2lkXqnVbe1ZBA1Ik6USfqKxN1mbpMXaYuU5epy9Rl6jL1ZhcsQrijs+2lngcMgSEwBIbAUKYuU5epy9R/KjA0HjBlmbJMWaaslU1ZMnWZOvud/c5+Z7+z3zdiv8vUZepEnagTdaJO1Dcg6p9v/erTOjnYNAAAAABJRU5ErkJggg=="></image>
							</pattern>
							<rect id="rect1" x="20" y="20" width="500" height="300" stroke="green"  fill="url(#grid1)"></rect>
							<rect id="rect2" x="20" y="330" width="500" height="300" stroke="green" fill="url(#grid2)"></rect>

						</svg>

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

				<span class="show-position">x:100 y:200</span>
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
					<tr>
						<td>name</td>
						<td>hsl</td>
					</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="${ctxStatic}/js/jquery-1.11.3.js"></script>
<script type="text/javascript" src="${ctxStatic}/plugin/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctxStatic}/plugin/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/init.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modelling.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/mousewheel.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tcs-editor.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tcs-canvas.js"></script>

</body>
</html>