var managerManage = function() {
	cleanEasyUIContainer();
	$("#title").text("管理员管理");
	$('#easy-ui-container').datagrid({
		url : "getManagers",
		toolbar : [ {
			iconCls : 'icon-add',
			handler : addManager
		} ],
		fitColumns : true,
		singleSelect : true,
		columns : [ [ {
			field : 'id',
			title : 'ID'
		}, {
			field : 'account',
			title : '账号'
		}, {
			field : 'name',
			title : '昵称'
		}, {
			field : 'password',
			title : '操作',
			formatter : operationFormat
		} ] ]
	});
}
var operationFormat = function(value, row, index) {
	return "<a href='javascript:void(0);' onclick='removeManager(" + row.id
			+ ")'>刪除</a>";
}
var removeManager = function(id) {
	$.ajax({
		url : "removeManager",
		type : "POST",
		async : false,
		data : {
			id : id
		},
		dataType : "json",
		success : function(ret) {
			if (ret) {
				dataReload();
			} else {
				alert("删除失败");
			}
		},
		error : function(xhr, emess) {
			alert(xhr.status + " : " + emess);
		}
	});
}
var addManager = function() {
	var $form = $('<form class="form-horizontal" style="margin-left: 13%"> '
			+ '<div class="form-group"> '
			+ '<label class="col-sm-2 control-label">账号</label> '
			+ '<div class="col-sm-6"> '
			+ '<input type="text" class="form-control" name="account"> '
			+ '</div> '
			+ '</div> '
			+ '<div class="form-group"> '
			+ '<label class="col-sm-2 control-label">姓名</label> '
			+ '<div class="col-sm-6"> '
			+ '<input type="text" class="form-control" name="name"> '
			+ '</div> '
			+ '</div> '
			+ '<div class="form-group"> '
			+ '<label class="col-sm-2 control-label">密码</label> '
			+ '<div class="col-sm-6"> '
			+ '<input type="password" class="form-control" name="password"> '
			+ '</div> ' + '</div> ' + '</form>');
	var form = $form[0];
	$("#modal .modal-title").text('添加管理员');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(function() {
		var account = form.account.value;
		var name = form.name.value;
		var password = form.password.value;
		if (account === '' || name === '' || password === '')
			alert_add("#modal .modal-body", "请填写完整的表单数据");
		else {
			$.ajax({
				url : "addManager",
				type : "POST",
				async : false,
				data : {
					account : account,
					name : name,
					pass : password
				},
				success : function(ret) {
					if (ret === 'FAILED') {
						alert_add("#modal .modal-body", "添加失败！错误未知");
					} else if (ret === "EXISTS") {
						alert_add("#modal .modal-body", "账号已存在");
					} else if (ret === 'SUCCESS') {
						$("#modal").modal("hide");
						dataReload();
					}
				},
				error : function(xhr, emess) {
					alert_add("#modal .modal-body", xhr.status + " : " + emess);
				}
			});
		}
	});
}