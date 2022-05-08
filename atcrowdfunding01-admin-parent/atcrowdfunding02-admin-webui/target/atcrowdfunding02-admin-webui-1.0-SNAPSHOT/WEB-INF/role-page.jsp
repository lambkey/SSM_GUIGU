<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/19
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>
<%--引入分页导航插件pagination和pagination插件的css--%>
<link rel="stylesheet" href="css/pagination.css">
<%--引入分页导航插件pagination的js--%>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<link href="ztree/zTreeStyle.css" rel="stylesheet" />
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd/my-role.js" ></script>
<script>
    $(function () {

        // 1、为分页操作准备初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";
        generatePage();

        // 关键词查询，修改全局的window.keyword即可
        $("#searchBtn").click(function () {
            window.pageNum = 1;
            window.keyword = $("#keywordInput").val();
            generatePage();
        });


        // 2、新增角色
        // 打开模态框
        $("#showAddModalBtn").click(function () {
            $("#addModal").modal("show");
        });

        // 给新增模态框中的保存按钮绑定单击响应函数
        $("#saveRoleBtn").click(function () {
            // 1、获取用户在文本框中输入的角色名称
            // $("#addModal [name=roleName]").val()表示在addModal的id下面的标签里面的name属性
            // $.trim（）去掉前空格
            var roleName =$.trim($("#addModal [name=roleName]").val());

            // 发送ajax请求
            $.ajax({
                "url":"role/save.json",
                "type":"post",
                "data":{
                    "name":roleName
                },
                "dataType":"json",
                "success":function (response) {
                    var  result = response.result;

                    if (result == "SUCCESS"){
                        layer.msg("操作成功");
                    }
                    if (result == "FAILURE"){
                        layer.msg("操作失败"+response.message);
                    }

                },
                "error":function (response) {
                    layer.msg(response.status+" "+response.statusText);
                }
            });

            // 关闭模态框
            $("#addModal").modal("hide");

            // 清理模态框
            $("#addModal [name=roleName]").val("");

            // 重新加载分页数据,将页码定位到最后一页
            window.pageNum = 9999999;
            generatePage();

        });

        // 4、更新角色
        // $(".updateRole").click(function () {
        //     $("#updateModal").modal("show");
        // })
        // 以上的打开模态框形式在翻页之后就不起作用了
        // 更改
        // 1、on()函数的第一个参数是事件类型
        // 2、on()函数的第二个参数是找到真正要绑定事件的元素的选择器
        // 3、on()函数的第三个参数是事件的响应函数
        $("#rolePageBody").on("click",".updateRole",function () {

            // 打开模态框
            $("#updateModal").modal("show");

            // 获取表格当前行中的角色名称
            // 标签在my-role.js文件中
            // button的父亲是td，当前td的上一个兄弟元素就是 var roleNameTd = "<td>"+roleName+"</td>"，它里面的文本就是text
            var roleName = $(this).parent().prev().text();

            $("#updateModal [name = roleName]").val(roleName);

            // 获取当前标签的id的值
            var roleId = this.id;
            window.roleId = roleId;
        });
        $("#updateRoleBtn").click(function () {
            var role={"id":window.roleId,"name":$("#updateModal [name = roleName]").val()};
            var roleJSON = JSON.stringify(role);
            $.ajax({
                "url":"role/update.json",
                "type":"post",
                "data":roleJSON,
                "contentType":"application/json;charset=UTF-8",
                "dataType":"json",
                "success":function (response) {
                    if (result == "SUCCESS"){
                        layer.msg("操作成功");
                    }
                    if (result == "FAILURE"){
                        layer.msg("操作失败"+response.message);
                    }
                },
                "error":function (response) {
                    layer.msg(response.status+" "+response.statusText);
                }

            });
            // 关闭模态框
            $("#updateModal").modal("hide");

            // 清理模态框
            $("#updateModal [name=roleName]").val("");
            generatePage();
        });


        // 5、删除角色
        // 开启 单点删除模态框
        $("#rolePageBody").on("click",".removeRole",function () {
            var roleArray =[{
                roleId:this.id,
                roleName:$(this).parent().prev().text()
            }];
            showConfirmModal(roleArray);
            generatePage();
        });
        $("#removeRoleBtn").click(function () {
            var requestBody = JSON.stringify(window.arrayRoleId);
            $.ajax({
                "url":"role/delete/by/role/id/array.json",
                "type":"post",
                "data": requestBody,
                "contentType":"application/json;charset=UTF-8",
                "dataType":"json",
                "success":function (response) {
                    if (response.result == "SUCCESS"){
                        layer.msg("操作成功");
                    }
                    if (response.result =="FAILURE"){
                        layer.msg("操作失败");
                    }
                },
                "error":function (response) {
                    layer.msg(response.status+""+response.statusText);
                }
            });
            // 关闭模态框
            $("#removeModal").modal("hide");

            // 清理模态框
            $("#removeModal [name=roleName]").val("");
            generatePage();
        });


        // 6、批量删除
        // 给总的checkbox绑定单击响应函数
        $("#summaryBox").click(function () {

            // 1、获取当前多选框自身的状态
            var currentStatus = this.checked;

            // 2、用当前多选框的状态设置其他的多选框
            $(".itemBox").prop("checked",currentStatus);

        });

        // 全部的反向操作
        $("#rolePageBody").on("click",".itemBox",function () {

            // 获取当前已经选中的.itemBox的数量
            var checkedBoxCount = $(".itemBox:checked").length;

            // 获取全部.itemBox的数量
            var totalBoxCount = $(".itemBox").length;

            // 使用二者的比较结果设置总的checkbox
            $("#summaryBox").prop("checked",checkedBoxCount == totalBoxCount);
            
        });
        // 给批量删除的按钮绑定单击响应函数
        $("#batchRemoveBtn").click(function () {

            // 创建一个数组对象用来存放后面获取到的角色对象
            var arrayRole = [];

            // 遍历当前选中的多选框
            $(".itemBox:checked").each(function () {
                // 使用this引用当前遍历得到的多选框
                var roleId = this.id;

                // 通过DOM操作获取角色名称
                var roleName = $(this).parent().next().text();

                arrayRole.push({
                    "roleId":roleId,
                    "roleName":roleName
                });
            });

            // 检查roleArray的长度是否为0
            if (arrayRole.length == 0){
                layer.msg("请至少选择一个执行删除");
                return;
            }

            // 调用专门的函数打开模态框
            // 开启 删除模态框
            showConfirmModal(arrayRole);
            $("#removeRoleBtn").click(function () {
                var requestBody = JSON.stringify(window.arrayRoleId);
                $.ajax({
                    "url":"role/delete/by/role/id/array.json",
                    "type":"post",
                    "data":requestBody,
                    "contentType":"application/json;charset=UTF-8",
                    "dataType":"json",
                    "success":function (response) {
                        if (response.result == "SUCCESS"){
                            layer.msg("操作成功");
                        }
                        if (response.result =="FAILURE"){
                            layer.msg("操作失败");
                        }
                    },
                    "error":function (response) {
                        layer.msg(response.status+""+response.statusText);
                    }
                });
            });
            // 关闭模态框
            $("#removeModal").modal("hide");

            // 清理模态框
            $("#removeModal [name=roleName]").val("");
            generatePage();
        });

        $("#rolePageBody").on("click",".checkBtn",function () {
            window.roleId = this.id;
            $("#assignModal").modal("show");
            generateAuthTree();
        });

        // 给分配权限的模态框中的提交按钮设置单击响应函数
        $("#assignAuthBtn").click(
            function () {
           // 声明一个数组，用来存放被勾选的auth的id
           var authIdArray =[];

           // 拿到zTreeObj
           var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");

           // 通过getCheckedNodes方法拿到被选中的option信息
           var authArray = zTreeObj.getCheckedNodes();

           for (var i =0;i<authArray.length;i++){
               // 从被选中的auth中遍历得到每一个auth的id
               var authId = authArray[i].id;
               // 通过push方法将得到的id存入authIdArray
               authIdArray.push(authId);
           }

           var requestBody ={
               // 为了后端取值方便，两个数据都用数组格式存放，后端统一用List<Integer>获取
               "roleId":[window.roleId],
               "authIdList":authIdArray
           }
           requestBody = JSON.stringify(requestBody);

           $.ajax({
               "url":"assign/do/save/role/auth/relationship.json",
               "type":"post",
               "data":requestBody,
               "contentType":"application/json;charset=UTF-8",
               "success":function (response) {
                   if (response.result == "SUCCESS"){
                       layer.msg("操作成功!");
                   }
                   if (response.result == "FAILURE"){
                       layer.msg("操作失败！提示信息："+response.message);
                   }
               },
               "error":function (response) {
                   layer.msg(response.status+" "+response.statusText);
               }

           });

           // 关闭模态框
           $("#assignModal").modal("hide");

        });


    });

</script>
<body>
<%@include file="include-nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@include file="include-siderbar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput"  class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button type="button"  id="showAddModalBtn"  class="btn btn-primary" style="float:right;"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr >
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox" id="summaryBox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody">
                            <%--js填充--%>
                            </tbody>
                            <tfoot>
                            <tr >
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"></div>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
    </div>
</div>
</div>
<%@include file="/WEB-INF/modal-role-add.jsp"%>
<%@include file="/WEB-INF/modal-role-update.jsp"%>
<%@include file="/WEB-INF/modal-role-remove.jsp"%>
<%@include file="/WEB-INF/modal-assign-auth.jsp"%>
</body>
</html>

