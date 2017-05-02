var recommendSongManage = function() {
	cleanEasyUIContainer();
	$("#title").text("歌曲管理");
	$('#easy-ui-container').datagrid({
		url : "getRecommendSongs",
		toolbar : [ {
			iconCls : 'icon-add',
			handler : addRecommendSong
		} ],
		fitColumns : true,
		singleSelect : true,
		columns : [ [ {
			field : 'songId',
			title : 'ID'
		}, {
			field : 'songName',
			title : '歌名'
		}, {
			field : 'album',
			title : '专辑',
			formatter : albumFormat
		}, {
			field : 'singer',
			title : '专辑',
			formatter : singerFormat
		}, {
			field : 'resourceId',
			title : '操作',
			formatter : recommend_song_operationFormat
		} ] ]
	});
}

var addRecommendSong = function() {
	var songs;
	$.ajax({
		url : "getAllSongs",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(ret) {
			songs = ret;
		}
	});
	if (!songs) {
		alert("获取歌手列表失败");
		return;
	}
	var $song_select = $('<select name="r_song" class="form-control"><option value="0">请选择歌曲</option></select>');
	for ( var i in songs) {
		var song = songs[i];
		var option = '<option value="' + song.songId + '">' + song.songName
				+ '</option>';
		$song_select.append(option);
	}
	var $form = $('<form class="form-horizontal" style="margin-left: 13%">'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">请选择歌曲</label>'
			+ '<div class="col-sm-6" id="r_s">' + '</div>' + '</div>'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">权重</label>'
			+ '<div class="col-sm-6">'
			+ '<input type="text" class="form-control" name="sort">' + '</div>'
			+ '</div>' + +'</form>');
	$form.find("#r_s").append($song_select);
	$("#modal .modal-title").text('添加推荐歌曲');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(function() {
		var form = $form[0];
		var songId = form.r_song.value;
		var sort = form.sort.value;
		sort = parseInt(sort);
		if (songId === '0') {
			alert_add("#modal .modal-body", "请选择歌曲");
			return;
		}
		if (isNaN(sort) || sort < 0) {
			alert_add("#modal .modal-body", "权重为数字且大于等于0");
			return;
		}
		$.ajax({
			url : "addRecommendSong",
			type : "POST",
			async : false,
			data : {
				songId : songId,
				sort : sort
			},
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
var recommend_song_operationFormat = function(val, row, index) {
	return "<a href='javascript:removeRecommendSong(" + row.songId
			+ ");'>删除</a>"
}
var removeRecommendSong = function(songId) {
	$.ajax({
		url : "removeRecommendSong",
		type : "POST",
		async : false,
		data : {
			songId : songId
		},
		success : function(ret) {
			if (ret === "SUCCESS") {
				alert("删除成功");
				dataReload();
			} else {
				alert("删除失败");
			}
		},
		error : function(xhr, emess) {
			alert("ERROR :　" + emess);
		}
	})
}