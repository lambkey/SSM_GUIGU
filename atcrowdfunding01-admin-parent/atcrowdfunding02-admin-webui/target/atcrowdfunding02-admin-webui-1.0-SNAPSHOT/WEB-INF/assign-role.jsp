<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/19
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>
<script type="text/javascript">
    $(function () {
        $("#rightBtn").click(
            function () {
                // 把左边选中的option添加到右边
                $("select:eq(0)>option:selected").appendTo("select:eq(1)");
                // 删除左边的已选中的option元素
                $("select:eq(0)>option:selected").prop();
            }
        );
        $("#leftBtn").click(
            function () {
                // 把右边边选中的option添加到左边
                $("select:eq(1)>option:selected").appendTo("select:eq(0)");
                // 删除右边的已选中的option元素
                $("select:eq(0)>option:selected").prop();
            }
        );
        $("#submitBtn").click(
            function () {
                // option有一个选中标签，selected，设置它的属性为selected就可以选中它
                $("select:eq(1)>option").prop("selected","selected")
        })

    })
</script>
<body>
<%@include file="include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@include file="include-siderbar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form action="assign/do/role/assign.html" method="post"  role="form" class="form-inline">
                        <input type="hidden" name="adminId" value="${param.adminId}">
                        <input type="hidden" name="pageNum" value="${param.pageNum}">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <div class="form-group">
                            <label for="exampleInputPassword1">未分配角色列表</label><br>
                            <select  class="form-control" multiple="multiple" size="10" style="width:200px;overflow-y:auto;">
                                <c:forEach items="${requestScope.unAssignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="rightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="leftBtn" class="btn btn-default glyphicon glyphicon-chevron-left" style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label for="exampleInputPassword1">已分配角色列表</label><br>
                            <select name="roleIdList" class="form-control" multiple="multiple" size="10" style="width:200px;overflow-y:auto;">
                                <c:forEach items="${requestScope.assignedRoleList}" var="role">
                                    <option  value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <button id="submitBtn" type="submit" style="width: 150px;margin: 50px auto 0px auto"  class="btn btn-lg btn-success btn-block">保存</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

</body>
</html>

