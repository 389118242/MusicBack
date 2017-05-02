var userManage = function() {
	cleanEasyUIContainer();
	$("#title").text("用户管理");
	$('#easy-ui-container').datagrid({
		url : "getUsers",
		fitColumns : true,
		singleSelect : true,
		pagination : true,
		pageNumber : 1,
		pageSize : 9,
		pageList : [ 9, 27, 81 ],
		columns : [ [ {
			field : 'userId',
			title : 'ID'
		}, {
			field : 'userAccount',
			title : '账号'
		}, {
			field : 'userName',
			title : '昵称'
		}, {
			field : 'userImg',
			title : '头像',
			formatter : imgFormat
		}, {
			field : 'userDetail',
			title : '简介',
			formatter : userDetailFormat
		}, {
			field : 'userEmail',
			title : '邮箱'
		}, {
			field : 'userState',
			title : '操作',
			formatter : stateFormat
		} ] ]
	});
	paginationFormat("easy-ui-container");
}

var imgFormat = function(value, row, index) {
	var ret = "默认头像";
	if (value.indexOf("data:image") === 0)
		ret = "<img style='width:100px;height:100px' src='" + value + "'>"
	return ret;
}
var userDetailFormat = function(val, row, index) {
	return '<textarea rows="3" cols="39" disabled="disabled" style="resize:none">'
			+ (val === null ? '' : val) + '</textarea>';
}
var stateFormat = function(value, row, index) {
	var ret = "<a class='operation' href='javascript:void(0);' onclick='changeState(this);' user-id='"
			+ row.userId
			+ "' user-state='"
			+ value
			+ "'>"
			+ (value ? "冻结账号" : "解封账号") + "</a>";
	ret += "<a class='operation' href='javascript:void(0);' onclick='showSendMess(this);' user-id='"
			+ row.userId + "'>发送消息</a>"
	return ret;
}

var changeState = function(element) {
	var $a = $(element);
	var id = $a.attr("user-id");
	var currentState = $a.attr("user-state");
	var state = currentState === '1' ? "0" : "1";
	$.ajax({
		url : "changeState",
		type : "POST",
		async : false,
		data : {
			userId : id,
			userState : state
		},
		dataType : "json",
		success : function(state) {
			if (state) {
				$(".pagination-load").click();
			} else {
				alert("更新状态失败");
			}
		},
		error : function(xhr, emess) {
			alert(xhr.status + " : " + emess);
		}
	});
}

var showSendMess = function(element) {
	var $a = $(element);
	var id = $a.attr("user-id");
	var $div = $('<form class="form-horizontal" style="margin-left: 13%"><div class="form-group">'
			+ '<label class="col-sm-2 control-label">消息内容</label>'
			+ '<div class="col-sm-6">'
			+ '<textarea class="form-control" id="mess"></textarea>'
			+ '</div>'
			+ '</div></form>');
	$("#modal .modal-title").text('发送消息');
	$("#modal .modal-body").append($div);
	$("#modal").modal("show");
	$("#modal #submit").click(function() {
		var mess = $div.find("#mess").val();
		$.ajax({
			url : "sendMessage",
			type : "POST",
			async : false,
			data : {
				userId : id,
				mess : mess
			},
			dataType : "json",
			success : function(state) {
				if (state) {
					alert("发送成功");
				} else {
					alert("发送失败");
				}
				$("#modal").modal("hide");
			},
			error : function(xhr, emess) {
				alert(xhr.status + " : " + emess);
			}
		});
	});
}