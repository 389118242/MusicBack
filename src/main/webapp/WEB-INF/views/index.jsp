<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理中心</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/main.css">
<link rel="stylesheet" type="text/css"
	href="easy-ui/themes/metro/easyui.css">
<link rel="stylesheet" type="text/css" href="easy-ui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="css/index.css">
</head>
<body>
	<nav id="body-title" class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="index">后台管理系统</a>
			</div>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-expanded="false">${loginManager.name}
							<span class="caret"></span>
					</a>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#" data-toggle="modal"
								data-target="#alterPassword">修改密码</a></li>
								<li><a href="${frontUrl }" target="_blank">进入前台</a></li>
							<li class="divider"></li>
							<li><a href="logout">退出登录</a></li>
						</ul></li>
				</ul>
			</div>
		</div>
	</nav>
	<input id="front-url" hidden="hidden" value="${frontUrl }">
	<div id="body-content" class="container-fluid">
		<div class="row">
			<div class="col-md-2">
				<ul class="nav nav-pills nav-stacked">
					<li><a href="javascript:userManage();">用户管理</a></li>
					<c:if test="${loginManager.account == 'admin' }">
						<li><a href="javascript:managerManage();">管理员管理</a></li>
					</c:if>
					<li><a data-toggle="collapse" data-target="#song" href="#">
							歌曲相关 <span class="caret"></span>
					</a>
						<div id="song" class="collapse list-group-div">
							<ul style="margin-bottom: 0px; padding-bottom: 0px;"
								class="list-group">
								<li class="list-group-item"><a href="javascript:songManage();">歌曲管理</a></li>
								<li class="list-group-item"><a href="javascript:songCommManage();">歌曲评论</a></li>
								<li class="list-group-item"><a href="javascript:recommendSongManage();">推荐歌曲</a></li>
							</ul>
						</div></li>
					<li><a data-toggle="collapse" data-target="#song-list"
						href="#"> 歌单相关 <span class="caret"></span>
					</a>
						<div id="song-list" class="collapse list-group-div">
							<ul style="margin-bottom: 0px; padding-bottom: 0px;"
								class="list-group">
								<li class="list-group-item"><a href="javascript:songListManage();">歌单管理</a></li>
								<li class="list-group-item"><a href="javascript:songListCommManage();">歌单评论</a></li>
								<li class="list-group-item"><a href="javascript:listKindManage();">歌单种类</a></li>
								<li class="list-group-item"><a href="javascript:recommendSongListManage();">推荐歌单</a></li>
							</ul>
						</div></li>
					<li><a data-toggle="collapse" data-target="#album" href="#">
							专辑相关 <span class="caret"></span>
					</a>
						<div id="album" class="collapse list-group-div">
							<ul style="margin-bottom: 0px; padding-bottom: 0px;"
								class="list-group">
								<li class="list-group-item"><a href="javascript:albumManage();">专辑管理</a></li>
								<li class="list-group-item"><a href="javascript:albumCommManage();">专辑评论</a></li>
								<li class="list-group-item"><a href="javascript:singerManage();">歌手管理</a></li>
								<li class="list-group-item"><a href="javascript:companyManage();">发行公司</a></li>
							</ul>
						</div></li>
				</ul>
			</div>

			<div id="content-right" class="col-md-10">
				<h1 id="title" class="text-center"></h1>
				<div id="easy-ui-container"></div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="alterPassword" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="reset" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">修改密码</h4>
				</div>
				<div class="modal-body">
					<form id="alter-form" class="form-horizontal"
						style="margin-left: 13%">
						<div class="form-group">
							<label class="col-sm-2 control-label" for="name">当前密码</label>
							<div class="col-sm-6">
								<input type="password" class="form-control" id="old-pass">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="name">新密码</label>
							<div class="col-sm-6">
								<input type="password" class="form-control" id="new-pass-1">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="name">确认密码</label>
							<div class="col-sm-6">
								<input type="password" class="form-control" id="new-pass-2">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="reset" class="btn btn-default" data-dismiss="modal">关闭
					</button>
					<button type="submit" class="btn btn-primary">提交更改</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="modal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="reset" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
					<button type="button" id="submit" class="btn btn-primary">提交</button>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="easy-ui/jquery.min.js"></script>
	<script type="text/javascript" src="easy-ui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/utils.js"></script>
	<script type="text/javascript" src="js/user.js"></script>
	<c:if test="${loginManager.account == 'admin' }">
		<script type="text/javascript" src="js/manager.js"></script>
	</c:if>
	<script type="text/javascript" src="js/song-manage.js"></script>
	<script type="text/javascript" src="js/song-comment-manage.js"></script>
	<script type="text/javascript" src="js/recommend-song-manage.js"></script>
	<script type="text/javascript" src="js/songlist-manage.js"></script>
	<script type="text/javascript" src="js/songlist-comment-manage.js"></script>
	<script type="text/javascript" src="js/listkind-manage.js"></script>
	<script type="text/javascript" src="js/singer-manage.js"></script>
	<script type="text/javascript" src="js/album-manage.js"></script>
	<script type="text/javascript" src="js/album-comment-manage.js"></script>
	<script type="text/javascript" src="js/company-manage.js"></script>
	<script type="text/javascript" src="js/recommend-songlist-manage.js"></script>

	<script type="text/javascript">
		$(function() {
			$("#alterPassword").on("hidden.bs.modal", function() {
				$("#alter-form")[0].reset();
			});
			$("#modal").on("hidden.bs.modal", function() {
				$("#modal .modal-title").text('');
				$("#modal .modal-body").html('');
				$("#modal #submit").unbind();
				/**
				$("#modal .modal-title").text('');
				$("#modal .modal-body").append();
				$("#modal #submit").click(function(){
				});
				 */
			});
		});

		$("#alterPassword button[type=submit]").click(function() {
			var old_pass = $("#old-pass").val();
			var new_pass_1 = $("#new-pass-1").val();
			var new_pass_2 = $("#new-pass-2").val();

			if (old_pass === '' || new_pass_1 === '' || new_pass_2 === '') {
				alert_add("#alter-form", "请将数据填写完整");
				return;
			}
			if (new_pass_1 !== new_pass_2) {
				alert_add("#alter-form", "新密码两次输入不一致");
				return;
			}
			$.ajax({
				url : "alterPassword",
				type : "POST",
				async : false,
				data : {
					oldPass : old_pass,
					newPass : new_pass_1
				},
				success : function(ret) {
					if (ret === 'F') {
						alert_add("#alter-form", "修改密码失败！错误未知");
					} else if (ret === "E") {
						alert_add("#alter-form", "当前密码输入错误，请检查后输入");
					} else if (ret === 'S') {
						location.reload();
					}
				},
				error : function(xhr, emess) {
					alert_add(xhr.status + " : " + emess);
				}
			});
		});
	</script>
</body>
</html>