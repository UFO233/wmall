<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="dk" uri="/WEB-INF/datetag.tld"%>
<%
	if(request.getProtocol().compareTo("HTTP/1.0") == 0){
		response.setHeader("Pragma", "no-cache");
	}else if(request.getProtocol().compareTo("HTTP/1.1") == 0){
		response.setHeader("Cache-Control", "no-cache");
	}
	response.setDateHeader("Expires", 0);
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>测试</title>
		<base href="<%=basePath %>" />
		<script type="text/javascript" src="<%=basePath%>scripts/jquery-1.8.0.min.js"></script>
		<script language="javascript" type="text/javascript">
			function onCompleteFile(iframeId, savePath, saveName){
				alert("savePath===="+savePath);
				alert("saveName===="+saveName);
			}
			
			function reinitIframe(iframeId, minHeight, isOpera, isFireFox, isChrome, isSafari, isIE) {
			    try {
			        var iframe = document.getElementById(iframeId);
			        var bHeight = 0;
			        if (isChrome == false && isSafari == false)
			            bHeight = iframe.contentWindow.document.body.scrollHeight;

			        var dHeight = 0;
			        if (isFireFox == true)
			            dHeight = iframe.contentWindow.document.documentElement.offsetHeight + 2;
			        else if (isIE == false && isOpera == false)
			            dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
			        else if (isIE == true && ! -[1, ] == false) { } //ie9+
			        else
			            bHeight += 3;

			        var height = Math.max(bHeight, dHeight);
			        if (height < minHeight) height = minHeight;
			        iframe.style.height = height + "px";
			    } catch (ex) { }
			}
		</script>
	</head>
	<body >
		<div style="width:800px;">
			<p>-------------</p>
			<iframe id="uploadImgFrame" src="<%=basePath%>scripts/uploadify/uploadImg-V1.jsp?path=temp&strType=jpg;png;jpeg;bmp&fileSize=1000KB&fileDataId=fileData&isNeedPreview=true
			&multi=true&fileNumLimit=10&previewImgHeight=160px&originalFileNameLength=20&isZoom=true&zoomSize=200*400|-small;80*120|-normal;800*1200|-big
			&existImg=123|upload/temp/1458651879141.jpg|测试1测试1测试1测试1测试1测试1测试1测试1.jpg|jpg|1458651879141|125;124|upload/temp/1458652445196.jpg|测试2.jpg|jpg|1458652445196|215" 
			frameborder="0" marginwidth="0" marginheight="0" width="100%" scrolling="no"></iframe>
			<input  type="hidden" id="fileData" name="fileData" value="" />
			<p>-------------</p>
		</div>
		<div style="width:800px;">
			<p>-------------</p>
			<iframe id="uploadFileFrame" src="<%=basePath%>scripts/uploadify/uploadFile-V2.jsp?path=temp&strType=pdf;doc;docx;rar;zip&fileSize=1000KB&fileDataId=fileData2&isNeedPreview=true
			&multi=true&fileNumLimit=10&originalFileNameLength=80
			&existData=123|upload/temp/1458651879141.jpg|测试1测试1测试1测试1测试1测试1测试1测试1.jpg|jpg|1458651879141|125;124|upload/temp/1458652445196.jpg|测试2.jpg|jpg|1458652445196|215" 
			frameborder="0" marginwidth="0" marginheight="0" width="100%" scrolling="no"></iframe>
			<input  type="hidden" id="fileData2" name="fileData2" value="" />
			<p>-------------</p>
		</div>
	</body>
</html>
