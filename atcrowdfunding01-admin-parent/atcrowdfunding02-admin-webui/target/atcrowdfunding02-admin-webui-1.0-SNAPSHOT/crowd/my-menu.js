



// 在鼠标离开节点范围时删除按钮组
function myRemoveHoverDom(treeId,treeNode) {
    // 7、拼接按钮组的id
    var btnGroupId = treeNode.tId + "_btnGrp";

    // 8、移除对应的元素
    $("#"+btnGroupId).remove();
}

// 在鼠标移入节点范围时添加按钮组
function myAddHoverDom(treeId,treeNode) {
    // 1、按钮组的标签结构:<span><a><i></i></a><a><i>/i<></a></span>
    // 2、按钮组出现的位置：节点中treeDemo_n_a超链接的后面
    // 5、为了在需要移除按钮组的时候能够精确定位到按钮组所在span，需要给span设置有规律的id
    var btnGroupId = treeNode.tId + "_btnGrp";
    // 6、判断一下以前是否已经添加了按钮组
    if ($("#"+btnGroupId).length>0){
        return;
    }

    // 9、准备各个按钮的HTML标签
    var addBtn = "<a id='"+treeNode.id+"' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='添加节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn ="<a id='"+treeNode.id+"' class=' removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='删除节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    var editBtn = "<a id='"+treeNode.id+"' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='修改节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";


    // 10、获取当前节点的级别数据
    var level = treeNode.level;

    // 11、拼接装好的按钮代码
    var btmHTML = "";

    // 12、判断当前节点的级别
    if (level == 0){
        // 级别为0时是根节点，只能添加子节点
        btmHTML=addBtn;
    }
    if (level == 1){
        // 级别为1时是分支节点，可以添加子节点，修改
        btmHTML =addBtn +" "+editBtn;

        // 获取当前节点的子节点数量
        var length = treeNode.children.length

        // 如果没有子节点，可以删除
        if (length == 0){
            btmHTML = btmHTML + " " + removeBtn;
        }
    }
    if (level == 2){
        // 级别为2时，可以修改删除
        btmHTML = editBtn + " " +removeBtn;
    }


    // 3、找到附着按钮组的超链接
    var anchorId = treeNode.tId + "_a";

    // 4、执行在超链接后面附加span元素的操作
    $("#"+anchorId).after("<span id='"+btnGroupId+"'>"+btmHTML+"</span>");
}





function myAddDiyDom(treeId,treeNode) {

    // treeId是整个树形结构附着的ul标签的id
    console.log("treeId?="+treeId);
    // 当前全部节点的数据都在里面
    //console.log(treeNode);
    console.log(treeNode);
    // zTree生成id的规则
    // 例子：treeDemo_4_ico
    // 解析: ul标签的id_当前节点的序号_功能
    // 提示:  ul标签的id_当前节点的序号，部分可以通过访问treeNode的tId属性得到
    // 根据id的生成规则拼接出来span标签的id
    var spanId = treeNode.tId+"_ico";

    // 根据控制图标的span标签的id找到这个span标签
    // 删除旧的class
    $("#"+spanId).removeClass();
    // 添加新的class
    $("#"+spanId).addClass(treeNode.icon);
}
// 生成树形结构的函数
function generateTree() {

    // 2、准备生成树形结构的数据
    var zNodes =[];
    $.ajax({
        "url": "menu/do/get.json",
        "type": "post",
        "dataType": "json",
        "success": function (response) {
            if (response.result == "SUCCESS") {
                zNodes = response.data;
                //console.log(zNodes);
                // 1、创建JSON对象用于存储zTree所做的设置
                var setting = {
                    "view": {
                        "addDiyDom": myAddDiyDom,
                        "addHoverDom": myAddHoverDom,
                        "removeHoverDom": myRemoveHoverDom
                    },
                    "data": {
                        // 让菜单点击实现不跳转
                        "key": {
                            "url": "maomi"
                        }
                    }
                };
                // 3、初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);

            }
            if (response.result == "FAILURE") {
                layer.msg(response.message);
            }

        }
    });
}



