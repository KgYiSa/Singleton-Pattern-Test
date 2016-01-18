<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/plugin/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/default.css">
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.11.3.js"></script>

    <title>用户登录</title>
</head>
<body>
<header>
    <div><img src="../images/mj-logo.png" alt="" class=""></div>
    <div><p class="title"></p></div>
</header>

<div class="container-fluid">

    <div class="row">
        <div class="login-form form-horizontal" >
            <div class="login-form-title form-group">
                <h3>用户登录${date }</h3>
            </div>
            <form action="/api/login" method="post">
            <div class="form-group">
                <label for="inputEmail3" class="col-sm-offset-1 col-sm-2 control-label">Email</label>
                <div class="col-sm-8">
                    <input  name="username" class="form-control" id="inputEmail3" placeholder="Email"></div>
            </div>
            <div class="form-group">
                <label for="inputPassword3" class="col-sm-offset-1 col-sm-2 control-label">Password</label>
                <div class="col-sm-8">
                    <input type="password" name="password" class="form-control" id="inputPassword3" placeholder="Password"></div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-2">
                    <button type="submit" class="btn btn-default">登录</button>
                </div>
                <div class="col-sm-offset-3 col-sm-2">
                    <button type="button" class="btn btn-default">注册</button>
                </div>
            </div>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript" src="${ctxStatic}/plugin/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/init.js"></script>
</body>
</html>