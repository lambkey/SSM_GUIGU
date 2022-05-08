
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<! DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <%--http://localhost:8080/atcrowdfunding02_admin_webui_war/test/ssm.html--%>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <script type="text/javascript" src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script>
        $(function () {
            $("#btn1").click(function () {
                $.ajax({
                   "url":"send/array/one.html",             //请求目标资源的地址
                    "type":"post",                      //请求方式
                    "data":{
                     "array":[5,8,12]
                    },                                  //要发送的请求数据参数
                    "dataType":"text",                  //如何对待服务端返回的数据
                    "success":function (response) {     //服务器端成功处理请求后调用的回调函数，response是响应体数据
                        alert(response);
                        console.log(response);
                    },
                    "error":function (response) {       //服务器端处理请求失败后调用的回调函数
                        alert(response);
                    }
                });
            });
            $("#btn2").click(function () {
                    var array =[5,8,12];
                    console.log(array.length);
                    var requestBody =JSON.stringify(array);
                    console.log(requestBody.length);
                $.ajax({
                    "url":"send/array/two.html",             //请求目标资源的地址
                    "type":"post",                      //请求方式
                    "data": requestBody,                //要发送的请求数据参数
                    "contentType":"application/json;charset=UTF-8",    //设置发送请求内容的编码格式
                    "dataType":"text",                  //如何对待服务端返回的数据
                    "success":function (response) {     //服务器端成功处理请求后调用的回调函数，response是响应体数据
                        alert(response);
                        console.log(response);
                    },
                    "error":function (response) {       //服务器端处理请求失败后调用的回调函数
                        alert(response);
                    }
                });
            });
            $("#btn3").click(function () {
                var stuInfo ={"stuName":"杨卓颖","stuAge":18,"address":"草潭镇","subjects":[{"subName":"数学","subScore":98},{"subName":"语文","subScore":80}]}
                var requestBody=JSON.stringify(stuInfo);//将JSON对象转换为JSON字符串
                $.ajax({
                    "url":"send/array/three.json",             //请求目标资源的地址
                    "type":"post",                      //请求方式
                    "data": requestBody,                //要发送的请求数据参数
                    "contentType":"application/json;charset=UTF-8",    //设置发送请求内容的编码格式
                    "dataType":"json",                  //如何对待服务端返回的数据
                    "success":function (response) {     //服务器端成功处理请求后调用的回调函数，response是响应体数据
                        alert(response);
                        console.log(response);
                    },
                    "error":function (response) {       //服务器端处理请求失败后调用的回调函数
                        alert(response);
                    }
                });
            });
            $("#btn4").click(function () {
                layer.msg("layer的小弹窗");
            });
            $("#btn5").click(function () {
                console.log("发送异步请求前");
                $.ajax(

                    {
                        "url":"test/ajax1.html",
                        "type":"post",
                        "dataType":"text",
                        "async":false,
                        "success":function (response) {
                            console.log(response);
                        }
                    }
                );

                console.log("发送异步请求后");
            });
        });
    </script>
</head>
<body>
    <%--超链接，使用绝对路径,test前面不写斜杠--%>
    <a href="test/ssm.html">测试SSM整合环境</a>
    <br/>
    <button id="btn1">Send [5,8,15] one</button>
    <button id="btn2">Send [5,8,15] two</button>
    <button id="btn3">Send Student</button>
    <button id="btn4">layer弹框</button>
    <button id="btn5">ajax异步</button>
</body>
</html>
