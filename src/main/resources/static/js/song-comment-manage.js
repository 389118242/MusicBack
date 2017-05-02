var songCommManage = function() {
	cleanEasyUIContainer();
	$("#title").text("歌曲评论管理");
	$('#easy-ui-container').datagrid({
		url : "getSongComments",
		fitColumns : true,
		singleSelect : true,
		pagination : true,
		pageNumber : 1,
		pageSize : 9,
		pageList : [ 9, 27, 81 ],
		columns : [ [ {
			field : 'comId',
			title : 'ID'
		}, {
			field : 'comContent',
			title : '评论内容',
			formatter : comContentFormat
		}, {
			field : 'user',
			title : '评论者',
			formatter : comUserFormat
		}, {
			field : 'parentId',
			title : '操作',
			formatter : song_comOperationFormat
		} ] ]
	});
	paginationFormat("easy-ui-container");
}

var comContentFormat = function(val, row, index) {
	return "<div style='width:300px;max-height:87px'>" + val + "<div>"
}
var comUserFormat = function(val, row, index) {
	return val.userName;
}
var song_comOperationFormat = function(val, row, index) {
	return "<a href='javascript:removeComment(\"song\"," + row.comId
			+ ");'>删除</a>"
}
var removeComment = function(type, commId) {
	var $form = $('<form class="form-horizontal" style="margin-left: 13%"><div class="form-group">'
			+ '<label class="col-sm-2 control-label">删除原因</label>'
			+ '<div class="col-sm-6">'
			+ '<textarea class="form-control" name="why"></textarea>'
			+ '</div>' + '</div></form>');
	$("#modal .modal-title").text('删除评论');
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
			url : "deleteComment",
			type : "POST",
			async : false,
			data : {
				type : type,
				comId : commId,
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