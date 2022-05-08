<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/18
  Time: 19:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/login.css">
    <script type="text/javascript" src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script type="text/javascript">
        $(function () {
            $("button").click(function (){
                //相当于浏览器的后退按钮
                window.history.back();
            });
        });
    </script>
    <style>
    </style>
</head>

<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
    </div>
</nav>

<div class="container">


    <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in" style="text-align: center;"></i> 尚筹网系统消息</h2>


    <h3 style="text-align: center;">${requestScope.SuccessTip}</h3>
    <%--
        requestScope对应的是存放request域数据的Map
        requestScope.exception相当于request.getAttribute("exception")
        requestScope.exception相当于exception.getMassage()
    --%>
    <button style="width: 150px;margin: 50px auto 0px auto"  class="btn btn-lg btn-success btn-block">点我返回上一层</button>
</div>

</body>
</html>
