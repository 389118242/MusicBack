var singerManage = function() {
	cleanEasyUIContainer();
	$("#title").text("歌手管理");
	$('#easy-ui-container').datagrid({
		url : "getPagedSingers",
		toolbar : [ {
			iconCls : 'icon-add',
			handler : addSinger
		}, {
			iconCls : 'icon-remove',
			handler : removeSinger
		} , {
			iconCls : 'icon-edit',
			handler : editSinger
		} ],
		fitColumns : true,
		singleSelect : true,
		pagination : true,
		pageNumber : 1,
		pageSize : 9,
		pageList : [ 9, 27, 81 ],
		columns : [ [ {
			field : 'singerId',
			title : 'ID'
		}, {
			field : 'singerName',
			title : '歌手名'
		}, {
			field : 'singerDetail',
			title : '简介',
			formatter : detailFormat
		}, {
			field : 'resourceId',
			title : '歌手图片',
			formatter : resourceFormat
		} ] ]
	});
	paginationFormat("easy-ui-container");
}
var detailFormat = function(val, row, index) {
	return '<textarea rows="3" cols="39" disabled="disabled" style="resize:none">'
			+ (val === null ? '' : val) + '</textarea>';
}
var resourceFormat = function(val, row, index) {
	return null == val ? "" : "<img style='width:100px;height:100px' src='img/"
			+ val + "'>"
}
var addSinger = function() {
	var $form = $('<form class="form-horizontal" style="margin-left: 13%">'+
			'<div class="form-group">'+
			'<label class="col-sm-2 control-label">歌手名</label>'+
			'<div class="col-sm-6">'+
				'<input type="text" class="form-control" name="name">'+
			'</div>'+
		'</div>'+
		'<div class="form-group">'+
			'<label class="col-sm-2 control-label">简介</label>'+
			'<div class="col-sm-6">'+
				'<textarea class="form-control" name="detail"></textarea>'+
			'</div>'+
		'</div>'+
		'<div class="form-group">'+
			'<label class="col-sm-2 control-label">歌手图片</label>'+
			'<div class="col-sm-6">'+
				'<input type="file" name="img">'+
				'<br>*** 300×300 ***'+
			'</div>'+
		'</div>'+
	'</form>');
	$("#modal .modal-title").text('添加歌手');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(function() {
		var form = $form[0];
		var name = form.name.value;
		var detail = form.detail.value;
		var img = form.img.files[0];
		if (name.trim() === '') {
			alert_add("#modal .modal-body", "请填写歌手名称");
			return;
		}
		if (detail.trim() === '') {
			alert_add("#modal .modal-body", "请填写歌手详情");
			return;
		}
		if (!img || img.type.indexOf("image/") === -1) {
			alert_add("#modal .modal-body", "请添加歌手相片");
			return;
		}
		var formData = new FormData();
		formData.append("name", name);
		formData.append("detail", detail);
		formData.append("file", img);
		$.ajax({
			url : "addSinger",
			type : "POST",
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			data : formData,
			success : function(ret) {
				if (ret === "SUCCESS") {
					dataReload();
					$("#modal").modal("hide");
				} else if (ret === "FAILED") {
					alert_add("#modal .modal-body", "添加歌手失败");
				} else {
					alert_add("#modal .modal-body", ret);
				}
			},
			error : function(xhr, emess) {
				alert_add("#modal .modal-body", "ERROR :　" + emess);
			}
		});
	});
}
var removeSinger = function() {
	var row = $('#easy-ui-container').datagrid("getSelected");
	if (row == null) {
		alert("未选中要删除的歌手");
		return;
	}
	var singerId = row.singerId;
	$.ajax({
		url : "removeSinger",
		type : "POST",
		async : false,
		data : {
			singerId : singerId
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
var editSinger = function(){
	var singer = $('#easy-ui-container').datagrid("getSelected");
	if (singer == null) {
		alert("未选中要修改的歌手");
		return;
	}
	var singerId = singer.singerId;
	var $form = $('<form class="form-horizontal" style="margin-left: 13%">'+
			'<div class="form-group">'+
			'<label class="col-sm-2 control-label">歌手名</label>'+
			'<div class="col-sm-6">'+
				'<input type="text" class="form-control" name="name">'+
			'</div>'+
		'</div>'+
		'<div class="form-group">'+
			'<label class="col-sm-2 control-label">简介</label>'+
			'<div class="col-sm-6">'+
				'<textarea class="form-control" name="detail"></textarea>'+
			'</div>'+
		'</div>'+
		'<div class="form-group">'+
			'<label class="col-sm-2 control-label">歌手图片</label>'+
			'<div class="col-sm-6">'+
				'<input type="file" name="img">'+
				'<br>*** 300×300 ***'+
			'</div>'+
		'</div>'+
	'</form>');
	var form = $form[0];
	form.name.value = singer.singerName;
	form.detail.value = singer.singerDetail;
	$("#modal .modal-title").text('修改歌手信息');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(function() {
		form = $form[0];
		var name = form.name.value;
		if (name.trim() === '') {
			alert_add("#modal .modal-body", "请填写歌手名称");
			return;
		}
		var detail = form.detail.value;
		var img = form.img.files[0];
		if (detail.trim() === '') {
			alert_add("#modal .modal-body", "请填写歌手详情");
			return;
		}
		var formData = new FormData();
		formData.append("name", name);
		formData.append("id", singerId);
		formData.append("detail", detail);
		if (img)
			formData.append("file", img);
		$.ajax({
			url : "editSinger",
			type : "POST",
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			data : formData,
			success : function(ret) {
				if (ret === "SUCCESS") {
					dataReload();
					$("#modal").modal("hide");
				} else if (ret === "FAILED") {
					alert_add("#modal .modal-body", "修改歌手信息失败");
				} else {
					alert_add("#modal .modal-body", ret);
				}
			},
			error : function(xhr, emess) {
				alert_add("#modal .modal-body", "ERROR :　" + emess);
			}
		});
	});
}