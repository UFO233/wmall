<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>Login Page - Ace Admin</title>
		<meta name="description" content="User login page" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<link rel="stylesheet" href="/resources/slide/css/slide-unlock.css" />
		<link rel="stylesheet" href="/resources/assets/css/bootstrap.css" />
		<link rel="stylesheet" href="/resources/assets/css/font-awesome.css" />
		<link rel="stylesheet" href="/resources/assets/css/ace-fonts.css" />
		<link rel="stylesheet" href="/resources/assets/css/ace.css" />
		<link rel="stylesheet" href="/resources/assets/css/ace-part2.css" />
		<link rel="stylesheet" href="/resources/assets/css/ace-rtl.css" />
		<link rel="stylesheet" href="/resources/assets/css/ace-ie.css" />
		<%@ include file="/WEB-INF/page/comm/taglibs.jsp" %>
		<script src="/resources/assets/js/html5shiv.js"></script>
		<script src="/resources/assets/js/respond.js"></script>
		<script src="/resources/slide/js/jquery.slideunlock.js"></script>
		<script src="/resources/js/lib/layer.js"></script>
		<%@ include file="/WEB-INF/page/comm/form.jsp" %>
	</head>
	<body class="login-layout light-login">
		<div class="main-container">
			<div class="main-content">
				<div class="row">
					<div class="col-sm-10 col-sm-offset-1">
						<div class="login-container">
							<div class="center">
								<h1>
									<i class="ace-icon fa fa-leaf green"></i>
									<span class="red">Ace</span>
									<span class="grey" id="id-text2">Application</span>
								</h1>
								<h4 class="blue" id="id-company-text">&copy; Tarena</h4>
							</div>
							<div class="space-6"></div>
							<div class="position-relative">
								<div id="login-box" class="login-box visible widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header blue lighter bigger">
												<i class="ace-icon fa fa-coffee green"></i>
												Please Enter Your Information
											</h4>
											<div class="space-6"></div>
											<form id="loginForm" role="form" data-validator-option="{theme:'yellow_right_effect',stopOnError:true}">
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" class="form-control" placeholder="手机号" name="loginName" value="18355373608" data-rule="手机号:required;loginName;length[5~50];"/>
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" class="form-control" placeholder="密码" name="loginPwd" value="123456" data-rule="密码:required;loginPwd;length[6~12];"/>
															<i class="ace-icon fa fa-lock"></i>
														</span>
													</label>
													<div id="slider">
														<div id="slider_bg"></div>
														<span id="label">>></span> <span id="labelTip">拖动滑块验证</span>
													</div>
													<div class="space"></div>
													<div class="clearfix">
														<label class="inline">
															<input type="checkbox" class="ace" />
															<span class="lbl"> 记住我</span>
														</label>
														<button type="submit" class="width-35 pull-right btn btn-sm btn-primary" id="login" disabled>
															<i class="ace-icon fa fa-key"></i>
															<span class="bigger-110">登录</span>
														</button>
													</div>
													<div class="space-4"></div>
												</fieldset>
											</form>
											<div class="social-or-login center">
												<span class="bigger-110">其他方式</span>
											</div>
											<div class="space-6"></div>
											<div class="social-login center">
												<a class="btn btn-primary">
													<i class="ace-icon fa fa-facebook"></i>
												</a>
												<a class="btn btn-info">
													<i class="ace-icon fa fa-twitter"></i>
												</a>
												<a class="btn btn-danger">
													<i class="ace-icon fa fa-google-plus"></i>
												</a>
											</div>
										</div><!-- /.widget-main -->
										<div class="toolbar clearfix">
											<div>
												<a href="#" data-target="#forgot-box" class="forgot-password-link">
													<i class="ace-icon fa fa-arrow-left"></i>
													忘记密码
												</a>
											</div>
											<div>
												<a href="#" data-target="#signup-box" class="user-signup-link">
													注册
													<i class="ace-icon fa fa-arrow-right"></i>
												</a>
											</div>
										</div>
									</div><!-- /.widget-body -->
								</div><!-- /.login-box -->
								<div id="forgot-box" class="forgot-box widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header red lighter bigger">
												<i class="ace-icon fa fa-key"></i>
												重设密码
											</h4>
											<div class="space-6"></div>
											<p>
												填写邮箱
											</p>
											<form>
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" class="form-control" placeholder="邮箱" />
															<i class="ace-icon fa fa-envelope"></i>
														</span>
													</label>
													<div class="clearfix">
														<button type="button" class="width-35 pull-right btn btn-sm btn-danger">
															<i class="ace-icon fa fa-lightbulb-o"></i>
															<span class="bigger-110">发送</span>
														</button>
													</div>
												</fieldset>
											</form>
										</div><!-- /.widget-main -->
										<div class="toolbar center">
											<a href="#" data-target="#login-box" class="back-to-login-link">
												返回登录
												<i class="ace-icon fa fa-arrow-right"></i>
											</a>
										</div>
									</div><!-- /.widget-body -->
								</div><!-- /.forgot-box -->
								<div id="signup-box" class="signup-box widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header green lighter bigger">
												<i class="ace-icon fa fa-users blue"></i>
												新用户注册
											</h4>
											<div class="space-6"></div>
											<p> 请填写详细信息: </p>
											<form>
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" class="form-control" placeholder="邮箱" />
															<i class="ace-icon fa fa-envelope"></i>
														</span>
													</label>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" class="form-control" placeholder="手机号" />
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" class="form-control" placeholder="密码" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
													</label>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" class="form-control" placeholder="确认密码" />
															<i class="ace-icon fa fa-retweet"></i>
														</span>
													</label>
													<label class="block">
														<input type="checkbox" class="ace" />
														<span class="lbl">
															我接受该
															<a href="#">用户协议</a>
														</span>
													</label>
													<div class="space-24"></div>
													<div class="clearfix">
														<button type="reset" class="width-30 pull-left btn btn-sm">
															<i class="ace-icon fa fa-refresh"></i>
															<span class="bigger-110">重置</span>
														</button>
														<button type="button" class="width-65 pull-right btn btn-sm btn-success">
															<span class="bigger-110">注册</span>
															<i class="ace-icon fa fa-arrow-right icon-on-right"></i>
														</button>
													</div>
												</fieldset>
											</form>
										</div>
										<div class="toolbar center">
											<a href="#" data-target="#login-box" class="back-to-login-link">
												<i class="ace-icon fa fa-arrow-left"></i>
												返回登录
											</a>
										</div>
									</div><!-- /.widget-body -->
								</div><!-- /.signup-box -->
							</div><!-- /.position-relative -->
							<div class="navbar-fixed-top align-right">
								<br />
								&nbsp;
								<a id="btn-login-dark" href="#">Dark</a>
								&nbsp;
								<span class="blue">/</span>
								&nbsp;
								<a id="btn-login-blur" href="#">Blur</a>
								&nbsp;
								<span class="blue">/</span>
								&nbsp;
								<a id="btn-login-light" href="#">Light</a>
								&nbsp; &nbsp; &nbsp;
							</div>
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div><!-- /.main-content -->
		</div><!-- /.main-container -->
		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='/resources/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='/resources/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {
				var slider = new SliderUnlock("#slider",{
					successLabelTip : "验证成功"
				},function(){
					$("#login").removeAttr("disabled");
				});
				slider.init();
				 $(document).on('click', '.toolbar a[data-target]', function(e) {
					e.preventDefault();
					var target = $(this).data('target');
					$('.widget-box.visible').removeClass('visible');//hide others
					$(target).addClass('visible');//show target
				 });
			});
			jQuery(function($) {
				 $('#btn-login-dark').on('click', function(e) {
					$('body').attr('class', 'login-layout');
					$('#id-text2').attr('class', 'white');
					$('#id-company-text').attr('class', 'blue');
					e.preventDefault();
				 });
				 $('#btn-login-light').on('click', function(e) {
					$('body').attr('class', 'login-layout light-login');
					$('#id-text2').attr('class', 'grey');
					$('#id-company-text').attr('class', 'blue');
					e.preventDefault();
				 });
				 $('#btn-login-blur').on('click', function(e) {
					$('body').attr('class', 'login-layout blur-login');
					$('#id-text2').attr('class', 'white');
					$('#id-company-text').attr('class', 'light-blue');
					e.preventDefault();
				 });
			});
			$(function(){
				$('#loginForm').bind('valid.form', function () {
					//防止重复提交
					$("#login").attr("disabled","disabled");
					$.ajax({
						url: '/admin/login.do',
						data: $("#loginForm").serialize(),
						type: 'POST',
						dataType: 'json',
						success: function (d) {
							if (d.success) {
								layer.alert(d.data);
								window.location.href="/menu/home.do";
							} else {
								layer.alert(d.msg, {
									icon: 2,
									skin: 'layer-ext-moon'
								});
								$("#login").removeAttr("disabled");
							}
						},
						error:function(){
							$("#login").removeAttr("disabled");
						}
					});

				});
			})
		</script>
	</body>
</html>
