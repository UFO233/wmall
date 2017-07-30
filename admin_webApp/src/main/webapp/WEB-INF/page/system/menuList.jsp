<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>菜单管理-菜单列表</title>
    <link rel="stylesheet" href="/resources/assets/css/jquery-ui.css"/>
    <link rel="stylesheet" href="/resources/assets/css/datepicker.css"/>
    <link rel="stylesheet" href="/resources/assets/css/ui.jqgrid.css"/>
    <%@ include file="/WEB-INF/page/comm/taglibs.jsp" %>
    <script src="/resources/assets/js/jqGrid/jquery.jqGrid.src.js"></script>
    <script src="/resources/assets/js/jqGrid/i18n/grid.locale-cn.js"></script>

    <link rel="stylesheet" href="/resources/assets/css/bootstrap-timepicker.css"/>
    <link rel="stylesheet" href="/resources/assets/css/daterangepicker.css"/>
    <link rel="stylesheet" href="/resources/assets/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="/resources/assets/css/colorpicker.css"/>
</head>
<body class="content_page">
<div class="box_page">
    <div class="row">
        <div class="col-xs-12" style="background-color: #fff">
            <div class="row">
                <div class="col-sm-3">
                    <div class="widget-body">
                        <div class="widget-main padding-8" style="border: 1px solid rgb(176, 224, 230);height: 550px;overflow-y: auto;">
                            <ul id="menuTree"></ul>
                        </div>
                    </div>
                </div>
                <div class="col-sm-9">
                    <div class="widget-box transparent">
                        <form class="form-inline" role="form" onsubmit="THISPAGE.reloadData();return false;">
                            <div class="input-group">
                                <input type="hidden" id="refreshFlag" value="0">
                                <input class="" type="text" id="parentName" readonly="readonly" disabled="disabled"
                                       placeholder="显示父菜单" maxlength="50"/>
                            </div>
                            <div class="input-group" id="addMenuDiv" style="display: none">
                                <span class="input-group-btn">
                                    <button type="button" class="btn btn-success btn-sm" onclick="addMenu()">
                                        <span class="ace-icon fa fa-pencil-square-o icon-on-right bigger-110"></span>
                                        添加菜单
                                    </button>
                                </span>
                            </div>
                            <input type="hidden" id="parentId" value=""/>
                        </form>
                    </div>
                    <table id="grid-table"></table>
                    <div id="grid-pager"></div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script src="/resources/assets/js/bootstrap.js"></script>
<script src="/resources/assets/js/chosen.jquery.js"></script>
<script src="/resources/assets/js/fuelux/fuelux.spinner.js"></script>
<script src="/resources/assets/js/date-time/bootstrap-datepicker.js"></script>
<script src="/resources/assets/js/date-time/bootstrap-timepicker.js"></script>
<script src="/resources/assets/js/date-time/moment.js"></script>
<script src="/resources/assets/js/date-time/daterangepicker.js"></script>
<script src="/resources/assets/js/date-time/bootstrap-datetimepicker.js"></script>
<script src="/resources/assets/js/bootstrap-colorpicker.js"></script>
<script src="/resources/js/lib/layer.js"></script>
<script src="/resources/assets/js/fuelux/fuelux.tree.js?v=${version}"></script>
<script src="/resources/assets/js/ace-elements.js?v=${version}"></script>
<script src="/resources/assets/js/ace.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $(document).on('click', 'a[data-url]', function (e) {
            var dataurl = $(this).attr('data-url');
            parent.openPage(dataurl);
            return false;
        });
        $("#parentName").val("菜单列表");
        initMenuTree();
        THISPAGE.init();
    });

    var queryConditions = {
        matchCon: ""
    }, THISPAGE = {

        init: function () {
            this.initDom();
            this.loadGrid();
            this.addEvent()
        },
        initDom: function () {
        },
        loadGrid: function () {
            var i = this, column = [{
                name: "id",
                label: "菜单ID",
                index: "id",
                hidden: true,
                align: "center"
            }, {
                name: "name",
                label: "菜单名称",
                index: "name",
                sortable: true,
                width: 90,
                align: "center"
            }, {
                name: "level",
                label: "层级",
                index: "level",
                sortable: true,
                width: 90,
                align: "center"
            }, {
                name: "url",
                label: "URL",
                index: "url",
                sortable: true,
                width: 90,
                align: "center"
            }, {
                name: "oStatus",
                label: "操作",
                index: "oStatus",
                sortable: true,
                width: 140,
                align: "center",
                formatter: function (cellValue, options, rowObject) {
                    var id = rowObject.id;
                    var btns = [];
                    btns[btns.length] = '<button class="btn btn-xs" name="editProdClass" onclick="editProdClass(\'' + id + '\')"><i class="ace-icon fa fa-pencil-square-o"></i>编辑</button> &nbsp;&nbsp;';
                    btns[btns.length] = '<button class="btn btn-danger btn-xs" name="delProdClass" onclick="delProdClass(\'' + id + '\',\'' + rowObject.name + '\')"><i class="ace-icon fa fa-trash"></i>删除</button>';
                    return btns.join('');
                }
            }];
            i.markRow = [];
            queryConditions.parentId = $("#parentId").val();
            jQuery("#grid-table").jqGrid({
                url: "/menu/menuList.do",
                mtype: "post",
                postData: queryConditions,
                datatype: 'json',
                colModel: column,
                viewRecords: true,
                rowNum: 10,
                rowList: [10, 20, 30],
                pager: "#grid-pager",
                height: "auto",
                loadComplete: function () {
                    var table = this;
                    setTimeout(function () {
                        updatePagerIcons(table);
                        var sb = [];
                        sb[sb.length] = '<button class="btn btn-xs" title="修改"><i class="ace-icon fa fa-pencil-square-o"></i>修改</button>&nbsp;&nbsp;|&nbsp;&nbsp;';
                        sb[sb.length] = '<button class="btn btn-danger btn-xs" title="删除"><i class="ace-icon fa fa-trash"></i>删除</button> ';
                        $("#grid-pager_left").html(sb.join(''));
                    }, 0);
                    $("#grid-table").jqGrid('setGridWidth', $(".widget-box").width());
                },
                loadError: function (data) {
                    if (data.msg) {
                        layer.alert(data.msg, 2);
                    } else if (data.readyState != 0) {
                        layer.alert("操作失败了哦，请检查您的网络连接！", 2);
                    }
                }
            });
        },
        reloadData: function (t) {
            try {
                this.markRow = [];
                if (!t) {
                    queryConditions.parentId = $("#parentId").val();
                    t = queryConditions;
                }
                $("#grid-table").jqGrid("setGridParam", {
                    url: "/menu/menuList.do",
                    datatype: "json",
                    postData: t
                }).trigger("reloadGrid")
            } catch (e) {
                layer.alert("错误");
            }
        },
        addEvent: function () {
            $(window).on('resize.jqGrid', function () {
                $("#grid-table").jqGrid('setGridWidth', $(".widget-box").width());
            });
            $(window).triggerHandler('resize.jqGrid');
        }
    };

    //点击树节点触发的事件处理
    function clickMenuResult(data) {
        var lev = data.level;
        if(lev == 3){
            $("#addMenuDiv").hide();
        }else{
            $("#addMenuDiv").show();     //点击树节点，显示添加类目按钮
        }
        $("#parentId").val(data.id);
        $("#parentName").val("父菜单：" + data.name);
        THISPAGE.reloadData();
    }

    function initMenuTree() {
        var remoteUrl = '/menu/getTreeData.do';		//动态树数据请求接口
        var remoteDateSource = function (options, callback) {
            var parent_id = null;
            if (!('text' in options || 'type' in options)) {
                parent_id = 'root';
            }
            else if ('type' in options && options['type'] == 'folder') { //it has children
                if ('additionalParameters' in options && 'children' in options.additionalParameters)
                    parent_id = options.additionalParameters['id']
            }
            if (parent_id !== null) {
                $.ajax({
                    url: remoteUrl,
                    data: 'pId=' + parent_id,
                    type: 'POST',
                    dataType: 'json',
                    success: function (response) {
                        if (response.success){
                            callback({data: response.data})
                        }
                    },
                    error: function () {
                        console.log("错误");
                    }
                })
            }
        };
        $('#menuTree').aceTree({
            dataSource: remoteDateSource,
            multiSelect: false,
            loadingHTML: '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
            'open-icon': 'ace-icon tree-minus',
            'close-icon': 'ace-icon tree-plus',
            'selectable': false,
            'parentSelect': false,					//父节点全选 子节点是否全选
            'selected-icon': 'ace-icon fa fa-check',
            'unselected-icon': 'ace-icon fa fa-times',
            cacheItems: false,
            folderSelect: false
        });
        if (location.protocol == 'file:') {
            alert("For retrieving data from server, you should access this page using a webserver.");
        }
    }

    $('#menuTree')
            .on('clicked.fu.tree', function (e, result) {
                clickMenuResult(result.target.additionalParameters);
            })
            .on('opened.fu.tree', function (e, result) {
                clickMenuResult(result.additionalParameters);
            })
            .on('closed.fu.tree', function (e, result) {
                clickMenuResult(result.additionalParameters);
            });

    function addMenu(){
        //页面层
        parent.openPage("/menu/addMenu.do");
    }
</script>
</html>
