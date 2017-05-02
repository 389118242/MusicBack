var listKindManage = function() {
	cleanEasyUIContainer();
	$("#title").text("歌单种类管理");
	$('#easy-ui-container').datagrid({
		url : "getListKinds",
		toolbar : [ {
			iconCls : 'icon-add',
			handler : addListKind
		}, {
			iconCls : 'icon-remove',
			handler : removeListKind
		} ],
		fitColumns : true,
		singleSelect : true,
		pagination : true,
		pageNumber : 1,
		pageSize : 9,
		pageList : [ 9, 27, 81 ],
		columns : [ [ {
			field : 'kindId',
			title : 'ID'
		}, {
			field : 'kindName',
			title : '歌名'
		}] ]
	});
	paginationFormat("easy-ui-container");
}
var addListKind = function() {
	var $form = $('<form class="form-horizontal" style="margin-left: 13%"><div class="form-group">'
			+ '<label class="col-sm-2 control-label">类型</label>'
			+ '<div class="col-sm-6">'
			+ '<input name="kindName" class="form-control">'
			+ '</div>'
			+ '</div></form>');
	$("#modal .modal-title").text('添加歌单种类');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(function() {
		var form = $form[0];
		var kindName = form.kindName.value;
		if (kindName.trim() === '') {
			alert_add("#modal .modal-body", "请填写类型");
			return;
		}
		$.ajax({
			url : "addListKind",
			type : "POST",
			async : false,
			data : {
				kindName : kindName
			},
			success : function(ret) {
				if (ret == "SUCCESS") {
					dataReload();
					$("#modal").modal("hide");
				} else {
					alert_add("#modal .modal-body", ret);
				}
			},
			error : function(xhr, emess) {
				alert_add("#modal .modal-body", xhr.status + " : " + emess);
			}
		});
	});
}
var removeListKind = function() {
	var row = $('#easy-ui-container').datagrid("getSelected");
	if (row == null) {
		alert("未选中要删除的歌单种类");
		return;
	}
	var kindId = row.kindId;
	$.ajax({
		url : "removeListKind",
		type : "POST",
		async : false,
		data : {
			kindId : kindId
		},
		success : function(ret) {
			if (ret == "SUCCESS") {
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