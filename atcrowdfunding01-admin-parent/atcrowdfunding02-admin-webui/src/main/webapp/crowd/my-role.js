
function generateAuthTree() {
    var ajaxReturn = $.ajax({
        "url":"assign/get/tree.json",
        "type":"post",
        "async":false,
        "dataType":"json"
    });
    if (ajaxReturn.status!=200){
        layer.msg("error!status:"+ajaxReturn.status+"errorMessage"+ajaxReturn.statusText);
    }

    var resultEntity = ajaxReturn.responseJSON;

    if (resultEntity.result == "FAILURE"){
        layer.msg("操作失败！"+resultEntity.message);
    }

    if (resultEntity.result == "SUCCESS"){
        var authList = resultEntity.data;
        // 将服务端查询的list交给zTree自己组装

        var setting = {
            data:{
                // 开启简单JSON功能
                simpleData: {
                    enable: true,
                    // 通过pIdKey属性设置父节点的属性名，而不使用默认的pId
                    pIdKey:"categoryId"
                },
                key: {
                    // 设置在前端显示的节点名是查询到的title，而不是使用默认的name
                    name:"title"
                }
            },
            // 开启模态框的勾选选项
            check: {
                enable: true
            }
        };

        // 生成树形结构信息
        $.fn.zTree.init($("#authTreeDemo"),setting,authList);

        // 设置节点默认是展开的
        // 1 得到zTreeObj
        var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
        // 2 设置默认展开
        zTreeObj.expandAll(true);

        // ----------回显----------

        // 回显（勾选）后端查出的匹配的权限
        ajaxReturn = $.ajax(
            {
                "url":"assign/get/checked/auth/id.json",
                "type":"post",
                "dataType":"json",
                "async":false,
                "data":{
                   "roleId":window.roleId
                }
            }
        );
        if (ajaxReturn.status != 200){
            layer.msg("请求出错！错误码："+ ajaxReturn.status + "错误信息：" + ajaxReturn.statusText);
            return ;
        }

        resultEntity = ajaxReturn.responseJSON;

        if (resultEntity.result == "FAILED"){
            layer.msg("操作失败！"+resultEntity.message);
        }

        if (resultEntity.result == "SUCCESS"){
            var authIdArray = resultEntity.data;

            // 遍历得到的authId的数组
            // 根据authIdArray勾选对应的节点
            for (var i = 0; i < authIdArray.length;i++){
                var authId = authIdArray[i];

                // 通过id得到treeNode
                var treeNode = zTreeObj.getNodeByParam("id",authId);

                // checked设置为true，表示勾选节点
                var checked = true;

                // checkTypeFlag设置为false，表示不联动勾选
                // 即父节点的子节点未完全勾选时不改变父节点的勾选状态
                // 否则会出现bug：前端只要选了一个子节点，传到后端后，下次再调用时，发现前端那个子节点的所有兄弟节点也被勾选了，
                // 因为在子节点勾选时，父节点也被勾选了，之后前端显示时，联动勾选，导致全部子节点被勾选
                var checkTypeFlag = false;

                // ztreeObj的checkNode方法 执行勾选操作
                zTreeObj.checkNode(treeNode,checked,checkTypeFlag);
            }
        }
    }
}



// 声明专门的函数显示确认模态框
function showConfirmModal(roleArray) {
    // 打开模态框
    $("#removeModal").modal("show");

    // 清除旧的数据
    $("#roleNameDiv").empty();

    window.arrayRoleId = [];
    // 遍历数组
    for (var i = 0 ;i< roleArray.length; i++){
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName+"<br/>");
        window.arrayRoleId.push(role.roleId);
    }
}





// 执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage(){

    // 1、获取分页数据
    var pageInfo = getPageInfoRemote();

    // 2、填充表格
    fillTableBody(pageInfo);
}

// 远程访问服务器端程序获取pageInfoRemote数据
function getPageInfoRemote() {
   var ajaxResult = $.ajax({
        "url":"role/get/page/info.json",
        "type":"post",
        "data":{
            "keyword":window.keyword,
            "pageNum":window.pageNum,
            "pageSize":window.pageSize

        },
        // 同步请求
        "async":false,
        // 服务端响应返回的数据处理方式为json格式
        "dataType":"json"
    });
   // 如果当前响应状态码不是200，说明发生了错误或其他意外情况，显示提示消息。让当前函数停止执行
    if (ajaxResult.status != 200){
        layer.msg("Failure!FailureCode:"+ajaxResult.status);
        return null;
    }
    // 判断后端处理逻辑是否正确
    var  JSONResult = ajaxResult.responseJSON;
    if (JSONResult.result =="FAILURE"){
        layer.msg(JSONResult.message);
        return null;
    }

    if (JSONResult.result =="SUCCESS"){
       var pageInfo = JSONResult.data
        return pageInfo;
    }
}

// 填充表格
function fillTableBody(pageInfo){

    $("#rolePageBody").empty();
    $("#Pagination").empty();
    // 判断pageInfo对象是否有效
    if (pageInfo == null || pageInfo == undefined || pageInfo.list == null ||pageInfo.list.length ==0){
        $("#rolePageBody").append("<tr><td colspan='4'>抱歉！没有查询到您搜索的数据！</td></tr>")
        return ;
    }
    // 使用pageInfo的list属性填充tbody
    for (var i=0;i<pageInfo.list.length;i++){
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;
        var numberTd = "<td>"+(i+1)+"</td>";
        var checkboxTd = "<td><input class='itemBox' type='checkbox' id='"+roleId+"'></td>";
        var roleNameTd = "<td>"+roleName+"</td>";

        var checkBtn ="<button id='"+roleId+"'     type='button' class='checkBtn btn btn-success btn-xs'><i class=' glyphicon glyphicon-check'></i></button>";
        var addBtn ="<button id='"+roleId+"' class='btn btn-primary btn-xs updateRole'><i class=' glyphicon glyphicon-pencil'></i></button>";
        var removeBtn ="<button id='"+roleId+"' class='btn btn-danger btn-xs removeRole'><i class=' glyphicon glyphicon-remove'></i></button>";
        var btnTd ="<td>"+checkBtn+"&nbsp;"+addBtn+"&nbsp;"+removeBtn+"</td>";
        var tr ="<tr>"+numberTd+checkboxTd+roleNameTd+btnTd+"</tr>";
        $("#rolePageBody").append(tr);
    }

    // 生成分页导航条
    generateNavigator(pageInfo);
}

//生成分页页码导航条
function generateNavigator(pageInfo) {
    // 获取服务器查询的admin的总记录数
    var totalRecord = pageInfo.total;

    // 声明一个JSON对象存储Pagination要设置的属性
    var properties = {
        num_edge_entries: 3,                                // 边缘页数
        num_display_entries: 5,                             // 主体页，(夹在边缘页的中间)
        callback: paginationCallBack,                     // 用户点击“1,2,3”这样的页码时调用这个行数对这个函数实现跳转
        items_per_page: pageInfo.pageSize,   // 当前一页显示的记录数量
        current_page: pageInfo.pageNum-1,    // 当前显示的页面
        prev_text:"上一页",
        next_text:"下一页"
    };
    // 生成页码导航条
    $("#Pagination").pagination(totalRecord,properties);
}

// 翻页时的回调函数
function paginationCallBack(pageIndex,jQuery) {
    // 修改全局变量的pageNum
    window.pageNum = pageIndex+1;

    // 调用分页函数
    generatePage();

    return false;
}
