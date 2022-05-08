<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/19
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>

<body>
<%@include file="include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@include file="include-siderbar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="admin/to/main/page.html">首页</a></li>
                <li><a href="admin/get/page.html">数据列表</a></li>
                <li class="active">新增</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading" >表单数据 <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
                <div class="panel-body">

                    <form   action="admin/to/addAdmin.html" method="post" role="form">
                        <p>${requestScope.exception.message}</p>
                        <div class="form-group">
                            <label for="exampleInputPassword1">登陆账号</label>
                            <input  name="login_Acct" type="text" class="form-control" id="loginAcct" placeholder="请输入账号长度为5-12个数字">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputPassword1">登陆密码</label>
                            <input  name="user_Pswd" type="text" class="form-control" id="exampleInputPassword1" placeholder="请输入登陆密码">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputPassword1">用户名称</label>
                            <input name="user_Name" type="text" class="form-control" id="exampleInputPassword1" placeholder="请输入用户名称">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail1">邮箱地址</label>
                            <input name="email" type="email" class="form-control" id="exampleInputEmail1" placeholder="请输入邮箱地址">
                            <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                        </div>
                        <%--提交表单--%>
                        <button id="addSuccess" type="submit" class="btn btn-success"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                        <%--重新跳转到页面--%>

                        <a href="admin/to/add.html" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">帮助</h4>
            </div>
            <div class="modal-body">
                <div class="bs-callout bs-callout-info">
                    <h4>测试标题1</h4>
                    <p>测试内容1，测试内容1，测试内容1，测试内容1，测试内容1，测试内容1</p>
                </div>
                <div class="bs-callout bs-callout-info">
                    <h4>测试标题2</h4>
                    <p>测试内容2，测试内容2，测试内容2，测试内容2，测试内容2，测试内容2</p>
                </div>
        </div>
    </div>
</div>
</div>
<script >
    //用js提示用户，规范用户账号
    function LoginAcct(){
        /* 获取用户名值，一般用id=“username” */
        var loginAcct=$("#loginAcct").val();
        /* 使用正则表达式规范用户名长度 6-24个字母和数字组合*/
        var reg_username=/^[1-9][0-9]{4,10}$/;
        /* 判断用户名是否符合要求的类型 */
        var flag=reg_username.test(loginAcct);
        if(flag){
            //用户名合法
            $("#loginAcct").css("border","");
        }else{
            //用户名非法
            $("#loginAcct").css("border","1px solid red");
        }
        return flag;
    }
    //当某一个组件失去焦点时，调用对应的校验方法
    $("#loginAcct").blur(LoginAcct);
</script>
</body>
</html>

