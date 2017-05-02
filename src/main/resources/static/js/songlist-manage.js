var songListManage = function() {
	cleanEasyUIContainer();
	$("#title").text("歌单管理");
	$('#easy-ui-container').datagrid({
		url : "getSongLists",
		fitColumns : true,
		singleSelect : true,
		pagination : true,
		pageNumber : 1,
		pageSize : 9,
		pageList : [ 9, 27, 81 ],
		columns : [ [ {
			field : 'listId',
			title : 'ID'
		}, {
			field : 'listName',
			title : '歌单名'
		}, {
			field : 'listImg',
			title : '图片',
			formatter : listImgFormat
		}, {
			field : 'listDetail',
			title : '歌单性情',
			formatter : listDetailFormat
		}, {
			field : 'createTime',
			title : '创建时间',
			formatter : dateFormat
		}, {
			field : 'user',
			title : '创建者',
			formatter : songListUserFormat
		}, {
			field : 'playNum',
			title : '操作',
			formatter : songlist_operationFormat
		} ] ]
	});
	paginationFormat("easy-ui-container");
}
var dateFormat = function(val, row, index) {
	return new Date(val);
}
var listImgFormat = function(val, row, index) {
	return "<img style='width:100px;height:100px' src='" + val + "'>";
}
var listDetailFormat = function(val, row, index) {
	return '<textarea rows="3" cols="39" disabled="disabled" style="resize:none">'
			+ (val === null ? '' : val) + '</textarea>';
}
var songListUserFormat = function(val, row, index) {
	return val.userName;
}
var songlist_operationFormat = function(val, row, index) {
	return "<a href='javascript:removeSongList(" + row.listId + ");'>删除</a>";
}
var removeSongList = function(listId) {
	var $form = $('<form class="form-horizontal" style="margin-left: 13%"><div class="form-group">'
			+ '<label class="col-sm-2 control-label">删除原因</label>'
			+ '<div class="col-sm-6">'
			+ '<textarea class="form-control" name="why"></textarea>'
			+ '</div>' + '</div></form>');
	$("#modal .modal-title").text('删除歌单');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(function() {
		var form = $form[0];
		var why = form.why.value;
		if (why.trim() === '') {
			alert_add("#modal .modal-body", "请说明删除理由");
			return;
		}
		$.ajax({
			url : "deleteSongList",
			type : "POST",
			async : false,
			data : {
				listId : listId,
				why : why
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
