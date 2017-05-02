<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
<link rel="stylesheet" type="text/css" href="css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/htmleaf-demo.css">
<link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>
	<div class="htmleaf-container">
		<div class="demo form-bg">
			<div class="container">
				<div class="row">
					<div class="col-md-offset-3 col-md-6">
						<form class="form-horizontal" action="login" method="POST">
							<span class="heading">管理员登录</span>
							<div class="form-group">
								<input type="text" class="form-control" name="account"
									placeholder="用户名"> <i class="fa fa-user"></i>
							</div>
							<div class="form-group help">
								<input type="password" class="form-control" name="password"
									placeholder="密　码"> <i class="fa fa-lock"></i>
							</div>
							<div class="form-group">
								<c:if test="${null != error}">
									<span class="text" style="color: red;">账号或密码错误</span>
								</c:if>
								<button type="submit" class="btn btn-default">登录</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>