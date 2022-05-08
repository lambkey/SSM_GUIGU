<%--
  Created by IntelliJ IDEA.
  User: lamp
  Date: 2022/3/19
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="updateModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">更新角色</h4>
            </div>

            <div class="modal-body">
                <form  class="form-signin" role="form">
                    <div class="form-group has-success has-feedback">
                        <input  type="text" name="roleName" class="form-control"  placeholder="请输入角色的名称" autofocus>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button id="updateRoleBtn" type="button" class="btn btn-primary">确认更新</button>
            </div>
        </div>
    </div><
</div>

