<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%-- 
功能：用于文件批量或单个上传或编辑
使用方法：
一、将本文件及uploadify文件夹复制到项目中，如果使用的jquery版本不同，注意替换改文件中的jquery包；
二、在web.xml文件中加入如下配置：
	<servlet>
		<servlet-name>uploadServlet</servlet-name>
		<servlet-class>com.dk.common.fileHandle.UploadFileServlet</servlet-class>
		<load-on-startup>3</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>uploadServlet</servlet-name>
		<url-pattern>/uploadServlet</url-pattern>
	</servlet-mapping>
	<context-param>
		<param-name>upTempPath</param-name>
		<param-value>F:/MarsWorkspace/youngor-wmall/wmall-app/src/main/webapp/upload/</param-value>
	</context-param>
	upTempPath为上传文件时保存的目录；
	其中UploadImgServlet在dk-common jar包中有，如果是maven工程，可依赖dk-common jar包即可。如果是其他工程，可将dk-common中的UploadServlet复制到项目中；
三、在需要上传文件的jsp页面中用iframe包含该文件，如下：
	<iframe id="" src="<%=basePath%>common/uploadImg.jsp?path=temp&strType=jpg;png;jpeg;bmp&fileSize=1000KB&fileDataId=fileData&isNeedPreview=true
		&multi=true&fileNumLimit=10&originalFileNameLength=10
		&existData=123|upload/temp/1458651879141.jpg|测试1测试1测试1测试1测试1测试1测试1测试1.jpg|jpg|1458651879141|125;124|upload/temp/1458652445196.jpg|测试2.jpg|jpg|1458652445196|215" 
		frameborder="0" marginwidth="0" marginheight="0" width="100%" scrolling="no"></iframe>
	<input  type="hidden" id="fileData" name="fileData" value="" />
	参数说明：
	1.path为上传文件后保存的临时目录，系统将会自动在upTempPath路径下创建名为path值的目录，例如：upTempPath为D:/upload/,path为temp，则文件上传后保存在D:/upload/temp/目录下；
	2.strType为限制上传的文件类型，多种类型用英文;号分隔；
	3.fileSize为上传文件的大小限制，单位可为B,KB,MB,GB；
	4.fileDataId为最后返回的数据存放html元素的id值，上传文件后会自动将结果保存到id为fileDataId值的html元素的value属性中。
	       最后返回的值格式为：文件ID|文件路径|原始文件名|文件类型|新的文件名|文件大小KB;文件路径|原始文件名|文件类型|新的文件名|文件大小KB;文件ID|文件路径|原始文件名|文件类型|新的文件名|文件大小KB;文件路径|原始文件名|文件类型|新的文件名|文件大小KB
	       即多个文件数据之间用英文;号分隔，文件的各参数间用|分隔，新上传的文件文件ID为空；
	5.isNeedPreview为是否需要展示上传结果，默认为true，在一些特殊场合可能需需要预览的，在父页面中也可以定义文件上传完成的回调函数onCompleteFile，在这个函数中对上传结果自行处理；如：
	function onCompleteFile(iframeId, savePath, saveName){
		alert("savePath===="+savePath);
		alert("saveName===="+saveName);
	}
	6.multi为是否允许上传多个文件，默认为false；
	7.fileNumLimit为限制文件上传的数量，每次上传的文件数量及总共允许上传的数量均为fileNumLimit；
	8.originalFileNameLength为文件列表时显示的原始文件名的长度限制，超出长度的自动截取，显得更加美观，默认为10，单位：字节数；
	9.existData为编辑已有文件时使用，方便预览系统已有的文件，将已有的数据按返回数据一样的格式传入；
四、父页面包含以下js方法，用户自适应高度
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
五、在各自的系统中实现方法<%=basePath%>/delFile.shtml，用于删除文件使用，在预览界面删除刚上传的文件或已有的文件。示例如下：
	/**
	 * 文件上传插件的删除文件方法.
	 * 考虑的每个系统保存文件的路径、方式不一样，例如是否使用ftp等情况，所以每个系统需要根据自身情况实现这个方法.
	 * 
	 * 返回：删除成功，则返回code:1;删除失败，则返回code:-1,msg:错误原因
	 */
	@RequestMapping(value = "/delFile")
	@ResponseBody
	public void delImg(ModelMap map, HttpServletRequest request, HttpServletResponse response) {
		String filePath = request.getParameter("filePath");
		String normalFilePath = filePath.substring(0, filePath.lastIndexOf("."));
		int startIndex = filePath.lastIndexOf(".") + 1;
        String fileType = filePath.substring(startIndex - 1);
		String fileId = request.getParameter("fileId");
		String suffix = request.getParameter("suffix");
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		if(filePath.contains("temp")){//处理新上传还没保存数据库的临时文件，这里是否为temp是需要根据页面传给插件的path参数而定，不要跟文件保存的正式目录一样，这样这里可以做区别处理
			//获取web.xml中配置的文件保存临时目录
			String upTempPath = request.getSession().getServletContext().getInitParameter("upTempPath");
			if(StringUtil.isNotEmpty(suffix)){
				for(String oneSuffix : suffix.split(";")){
					boolean delFlag = FileHandleUtil.deleteFile(upTempPath + normalFilePath + oneSuffix + fileType);
					if(delFlag){
						rtnMap.put("code", "1");
					} else {
						rtnMap.put("code", "-1");
						rtnMap.put("msg", "删除失败");
					}
				}
			}else{
				boolean delFlag = FileHandleUtil.deleteFile(upTempPath + filePath);
				if(delFlag){
					rtnMap.put("code", "1");
				} else {
					rtnMap.put("code", "-1");
					rtnMap.put("msg", "删除失败");
				}
			}
		}else{//处理已经保存数据库的临时文件，在编辑页面时删除文件
			//如果需要处理数据库的，可以根据fileId进行处理
			
		}
		
		this.writeJson(rtnMap, response);
	}

注意事项：
一、为防止系统被攻击，upTempPath需配置到项目外面的一个目录，开发环境及生产环境运行时做虚拟目录映射即可实现文件访问；
二、系统如果最终部署使用负载均衡，则表单提交后将临时目录的文件复制到ftp服务器，同时删除临时目录中的文件，访问是在前段的Apache或nginx中桌虚拟目录映射；
三、为防止系统垃圾文件越来越多，一定要实现delFile方法；文件复制到ftp服务器后一定要删除临时目录中的文件；表单最终没有提交，也需要删除临时目录中刚上传的文件；
四、父页面iframe的id值必须定义，用于自适应高度；
 --%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	request.setCharacterEncoding("UTF-8");
	
	//获取父页面传过来的文件夹
	String uploadPath = request.getParameter("path");
	//需要返回文件保存数据（格式：文件ID|文件路径|原始文件名|文件类型|新的文件名|文件大小KB;文件路径|原始文件名|文件类型|新的文件名|文件大小KB;）的input的id
	String fileDataId = "";
	//已存在的文件参数
	String existData = "";
	//是否需要展示文件上传结果
	boolean isNeedPreview = true;
	//是否允许上传多个文件
	String multi = "false";
	//允许上传的文件数量（当multi为true是有效）
	int fileNumLimit = 0;
	//显示原始文件名的长度限制，如果长度超过设置值，则自动截取长度
	int originalFileNameLength = 10;
	
	String strType = "";
	String fileSize = "";
	String[] fileTypes = null;
	String fileType = "";
	if (request.getParameter("fileDataId") != null) {
		fileDataId = request.getParameter("fileDataId").toString();
	}

	if (request.getParameter("strType") != null) {
		strType = request.getParameter("strType").toString();
		fileTypes = strType.split(";");
		for (int i = 0; i < fileTypes.length; i++) {
			if (i != (fileTypes.length - 1)) {
				fileType += "*." + fileTypes[i] + ";";
			} else {
				fileType += "*." + fileTypes[i];
			}
		}
	}
	
	if (request.getParameter("fileSize") != null) {
		fileSize = request.getParameter("fileSize").toString();
	}
	
	if (request.getParameter("existData") != null) {
		existData = new String(request.getParameter("existData").getBytes("ISO8859-1"), "UTF-8"); 
	}
	
	if (request.getParameter("isNeedPreview") != null) {
		isNeedPreview = Boolean.valueOf(request.getParameter("isNeedPreview").toString());
	}
	
	if (request.getParameter("multi") != null) {
		multi = request.getParameter("multi").toString();
	}
	
	if (request.getParameter("fileNumLimit") != null) {
		fileNumLimit = Integer.parseInt(request.getParameter("fileNumLimit"));
	}
	
	if (request.getParameter("originalFileNameLength") != null) {
		originalFileNameLength = Integer.parseInt(request.getParameter("originalFileNameLength"));
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>上传文件插件</title>
		<link href="<%=basePath%>/scripts/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
		<link href="<%=basePath%>/scripts/uploadify/upFiles.css" rel="stylesheet" type="text/css" />
		<script src="<%=basePath%>/scripts/jquery-1.8.0.min.js" type="text/javascript"></script>
		<script type="text/javascript" src="<%=basePath%>/scripts/uploadify/jquery.uploadify.min.js"></script>
		<script language="javascript" type="text/javascript">
			//文件当前序号，从0开始
   	 		var curIndex=0;
   	 		//文件数据数值，格式：文件ID|文件路径|原始文件名|文件类型|新的文件名|文件大小KB;文件路径|原始文件名|文件类型|新的文件名|文件大小KB
       	 	//新上传文件文件ID为空
       	 	var existDataArr = [];
			$(document).ready(function(){
            	 function ForDight(Dight,How){  
                     Dight = Math.round(Dight*Math.pow(10,How))/Math.pow(10,How);  
                     return Dight;  
                 }  
            	             	 
            	 var existData = '<%=existData %>';
            	 if(existData != ""){
            		 existDataArr = existData.split(";");
            		 if(existDataArr.length > 0){
            			 var html = "";
            			 for(var i=0;i<existDataArr.length;i++){
            				 if(existDataArr[i] != ""){
            					 var oneArr = existDataArr[i].split("|");
            					 var oriName = oneArr[2].substring(0, oneArr[2].lastIndexOf("."));
            					 oriName = autoAddEllipsis(oriName, <%=originalFileNameLength%>);
            					 html += "<tr id='preview-" + curIndex + "' data-fileindex='" + curIndex + "'>"
            					 			+ "<td align='center' width='30px'>"+Number(curIndex + 1) +"</td>"
            					 			+ "<td style='display:none;'>"+oneArr[0]+"</td>"
            					 			+ "<td style='display:none;'>"+oneArr[1]+"</td>"
            					 			+ "<td style='display:none;'>"+oneArr[4]+"</td>"
            					 			+ "<td><label title='" + oneArr[2] + "'>"+oriName+"</label></td>"
            					 			+ "<td width='60px'>"+oneArr[3]+"</td>"
            					 			+ "<td width='88px'>"+oneArr[5]+"</td>"
            					 			+ "<td width='30px'><button class='btn btn-danger'><i class='icon-trash icon-white'></i></button></td>"
            			          		+ "</tr>";
            					 curIndex += 1;
            				 }
            			 }
            			 $("#previewArea").append(html);
            			 outputFileData();
            		 }
            	 }
            	 
            	outputFileData();
            	 
            	            	 
             	$("#file_upload").uploadify({
             		
    				//接受true or false两个值，当为true时选择文件后会自动上传；为false时只会把选择的文件增加进队列但不会上传，这时只能使用upload的方法触发上传。不设置auto时默认为true
    				auto:true, 
    				//设置上传按钮的class
    				buttonClass: "", 
    				//设置鼠标移到按钮上的开状，接受两个值'hand'和'arrow'(手形和箭头)
    				buttonCursor: 'hand',
    				//设置图片按钮的路径（当你的按钮是一张图片时）。如果使用默认的样式，你还可以创建一个鼠标悬停状态，但要把两种状态的图片放在一起，并且默认的放上面，悬停状态的放在下面（原文好难表达啊：you can create a hover state for the button by stacking the off state above the hover state in the image）。这只是一个比较便利的选项，最好的方法还是把图片写在CSS里面。
    				buttonImage: '<%=basePath%>/scripts/uploadify/img/browse-btn.png', 
    				//设置按钮文字。值会被当作html渲染，所以也可以包含html标签
    				buttonText: '<div>选择文件</div>',
    				//取消按钮图片
    				//cancelImage: '<%=basePath%>/scripts/uploadify/img/cancel-btn.png', 
    				//开启或关闭debug模式
    				debug: false,
    				//设置在后台脚本使用的文件名。举个例子，在php中，如果这个选项设置为'the_files',你可以使用$_FILES['the_files']存取这个已经上传的文件。
    				fileObjName:'filedata',
    				//设置上传文件的容量最大值。这个值可以是一个数字或者字符串。如果是字符串，接受一个单位（B,KB,MB,GB）。如果是数字则默认单位为KB。设置为0时表示不限制
    				fileSizeLimit: '<%=fileSize%>',
    				//设置允许上传的文件扩展名（也就是文件类型）。但手动键入文件名可以绕过这种级别的安全检查，所以你应该始终在服务端中检查文件类型。输入多个扩展名时用分号隔开('*.jpg;*.png;*.gif')
    				fileTypeExts: '<%=fileType%>',
    				//可选文件的描述。这个值出现在文件浏览窗口中的文件类型下拉选项中。（但我设置了好像没效果？原文：The description of the selectable files.  This string appears in the browse files dialog box in the file type drop down.）
    				fileTypeDesc: '<%=fileType%>',
    				//通过get或post上传文件时，此对象提供额外的数据。如果想动态设置这些值，必须在onUploadStartg事件中使用settings的方法设置。
    				formData: {
    					timestamp: '',
    					token: ''
    				},
    				//设置按钮的高度(单位px)，默认为30.(不要在值里写上单位，并且要求一个整数，width也一样)
    				height: 30,
    				//设置按钮宽度(单位px)，默认120
    				width: 120,
    				//模板对象。给增加到上传队列中的每一项指定特殊的html模板。模板格式请看官网http://www.uploadify.com/documentation/uploadify/itemtemplate/
    				itemTemplate:false,
    				//提交上传文件的方法，接受post或get两个值，默认为post
    				method:'post',
    				//设置是否允许一次选择多个文件，true为允许，false不允许
    				multi: <%=multi%>,
    				//是否缓存swf文件。默认为true，会给swf的url地址设置一个随机数，这样它就不会被缓存。(有些浏览器缓存了swf文件就会触发不了里面的事件--by rainweb)
    				preventCaching:true,
    				//设置文件上传时显示数据，有‘percentage’ or ‘speed’两个参数(百分比和速度)
    				progressData: 'percentage',
    				//设置上传队列DOM元素的ID，上传的项目会增加进这个ID的DOM中。设置为false时则会自动生成队列DOM和ID。默认为false
    				queueID: false,
    				//设置每一次上传队列中的文件数量。注意并不是限制总的上传文件数量（那是uploadLimit）.如果增加进队列中的文件数量超出这个值，将会触发onSelectError事件。默认值为999
    				queueSizeLimit: <%=fileNumLimit%>,
    				//是否移除掉队列中已经完成上传的文件。false为不移除
    				removeCompleted: true,
    				//设置上传完成后删除掉文件的延迟时间，默认为3秒。如果removeCompleted为false的话，就没意义了
    				removeTimeout: 1,
    				//设置上传过程中因为出错导致上传失败的文件是否重新加入队列中上传
    				requeueErrors: false,
    				//设置文件上传后等待服务器响应的秒数，超出这个时间，将会被认为上传成功，默认为30秒
    				successTimeout: 30,
    				//swf的相对路径，必写项
    				swf: '<%=basePath%>/scripts/uploadify/uploadify.swf',
    				//服务器端脚本文件路径，必写项
    				uploader: '<%=basePath%>/uploadFileServlet?params=<%=uploadPath%>:',
    				//上传文件的数量。达到或超出这数量会触发onUploadError方法。默认999
    				uploadLimit: 20,
    				/***Event****/
    				//文件被移除出队列时触发,返回file参数
    				onCancel: function(file){
    					console.log('The file'+ file.name + 'was cancelled.')
    				},
    				//当调用cancel方法且传入'*'这个参数的时候触发，其实就是移除掉整个队列里的文件时触发，上面有说cancel方法带*时取消整个上传队列
    				onClearQueue: function(queueItemCount){
    					console.log(queueItemCount+'file(s) were removed frome the queue')
    				},
    				//调用destroy方法的时候触发
    				onDestroy: function(){
    					
    				},
    				//关闭掉浏览文件对话框时触发。返回queueDate参数，有以下属性：
    				/*
    					filesSelected 浏览文件对话框中选取的文件数量
    					filesQueued 加入上传队列的文件数
    					filesReplaced 被替换的文件个数
    					filesCancelled 取消掉即将加入队列中的文件个数
    					filesErrored 返回错误的文件个数
    				*/
    				onDialogClose: function(queueData){
    					console.log(queueData.filesSelected+'\n'+queueData.filesQueued+'\r\n'+queueData.filesReplaced+'\r\n'+queueData.filesCancelled+'\r\n'+ queueData.filesErrored)
    				},
    				//打开选择文件对话框时触发
    				onDialogOpen:function(){
    					
    				},
    				//禁用uploadify时触发(通过disable方法)
    				onDisable:function(){
    					
    				},
    				//启用uploadify时触发(通过disable方法)
    				onEnalbe: function(){
    					
    				},
    				//在初始化时检测不到浏览器有兼容性的flash版本时实触发
    				onFallback:function(){
    					
    				},
    				//每次初始化一个队列时触发，返回uploadify对象的实例
    				onInit: function(instance){
    					console.log('The queue ID is'+ instance.settings.queueID);
    				},
    				//队列中的文件都上传完后触发，返回queueDate参数，有以下属性：
    				/*
    					uploadsSuccessful 成功上传的文件数量
    					uploadsErrored 出现错误的文件数量
    				*/
    				onQueueComplete:function(queueData){
    					console.log(queueData.uploadsSuccessful+'\n'+queueData.uploadsErrored);
    				},
    				//选择每个文件增加进队列时触发，返回file参数
    				onSelect: function(file){
    					console.log(file.name);
    				},
    				//选择文件出错时触发，返回file,erroCode,errorMsg三个参数
    				/*
    					errorCode是一个包含了错误码的js对象，用来查看事件中发送的错误码，以确定错误的具体类型，可能会有以下的常量：
    					QUEUE_LIMIT_EXCEEDED:-100 选择的文件数量超过设定的最大值；
    					FILE_EXCEEDS_SIZE_LIMIT:-110 文件的大小超出设定
    					INVALID_FILETYPE:-130 选择的文件类型跟设置的不匹配

    					errorMsg 完整的错误信息，如果你不重写默认的事件处理器，可以使用‘this.queueData.errorMsg’ 存取完整的错误信息
    				*/
    				onSelectError: function(file,errorCode,errorMsg){
    					var msgText = "上传失败\n";
    					switch (errorCode) {
    					case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
    						// this.queueData.errorMsg = "每次最多上传 " +
    						// this.settings.queueSizeLimit + "个文件";
    						msgText += "上传的文件数量已经超出系统限制的" + $('#file_upload').uploadify('settings', 'queueSizeLimit') + "个文件！";
    						break;
    					case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
    						msgText += "文件 [" + file.name + "] 大小超出系统限制的" + $('#file_upload').uploadify('settings', 'fileSizeLimit') + "大小！";
    						break;
    					case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
    						msgText += "文件大小为0";
    						break;
    					case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
    						msgText += "文件格式不正确，仅限 " + this.settings.fileTypeExts;
    						break;
    					default:
    						msgText += "错误代码：" + errorCode + "\n" + errorMsg;
    					}
    					alert(msgText);
    				},
    				//swf动画加载完后触发，没有参数
    				onSWFReady: function(){
    					
    				},
    				//在每一个文件上传成功或失败之后触发，返回上传的文件对象或返回一个错误，如果你想知道上传是否成功，最后使用onUploadSuccess或onUploadError事件
    				onUploadComplete: function(file){
    					
    				},
    				//一个文件完成上传但返回错误时触发，有以下参数
    				/*
    					file 完成上传的文件对象
    					errorCode 返回的错误代码
    					erorMsg 返回的错误信息
    					errorString 包含所有错误细节的可读信息
    				*/
    				onUploadError: function(file,errorCode,erorMsg,errorString){
    					// 手工取消不弹出提示
    					if (errorCode == SWFUpload.UPLOAD_ERROR.FILE_CANCELLED || errorCode == SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED) {
    						return;
    					}
    					var msgText = "上传失败\n";
    					switch (errorCode) {
    					case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
    						msgText += "HTTP 错误\n" + errorMsg;
    						break;
    					case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
    						msgText += "上传文件丢失，请重新上传";
    						break;
    					case SWFUpload.UPLOAD_ERROR.IO_ERROR:
    						msgText += "IO错误";
    						break;
    					case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
    						msgText += "安全性错误\n" + errorMsg;
    						break;
    					case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
    						msgText += "每次最多上传 " + this.settings.uploadLimit + "个";
    						break;
    					case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
    						msgText += errorMsg;
    						break;
    					case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
    						msgText += "找不到指定文件，请重新操作";
    						break;
    					case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
    						msgText += "参数错误";
    						break;
    					default:
    						msgText += "文件:" + file.name + "\n错误码:" + errorCode + "\n" + errorMsg + "\n" + errorString;
    					}
    					alert(msgText);
    				},
    				//每更新一个文件上传进度的时候触发,返回以下参数
    				/*
    					file 正上传文件对象
    					bytesUploaded 文件已经上传的字节数
    					bytesTotal 文件的总字节数
    					totalBytesUploaded 在当前上传的操作中（所有文件）已上传的总字节数
    					totalBytesTotal 所有文件的总上传字节数
    				*/
    				onUploadProgress: function(file,bytesUploaded,bytesTotal,totalBytesUploaded,totalBytesTotal){
    					$('#pregress').html('总共需要上传'+bytesTotal+'字节，'+'已上传'+totalBytesTotal+'字节');
    				},
    				//每个文件即将上传前触发
    				onUploadStart: function(file){
    					console.log('start update');
    				},
    				//每个文件上传成功后触发
    				onUploadSuccess: function(file,resData,resultFlag){
    					if(resultFlag){
    						var saveName = resData.substring(resData.lastIndexOf("/")+1).replace(file.type,"");
	       					var oriName = file.name.substring(0, file.name.lastIndexOf("."));
       					 	oriName = autoAddEllipsis(oriName, <%=originalFileNameLength%>);
    						var html = "<tr id='preview-" + curIndex + "' data-fileindex='" + curIndex + "'>"
							 			+ "<td align='center' width='30px'>"+Number(curIndex + 1) +"</td>"
							 			+ "<td style='display:none;'></td>"
							 			+ "<td style='display:none;'>"+resData+"</td>"
							 			+ "<td style='display:none;'>"+saveName+"</td>"
							 			+ "<td><label title='" + file.name + "'>"+oriName+"</label></td>"
							 			+ "<td width='60px'>"+file.type.substring(1)+"</td>"
							 			+ "<td width='88px'>"+ForDight(Number(file.size/1024),2)+"</td>"
							 			+ "<td width='30px'><button class='btn btn-danger' path='" + resData + "' fileId='' fileIndexId='preview-" + curIndex + "' onclick='delFile(this);'><i class='icon-trash icon-white'></i></button></td>"
					          		+ "</tr>";
 						    $("#previewArea").append(html);
 						   	existDataArr[curIndex] = "|" + resData + "|" + file.name + "|" + file.type.substring(1) + "|" + saveName + "|" + ForDight(Number(file.size/1024),2);
 						   	curIndex += 1; 						   	
 						   	outputFileData();
 						   	if(typeof(window.parent.OnCompleteFile)=="function"){
						   		window.parent.onCompleteFile(window.frameElement.id,resData,saveName);
						  	}
    					} else {
    						alert("上传失败了");
    					}
    					
    				}    				
				});
             	
             	startInit(window.frameElement.id, 160);
			});
          	
          	//删除文件方法，先ajax删除正式文件或临时文件，再删除页面数据	 
        	function delFile(obj){
          		var statu = confirm("确定要删除吗?");
       			if(statu){
       				var file_path = $(obj).attr("path");
    				var fileId = $(obj).attr("fileId");
    				var fileIndexId = $(obj).attr("fileIndexId");
            		var fileIndex = fileIndexId.substring(fileIndexId.length - 1);
    				$.ajax({
    					url:"<%=basePath%>/delFile.shtml",
    					data:{'filePath':file_path,"fileId":fileId},
    					type:"POST",
    					dataType:'json',
    					success:function(data){
    						var code = data.code;
    						if (code == "-1") {
    							alert(data.msg);
    						}else{
    							$("#"+fileIndexId).remove();
    							if(fileIndex != Number(curIndex) - 1){
    								for(var i = Number(fileIndex) + 1; i < curIndex; i++){
    									existDataArr[i-1] = existDataArr[i];
    									if(i == Number(curIndex - 1)){
    										existDataArr[i] = "";
    									}
    									$("#preview-"+i).attr("data-fileindex", Number(i - 1));
    									$("#preview-"+i).find("td:first").html(i);
    									$("#preview-"+i).find("button").attr("fileIndexId", "preview-" + Number(i - 1));
    									$("#preview-"+i).attr("id", "preview-" + Number(i - 1));
    								}
    							}else{
    								existDataArr[fileIndex] = "";
    							}
    							outputFileData();
        						curIndex--;
    						}
    						
    					}
    				});
       			}
       		}
          	
        	//输出文件组合数据到父页面元素中
        	function outputFileData(){
        		var dataStr = "";
        		if("<%=fileDataId%>" != "" && existDataArr.length > 0){
        			for(var i=0;i<existDataArr.length;i++){
        				if(existDataArr[i] != ""){
        					dataStr += existDataArr[i] + ";"
        				}        				
        			}
        			if(dataStr != ""){
        				dataStr = dataStr.substring(0,dataStr.length -1);
        			}
                	$("#<%=fileDataId%>", window.parent.document).val(dataStr);
                }
        		if(existDataArr.length == 0 || !<%=isNeedPreview%> || dataStr == ""){
        			$("#file-preview").addClass("hide");
        		}else{
        			$("#file-preview").removeClass("hide");
        		}
        	}
        	
        	function startInit(iframeId, minHeight) {
				var browserVersion = window.parent.navigator.userAgent.toUpperCase();
				var isOpera = false, isFireFox = false, isChrome = false, isSafari = false, isIE = false;
			    isOpera = browserVersion.indexOf("OPERA") > -1 ? true : false;
			    isFireFox = browserVersion.indexOf("FIREFOX") > -1 ? true : false;
			    isChrome = browserVersion.indexOf("CHROME") > -1 ? true : false;
			    isSafari = browserVersion.indexOf("SAFARI") > -1 ? true : false;
			    if (!!window.parent.ActiveXObject || "ActiveXObject" in window.parent)
			        isIE = true;
			    window.parent.setInterval("reinitIframe('" + iframeId + "'," + minHeight + "," + isOpera + "," + isFireFox + "," + isChrome + "," + isSafari + "," + isIE + ")", 100);
			} 
        	
        	/* 
        	 * 处理过长的字符串，截取并添加省略号 
        	 * 注：半角长度为1，全角长度为2 
        	 *  
        	 * pStr:字符串 
        	 * pLen:截取长度 
        	 *  
        	 * return: 截取后的字符串 
        	 */  
        	function autoAddEllipsis(pStr, pLen) {        	  
        	    var _ret = cutString(pStr, pLen);  
        	    var _cutFlag = _ret.cutflag;  
        	    var _cutStringn = _ret.cutstring;  
        	  
        	    if ("1" == _cutFlag) {  
        	        return _cutStringn + "...";  
        	    } else {  
        	        return _cutStringn;  
        	    }  
        	}  
        	  
        	/* 
        	 * 取得指定长度的字符串 
        	 * 注：半角长度为1，全角长度为2 
        	 *  
        	 * pStr:字符串 
        	 * pLen:截取长度 
        	 *  
        	 * return: 截取后的字符串 
        	 */  
        	function cutString(pStr, pLen) {  
        	  
        	    // 原字符串长度  
        	    var _strLen = pStr.length;  
        	  
        	    var _tmpCode;  
        	  
        	    var _cutString;  
        	  
        	    // 默认情况下，返回的字符串是原字符串的一部分  
        	    var _cutFlag = "1";  
        	  
        	    var _lenCount = 0;  
        	  
        	    var _ret = false;  
        	  
        	    if (_strLen <= pLen/2) {  
        	        _cutString = pStr;  
        	        _ret = true;  
        	    }  
        	  
        	    if (!_ret) {  
        	        for (var i = 0; i < _strLen ; i++ ) {  
        	            if (isFull(pStr.charAt(i))) {  
        	                _lenCount += 2;  
        	            } else {  
        	                _lenCount += 1;  
        	            }  
        	  
        	            if (_lenCount > pLen) {  
        	                _cutString = pStr.substring(0, i);  
        	                _ret = true;  
        	                break;  
        	            } else if (_lenCount == pLen) {  
        	                _cutString = pStr.substring(0, i + 1);  
        	                _ret = true;  
        	                break;  
        	            }  
        	        }  
        	    }  
        	      
        	    if (!_ret) {  
        	        _cutString = pStr;  
        	        _ret = true;  
        	    }  
        	  
        	    if (_cutString.length == _strLen) {  
        	        _cutFlag = "0";  
        	    }  
        	  
        	    return {"cutstring":_cutString, "cutflag":_cutFlag};  
        	}  
        	  
        	/* 
        	 * 判断是否为全角 
        	 *  
        	 * pChar:长度为1的字符串 
        	 * return: true:全角 
        	 *          false:半角 
        	 */  
        	function isFull (pChar) {  
        	    if ((pChar.charCodeAt(0) > 128)) {  
        	        return true;  
        	    } else {  
        	        return false;  
        	    }  
        	}
        	
		</script>
	</head>
	<body>
	    <div id="file-preview">
	    	<table class="table table-striped table-bordered table-hover table-condensed">
		        <thead>
		          	<tr>
		            	<th>序号</th>
		            	<th>文件名</th>
		            	<th>文件类型</th>
		            	<th>文件大小(KB)</th>
		            	<th></th>
		          	</tr>
		        </thead>
		        <tbody id="previewArea">
		        </tbody>
		  	</table>
		  	<input id="file_upload" name="file_upload" type="file" class=""/>
	    </div>
	</body>
</html>
