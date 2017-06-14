<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  User: ivenhf
  Date: 14-8-25
  Time: 下午5:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld" %>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
<meta name="renderer" content="webkit">
<c:set var="version" value='2016063001'></c:set>

<!-- bootstrap & fontawesome -->
<link rel="stylesheet" href="/resources/assets/css/ui.jqgrid.css"/>
<link rel="stylesheet" href="/resources/assets/css/bootstrap.css"/>
<link rel="stylesheet" href="/resources/assets/css/font-awesome.css"/>

<!-- page specific plugin styles -->

<!-- text fonts -->
<link rel="stylesheet" href="/resources/assets/css/ace-fonts.css"/>

<!-- ace styles -->
<link rel="stylesheet" href="/resources/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style"/>

<!--[if lte IE 9]>
<link rel="stylesheet" href="/resources/assets/css/ace-part2.css" class="ace-main-stylesheet" />
<![endif]-->

<!--[if lte IE 9]>
<link rel="stylesheet" href="/resources/assets/css/ace-ie.css" />
<![endif]-->

<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

<!--[if lte IE 8]>
<script src="/resources/assets/js/html5shiv.js"></script>
<script src="/resources/assets/js/respond.js"></script>
<![endif]-->
<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='/resources/assets/js/jquery.js'>" + "<" + "/script>");
</script>
<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
window.jQuery || document.write("<script src='/resources/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script src="/resources/js/common.js"></script>


