<%--
  Created by IntelliJ IDEA.
  User: 东love
  Date: 2015/10/4
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>jqGrid - Ace Admin</title>

    <meta name="description" content="Dynamic tables and grids using jqGrid plugin"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <%@ include file="/WEB-INF/page/comm/taglibs.jsp" %>
    <%@ include file="/WEB-INF/page/comm/form.jsp" %>
</head>
<body class="content_page">
<div class="tabbable">

    <%--<div class="tab-content">--%>
        <div id="dropdown14" class="tab-pane in active">
            <div class="main-content">
                <div class="main-content-inner">
                    <!-- /section:basics/content.breadcrumbs -->
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="widget-box transparent">

                                <div class="widget-header widget-header-small">
                                    <h4 class="widget-title blue smaller"> 管理员添加 </h4>
                                </div>
                                <div class="space-10"></div>
                                <form class="form-horizontal" id="adduser" role="form"
                                      data-validator-option="{theme:'yellow_right_effect',stopOnError:true}">


                                    <div class="form-group">
                                        <label class="col-sm-3 control-label no-padding-right">用户名</label>

                                        <div class="col-sm-9">
                                            <div class="pos-rel">
                                                <input class="col-xs-7 col-sm-5" type="text" id="loginName" name="loginName" placeholder="请输入用户名"
                                                       data-rule="用户名:required;userName;length[5~16];xxx;"
                                                       data-rule-xxx="[/^[A-Za-z0-9]+$/, '必须为英文和数字组成']"
                                                />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label no-padding-right">昵称</label>

                                        <div class="col-sm-9">
                                            <div class="pos-rel">
                                                <input class="col-xs-7 col-sm-5" type="text" id="nickName" name="nickName" placeholder="请输入昵称"
                                                       data-rule="昵称:required;nickName;"
                                                />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label no-padding-right">手机号</label>

                                        <div class="col-sm-9">
                                            <div class="pos-rel">
                                                <input class="col-xs-7 col-sm-5" type="text" id="phone" name="phone" placeholder="请输入手机号"
                                                />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label no-padding-right">密码</label>

                                        <div class="col-sm-9">
                                            <div class="pos-rel">
                                                <input class="col-xs-7 col-sm-5" type="password" id="password" name="password" placeholder="请输入密码"
                                                       data-rule="密码:required;password;length[6~12]"
                                                />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label no-padding-right">确认密码</label>

                                        <div class="col-sm-9">
                                            <div class="pos-rel">
                                                <input class="col-xs-7 col-sm-5" type="password" id="cpassword" name="cpassword" placeholder="请确认密码"
                                                       data-rule="确认密码:required;cpassword;match[password];length[6~12]"/>

                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label no-padding-right">二级密码</label>

                                        <div class="col-sm-9">
                                            <div class="pos-rel">
                                                <input class="col-xs-7 col-sm-5" type="password" id="secondPwd" name="secondPwd" placeholder="请输入二级密码"
                                                       data-rule="二级密码:required;secondPwd;length[6~12]"
                                                />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label no-padding-right">确认二级密码</label>

                                        <div class="col-sm-9">
                                            <div class="pos-rel">
                                                <input class="col-xs-7 col-sm-5" type="password" id="csecondPwd" name="csecondPwd" placeholder="请确认二级密码"
                                                       data-rule="二级密码:required;csecondPwd;match[secondPwd];length[6~12]"/>

                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-3 control-label no-padding-right">选择部门</label>

                                        <div class="col-sm-9">
                                            <div class="pos-rel">
                                                <select class="col-xs-7 col-sm-5" id="deptId" name="deptId" >
                                                    <c:forEach items="${deptList}" var="d">
                                                        <c:choose>
                                                            <c:when test="${d.superDeptId=='0'}">
                                                                <option value="${d.deptId}">${d.deptName}</option>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <option value="${d.deptId}">&nbsp;&nbsp;➥ ${d.deptName}</option>
                                                            </c:otherwise>
                                                        </c:choose>

                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>


                                    <hr>
                                    <div class="col-md-offset-3 col-md-9">
                                        <button class="btn btn-info" type="submit" id="submit">
                                            <i class="ace-icon fa fa-check bigger-110"></i>
                                            确定
                                        </button>

                                        &nbsp; &nbsp; &nbsp;
                                        <button class="btn" type="button" onclick="doBack()">
                                            <i class="ace-icon fa fa-reply-all bigger-110"></i>
                                            返回
                                        </button>
                                    </div>
                                </form>

                            </div>
                            <!-- /.col -->
                        </div>
                    </div>
                    <!-- /.row -->
                </div>
            </div>
        </div>
    <%--</div>--%>
</div>

<!-- /section:basics/navbar.layout -->
<div class="" id="main-container">
    <!-- /section:basics/sidebar -->
    <div class="main-content">
    </div>
    <!-- /.main-content -->
</div>

<!-- inline scripts related to this page -->
<script type="text/javascript">
    $(document).ready(function () {

        $(document).on('click', 'a[data-url]', function (e) {
            var dataurl = $(this).attr('data-url');
            parent.openPage(dataurl);
            return false;
        });

        $('#adduser').bind('valid.form', function () {
            //防止重复提交
            $("#submit").attr("disabled","disabled");

            $.ajax({
                url: '/sys/admin/addAdminInfo.ahtml',
                data: $(this).serialize(),
                type: 'POST',
                dataType: 'json',
                success: function (d) {
                    if (d.success) {
                        parent.ezzalert("添加管理员成功!",1);
                        parent.openPage('/sys/admin/goAdminPage.ahtml');
                    } else {
                        parent.ezzalert(d.msg,2);
                    }
                    $("#submit").removeAttr("disabled");
                },
                error:function(){
                    $("#submit").removeAttr("disabled");
                }
            });

        });
    });

    function doBack(){
//    parent.layer.close(parent.layer.index);
        history.go(-1);
    }
</script>
</body>
</html>
</html>
