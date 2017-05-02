var albumManage = function() {
	cleanEasyUIContainer();
	$("#title").text("专辑管理");
	$('#easy-ui-container').datagrid({
		url : "getAllAlbums",
		toolbar : [ {
			iconCls : 'icon-add',
			handler : addAlbum
		}, {
			iconCls : 'icon-remove',
			handler : removeAlbum
		}, {
			iconCls : 'icon-edit',
			handler : editAlbum
		} ],
		fitColumns : true,
		singleSelect : true,
		pagination : true,
		pageNumber : 1,
		pageSize : 9,
		pageList : [ 9, 27, 81 ],
		columns : [ [ {
			field : 'albumId',
			title : 'ID'
		}, {
			field : 'albumName',
			title : '名称'
		}, {
			field : 'resourceId',
			title : '专辑图片',
			formatter : resourceFormat
		}, {
			field : 'singer',
			title : '歌手',
			formatter : singerFormat
		}, {
			field : 'company',
			title : '发行公司',
			formatter : function(val) {
				return val.companyName;
			}
		}, {
			field : 'issueTime',
			title : '发行时间',
			formatter : dateFormat
		}, {
			field : 'albumDetail',
			title : '简介',
			formatter : detailFormat
		} ] ]
	});
	paginationFormat("easy-ui-container");
}
var addAlbum = function() {
	var singers;
	$.ajax({
		url : "getSingers",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(ret) {
			singers = ret;
		}
	});
	if (!singers) {
		alert("获取歌手列表失败");
		return;
	}
	var $singer_select = $('<select name="singer" class="form-control"><option value="0">请选择歌手</option></select>');
	for ( var i in singers) {
		var singer = singers[i];
		var option = '<option value="' + singer.singerId + '">'
				+ singer.singerName + '</option>';
		$singer_select.append(option);
	}
	var companys;
	$.ajax({
		url : "getAllCompanys",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(ret) {
			companys = ret;
		}
	});
	if (!companys) {
		alert("获取发行公司列表失败");
		return;
	}
	var $company_select = $('<select name="company" class="form-control"><option value="0">请选择发行公司</option></select>');
	for ( var i in companys) {
		var company = companys[i];
		var option = '<option value="' + company.rcId + '">'
				+ company.companyName + '</option>';
		$company_select.append(option);
	}
	var $form = $('<form class="form-horizontal" style="margin-left: 13%">'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">专辑名称</label>'
			+ '<div class="col-sm-6">'
			+ '<input type="text" class="form-control" name="name">' + '</div>'
			+ '</div>' + '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">歌手</label>'
			+ '<div class="col-sm-6" id="singer-c">' + '</div>' + '</div>'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">发行公司</label>'
			+ '<div class="col-sm-6" id="company-c">' + '</div>' + '</div>'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">专辑封面</label>'
			+ '<div class="col-sm-6">' + '<input type="file" name="img">'
			+ '<br>*** 300×300 ***' + '</div>' + '</div>'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">发行日期</label>'
			+ '<div class="col-sm-6">' + '<input type="text" name="time">'
			+ '<br>*** e.g. 1999-09-09 ***' + '</div>' + '</div>'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">简介</label>'
			+ '<div class="col-sm-6">'
			+ '<textarea class="form-control" name="detail"></textarea>'
			+ '</div>' + '</div>' + '</form>');
	$form.find("#singer-c").append($singer_select);
	$form.find("#company-c").append($company_select);
	$("#modal .modal-title").text('添加专辑');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(function() {
		var form = $form[0];
		var name = form.name.value;
		if (name.trim() === '') {
			alert_add("#modal .modal-body", "请填写专辑名称");
			return;
		}
		var singerId = form.singer.value;
		if (singerId === '0') {
			alert_add("#modal .modal-body", "请选择歌手");
			return;
		}
		var companyId = form.company.value;
		if (companyId === '0') {
			alert_add("#modal .modal-body", "请选择发行公司");
			return;
		}
		var time = form.time.value;
		if (time.trim() === '') {
			alert_add("#modal .modal-body", "请填写发行时间");
			return;
		}
		var date = new Date(time);
		if (isNaN(date) || date.getTime() > new Date().getTime()) {
			alert_add("#modal .modal-body", "请填写正确的发行时间");
			return;
		}
		var detail = form.detail.value;
		if (detail.trim() === '') {
			alert_add("#modal .modal-body", "请填写专辑详情");
			return;
		}
		var img = form.img.files[0];
		if (!img || img.type.indexOf("image/") === -1) {
			alert_add("#modal .modal-body", "请添加专辑封面");
			return;
		}
		var formData = new FormData();
		formData.append("albumName", name);
		formData.append("albumDetail", detail);
		formData.append("singer.singerId", singerId);
		formData.append("company.rcId", companyId);
		formData.append("issueTime", date);
		formData.append("file", img);
		$.ajax({
			url : "addAlbum",
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
					alert_add("#modal .modal-body", "添加专辑失败");
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
var removeAlbum = function() {
	var album = $('#easy-ui-container').datagrid("getSelected");
	if (album == null) {
		alert("未选中要删除的专辑");
		return;
	}
	var albumId = album.albumId;
	$.ajax({
		url : "removeAlbum",
		type : "POST",
		async : false,
		data : {
			albumId : albumId
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
var editAlbum = function() {
	var album = $('#easy-ui-container').datagrid("getSelected");
	if (album == null) {
		alert("未选中要修改的专辑");
		return;
	}
	var albumId = album.albumId;
	var singerId = album.singer.singerId;
	var companyId = album.company.rcId;
	var singers;
	$.ajax({
		url : "getSingers",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(ret) {
			singers = ret;
		}
	});
	if (!singers) {
		alert("获取歌手列表失败");
		return;
	}
	var $singer_select = $('<select name="singer" class="form-control"><option value="0">请选择歌手</option></select>');
	for ( var i in singers) {
		var singer = singers[i];
		var option = '<option value="'
				+ singer.singerId
				+ '" '
				+ (singer.singerId !== singerId ? "" : ' selected = "selected"')
				+ '>' + singer.singerName + '</option>';
		$singer_select.append(option);
	}
	var companys;
	$.ajax({
		url : "getAllCompanys",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(ret) {
			companys = ret;
		}
	});
	if (!companys) {
		alert("获取发行公司列表失败");
		return;
	}
	var $company_select = $('<select name="company" class="form-control"><option value="0">请选择发行公司</option></select>');
	for ( var i in companys) {
		var company = companys[i];
		var option = '<option value="' + company.rcId + '"'
				+ (company.rcId !== companyId ? "" : ' selected = "selected"')
				+ '>' + company.companyName + '</option>';
		$company_select.append(option);
	}
	var $form = $('<form class="form-horizontal" style="margin-left: 13%">'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">专辑名称</label>'
			+ '<div class="col-sm-6">'
			+ '<input type="text" class="form-control" name="name">' + '</div>'
			+ '</div>' + '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">歌手</label>'
			+ '<div class="col-sm-6" id="singer-c">' + '</div>' + '</div>'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">发行公司</label>'
			+ '<div class="col-sm-6" id="company-c">' + '</div>' + '</div>'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">专辑封面</label>'
			+ '<div class="col-sm-6">' + '<input type="file" name="img">'
			+ '<br>*** 300×300 ***' + '</div>' + '</div>'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">发行日期</label>'
			+ '<div class="col-sm-6">' + '<input type="text" name="time">'
			+ '<br>*** e.g. 1999-09-09 ***' + '</div>' + '</div>'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">简介</label>'
			+ '<div class="col-sm-6">'
			+ '<textarea class="form-control" name="detail"></textarea>'
			+ '</div>' + '</div>' + '</form>');
	$form.find("#singer-c").append($singer_select);
	$form.find("#company-c").append($company_select);
	var form = $form[0];
	form.name.value = album.albumName;
	form.time.value = new Date(album.issueTime);
	form.detail.value = album.albumDetail;
	$("#modal .modal-title").text('修改专辑');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(function() {
		var form = $form[0];
		var name = form.name.value;
		if (name.trim() === '') {
			alert_add("#modal .modal-body", "请填写专辑名称");
			return;
		}
		var singerId = form.singer.value;
		if (singerId === '0') {
			alert_add("#modal .modal-body", "请选择歌手");
			return;
		}
		var companyId = form.company.value;
		if (companyId === '0') {
			alert_add("#modal .modal-body", "请选择发行公司");
			return;
		}
		var time = form.time.value;
		if (time.trim() === '') {
			alert_add("#modal .modal-body", "请填写发行时间");
			return;
		}
		var date = new Date(time);
		if (isNaN(date) || date.getTime() > new Date().getTime()) {
			alert_add("#modal .modal-body", "请填写正确的发行时间");
			return;
		}
		var detail = form.detail.value;
		if (detail.trim() === '') {
			alert_add("#modal .modal-body", "请填写专辑详情");
			return;
		}
		var img = form.img.files[0];
		var formData = new FormData();
		formData.append("albumName", name);
		formData.append("albumDetail", detail);
		formData.append("singer.singerId", singerId);
		formData.append("company.rcId", companyId);
		formData.append("issueTime", date);
		formData.append("albumId", albumId);
		if (album.resourceId)
			formData.append("resourceId", album.resourceId);
		if (img)
			formData.append("file", img);
		$.ajax({
			url : "editAlbum",
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
					alert_add("#modal .modal-body", "修改专辑失败");
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