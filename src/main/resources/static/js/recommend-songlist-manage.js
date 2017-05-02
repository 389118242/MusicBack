var recommendSongListManage = function() {
	cleanEasyUIContainer();
	$("#title").text("推荐歌单");
	$('#easy-ui-container').datagrid({
		url : "getRecommendSongList",
		fitColumns : true,
		singleSelect : true,
		columns : [ [ {
			field : 'index',
			title : 'ID'
		}, {
			field : 'slId',
			title : '歌单名称',
			formatter : rscNameFormat
		}, {
			field : 'songList',
			title : '歌单所属',
			formatter : rscCreaterFormat
		}, {
			field : 'img',
			title : '图片',
			formatter : rscImgFormat
		}, {
			field : 'detail',
			title : '操作',
			formatter : rsc_operationFormat
		} ] ]
	});
}

var rscNameFormat = function(val, row, index) {
	return row.songList && row.songList.listName;
}

var rscCreaterFormat = function(val, row, index) {
	return val && val.user.userName;
}

var rscImgFormat = function(val, row, index) {
	return val && "<img style='width:100px;height:100px' src='" + val + "'>";
}

var rsc_operationFormat = function(val, row, index) {
	return "<a href='javascript:alterRecommendSongList(\"" + val + "\"," + row.slId + ","
			+ row.index + ");'>修改</a>"
}

var detail_map = {
	"L" : "400×400",
	"M" : "200×400",
	"S" : "256*256"
}

var alterRecommendSongList = function(detail, slId, index) {
	var songLists;
	$.ajax({
		url : "getAllSongList",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(ret) {
			songLists = ret;
		}
	});
	if (!songLists) {
		alert("获取歌单列表失败");
		return;
	}
	var $songList_select = $('<select name="r_s_l" class="form-control"><option value="0">请选择歌单</option></select>');
	for ( var i in songLists) {
		var songList = songLists[i];
		var option = '<option value="' + songList.listId + '" '
				+ (songList.listId === slId ? ' selected = "selected"' : '')
				+ '>' + songList.listName + '---' + songList.user.userName
				+ '</option>';
		$songList_select.append(option);
	}
	var $form = $('<form class="form-horizontal" style="margin-left: 13%">'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">请选择歌单</label>'
			+ '<div class="col-sm-6" id="r_s">' + '</div>' + '</div>'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">图片</label>'
			+ '<div class="col-sm-6">'
			+ '<input type="file" class="form-control" name="img">'
			+ '<br> *** ' + detail_map[detail] + ' ***' + '</div>' + '</div>'
			+ +'</form>');
	$form.find("#r_s").append($songList_select);
	$("#modal .modal-title").text('修改推荐歌单');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(function() {
		var form = $form[0];
		var listId = form.r_s_l.value;
		var img = form.img.files[0];
		if (listId === '0') {
			alert_add("#modal .modal-body", "请选择歌单");
			return;
		}
		if (!img) {
			alert_add("#modal .modal-body", "请选择图片");
			return;
		}
		var formData = new FormData();
		formData.append("index", index);
		formData.append("listId", listId);
		formData.append("img", img);
		formData.append("detail", detail);
		$.ajax({
			url : $("#front-url").val() + "alterRecommendSongList",
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