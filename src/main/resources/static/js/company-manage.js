var companyManage = function() {
	cleanEasyUIContainer();
	$("#title").text("发行公司管理");
	$('#easy-ui-container').datagrid({
		url : "getPagedCompanys",
		toolbar : [ {
			iconCls : 'icon-add',
			handler : addCompany
		}, {
			iconCls : 'icon-remove',
			handler : removeCompany
		}, {
			iconCls : 'icon-edit',
			handler : editCompany
		} ],
		fitColumns : true,
		singleSelect : true,
		pagination : true,
		pageNumber : 1,
		pageSize : 9,
		pageList : [ 9, 27, 81 ],
		columns : [ [ {
			field : 'rcId',
			title : 'ID'
		}, {
			field : 'companyName',
			title : '公司名称'
		} ] ]
	});
	paginationFormat("easy-ui-container");
}
var removeCompany = function(id) {
	var company = $('#easy-ui-container').datagrid("getSelected");
	if (company == null) {
		alert("未选中要删除的公司");
		return;
	}
	$.ajax({
		url : "removeCompany",
		type : "POST",
		async : false,
		data : {
			rcId : company.rcId
		},
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
var addCompany = function() {
	var $form = $('<form class="form-horizontal" style="margin-left: 13%"> '
			+ '<div class="form-group"> '
			+ '<label class="col-sm-2 control-label">公司名称</label> '
			+ '<div class="col-sm-6"> '
			+ '<input type="text" class="form-control" name="name"> '
			+ '</div> ' + '</div> ' + '</form>');
	var form = $form[0];
	$("#modal .modal-title").text('添加公司');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(
			function() {
				var name = form.name.value;
				if (name === '')
					alert_add("#modal .modal-body", "请填写公司名称");
				else {
					$.ajax({
						url : "addCompany",
						type : "POST",
						async : false,
						data : {
							companyName : name
						},
						success : function(ret) {
							if (ret === 'FAILED') {
								alert_add("#modal .modal-body", "添加失败！错误未知");
							} else if (ret === 'SUCCESS') {
								$("#modal").modal("hide");
								dataReload();
							}
						},
						error : function(xhr, emess) {
							alert_add("#modal .modal-body", xhr.status + " : "
									+ emess);
						}
					});
				}
			});
}
var editCompany = function() {
	var company = $('#easy-ui-container').datagrid("getSelected");
	if (company == null) {
		alert("未选中要修改的公司");
		return;
	}
	var $form = $('<form class="form-horizontal" style="margin-left: 13%"> '
			+ '<div class="form-group"> '
			+ '<label class="col-sm-2 control-label">公司名称</label> '
			+ '<div class="col-sm-6"> '
			+ '<input type="text" class="form-control" name="name"> '
			+ '</div> ' + '</div> ' + '</form>');
	var form = $form[0];
	form.name.value = company.companyName;
	$("#modal .modal-title").text('添加公司');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(
			function() {
				var name = form.name.value;
				if (name === '')
					alert_add("#modal .modal-body", "请填写公司名称");
				else {
					$.ajax({
						url : "addCompany",
						type : "POST",
						async : false,
						data : {
							rcId : company.rcId,
							companyName : name
						},
						success : function(ret) {
							if (ret === 'FAILED') {
								alert_add("#modal .modal-body", "修改失败！错误未知");
							} else if (ret === 'SUCCESS') {
								$("#modal").modal("hide");
								dataReload();
							}
						},
						error : function(xhr, emess) {
							alert_add("#modal .modal-body", xhr.status + " : "
									+ emess);
						}
					});
				}
			});
}