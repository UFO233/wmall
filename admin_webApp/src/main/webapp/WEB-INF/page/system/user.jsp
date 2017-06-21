<%--
  Created by IntelliJ IDEA.
  User: zcc
  Date: 2016/10/21
  Time: 20:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>系统管理-用户管理</title>
    <!-- page specific plugin styles -->
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
        <div class="col-xs-12">
            <div class="widget-box transparent">

                <form class="form-inline" role="form" onsubmit="THISPAGE.reloadData();return false;">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="input-group">
                                <input class="form-control" id="name" name="name" placeholder="请输入查询用户名称"/>
                            </div>
                            <div class="input-group">
                                <input class="form-control" id="mobile" name="mobile" placeholder="请输入查询用户手机号"/>
                            </div>
                            <div class="input-group">
                                <span class="input-group-btn">
                                    <button type="button" class="btn btn-purple btn-sm" onclick="THISPAGE.reloadData()">
                                        <span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
                                        查询
                                    </button>
                                </span>
                            </div>
                        </div>
                    </div>
                </form>
                <div class="hr hr12 dotted"></div>
                <table id="grid-table"></table>

                <div id="grid-pager"></div>
                <!-- PAGE CONTENT ENDS -->
            </div>
            <!-- /.col -->
        </div>
    </div>
    <!-- /.row -->
</div>
<!-- /.main-content -->

</div>
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

<!-- inline scripts related to this page -->
<script type="text/javascript">

     var queryConditions = {

     }, THISPAGE = {
            init: function () {
                this.initDom();
                this.loadGrid();
                this.addEvent()
            },
            initDom: function () {
            },
            loadGrid: function () {
                var i = this, a = [
                    {
                        name: "name",
                        label: "用户名称",
                        index: "name",
                        sortable: true,
                        width: 130,
                        align: "center"
                    },
                    {
                        name: "mobile",
                        label: "用户手机",
                        index: "mobile",
                        sortable: true,
                        width: 100,
                        align: "center"
                    },
                    {
                        name: "createTime",
                        label: "创建时间",
                        index: "createTime",
                        sortable: false,
                        width: 100,
                        align: "center"
                    }];
                i.markRow = [];
                jQuery("#grid-table").jqGrid({
                    url: "/sys/user/userData.do",
                    mType: "post",
                    postData: queryConditions,
                    datatype: "json",
                    colModel: a,
                    viewRecords: true,
                    rowNum: 10,
                    rowList: [10, 20, 30],
                    pager: "#grid-pager",
                    height: "auto",
                    loadComplete: function () {
                        var table = this;
                        $("#countDiv").hide();
                        $("#btnCount").val(true);
                        setTimeout(function () {
                            updatePagerIcons(table);
                        }, 0);
                        $("#grid-table").jqGrid('setGridWidth', $(".widget-box").width());
                    },
                    loadError: function (data) {
                        if (data.msg) {
                            layer.msg(data.msg, 2);
                        } else if (data.readyState != 0) {
                            layer.alert("操作失败了哦，请检查您的网络链接！", 2);
                        }
                    }

                });
            },
            reloadData: function (t) {
                try {
                    this.markRow = [];
                    if (!t) {
                        queryConditions.userId = $("#name").val();
                        queryConditions.endTime = $("#mobile").val();
                        t = queryConditions;
                    }
                    $("#grid-table").jqGrid("setGridParam", {
                        url: "/sys/user/userData.do",
                        dataType: "json",
                        postData: t
                    }).trigger("reloadGrid")
                } catch (e) {
                    alert(e);
                }
            }, addEvent: function () {
                $(window).on('resize.jqGrid', function () {
                    $("#grid-table").jqGrid('setGridWidth', $(".widget-box").width());
                });
                $(window).triggerHandler('resize.jqGrid');
            }
     };
    $(document).ready(function () {
        $(document).on('click', 'a[data-url]', function (e) {
            var dataurl = $(this).attr('data-url');
            parent.openPage(dataurl);
            return false;
        });
        THISPAGE.init();
    });
</script>
</body>
</html>