var songManage = function() {
	cleanEasyUIContainer();
	$("#title").text("歌曲管理");
	$('#easy-ui-container').datagrid({
		url : "getSongs",
		toolbar : [ {
			iconCls : 'icon-add',
			handler : addSong
		} ],
		fitColumns : true,
		singleSelect : true,
		pagination : true,
		pageNumber : 1,
		pageSize : 9,
		pageList : [ 9, 27, 81 ],
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
			field : 'lyric',
			title : '歌词',
			formatter : lyricFormat
		}, {
			field : 'songTime',
			title : '时间'
		}, {
			field : 'playNum',
			title : '播放次数'
		}, {
			field : 'resourceId',
			title : '操作',
			formatter : song_operationFormat
		} ] ]
	});
	paginationFormat("easy-ui-container");
}
var albumFormat = function(val, row, index) {
	return val.albumName;
}
var singerFormat = function(val, row, index) {
	return val.singerName;
}
var lyricFormat = function(val, row, index) {
	return '<textarea rows="3" cols="39" disabled="disabled" style="resize:none">'
			+ (val === null ? '' : val) + '</textarea>';
}
var song_operationFormat = function(val, row, index) {
	var resourceId = val;
	var songId = row.songId;
	return "<a class='operation' href='javascript:void(0);' onclick='updaetSong("
			+ resourceId
			+ ","
			+ songId
			+ ")'>更改歌曲文件</a>"
			+ "<a class='operation' href='javascript:void(0);' onclick='updaetSongLyric("
			+ songId
			+ ")'>更改歌词</a>"
			+ "<a class='operation' href='javascript:void(0);' onclick='removeSong("
			+ songId + ")'>删除</a>";
}
var updaetSong = function(resourceId, songId) {
	var $form = $('<form class="form-horizontal" style="margin-left: 13%"><div class="form-group">'
			+ '<label class="col-sm-2 control-label">音乐文件</label>'
			+ '<div class="col-sm-6">'
			+ '<input type="file" name="file" accept="audio/mpeg">'
			+ '</div>'
			+ '</div></form>');
	$("#modal .modal-title").text('更改歌曲文件');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(function() {
		var file = $form.find("input[name=file]")[0].files[0];
		if (!file) {
			alert_add("#modal .modal-body", "请添加音乐文件");
			return;
		}
		if (file.type.indexOf("audio") !== 0) {
			alert_add("#modal .modal-body", "请添加符合要求的音乐文件");
			return;
		}
		var formData = new FormData();
		formData.append("songId", songId);
		if (resourceId)
			formData.append("resourceId", resourceId);
		formData.append("file", file);
		$.ajax({
			url : 'updateSongFile',
			type : 'POST',
			data : formData,
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(ret) {
				if ('SUCCESS' === ret) {
					dataReload();
					$("#modal").modal("hide");
				} else
					alert_add("#modal .modal-body", ret);
			},
			error : function(xhr, emess) {
				alert_add("#modal .modal-body", "文件大小不可以超过20M");
			}
		});
	});
}
var updaetSongLyric = function(songId) {
	var $form = $('<form class="form-horizontal" style="margin-left: 13%"><div class="form-group">'
			+ '<label class="col-sm-2 control-label">歌词文件</label>'
			+ '<div class="col-sm-6">'
			+ '<input type="file" name="file">'
			+ '</div>' + '</div></form>');
	$("#modal .modal-title").text('更改歌词');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(function() {
		var file = $form.find("input[name=file]")[0].files[0];
		if (!file) {
			alert_add("#modal .modal-body", "请添加音乐文件");
			return;
		}
		var extension = file.name.substring(file.name.lastIndexOf("\.") + 1);
		if (extension.toLowerCase() !== "lrc") {
			alert_add("#modal .modal-body", "仅支持LRC格式歌词");
			return;
		}
		var formData = new FormData();
		formData.append("songId", songId);
		formData.append("file", file);
		$.ajax({
			url : 'updateLyric',
			type : 'POST',
			data : formData,
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(ret) {
				if ('SUCCESS' === ret) {
					dataReload();
					$("#modal").modal("hide");
				} else
					alert_add("#modal .modal-body", ret);
			},
			error : function(xhr, emess) {
				alert_add("#modal .modal-body", "文件大小不可以超过20M");
			}
		});
	});
}
var removeSong = function(songId) {
	$.ajax({
		url : 'deleteSong',
		type : 'POST',
		data : {
			songId : songId
		},
		async : false,
		success : function(ret) {
			if ('SUCCESS' === ret) {
				alert("删除成功");
				dataReload();
			} else
				alert("删除失败");
		},
		error : function(xhr, emess) {
			alert("请求异常");
		}
	});
}
var addSong = function() {
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
	var $singer_select = $('<select name="singer" class="form-control" onchange="initAlbumSelector(this)"><option value="0">请选择歌手</option></select>');
	for ( var i in singers) {
		var singer = singers[i];
		var option = '<option value="' + singer.singerId + '">'
				+ singer.singerName + '</option>';
		$singer_select.append(option);
	}
	var $form = $('<form class="form-horizontal" style="margin-left: 13%">'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">歌名</label>'
			+ '<div class="col-sm-6">'
			+ '<input type="text" class="form-control" name="songName">'
			+ '</div>' + '</div>' + '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">歌手</label>'
			+ '<div class="col-sm-6" id="singer">' + '</div>' + '</div>'
			+ '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">歌词文件</label>'
			+ '<div class="col-sm-6">' + '<input type="file" name="lyric">'
			+ '</div>' + '</div>' + '<div class="form-group">'
			+ '<label class="col-sm-2 control-label">歌曲文件</label>'
			+ '<div class="col-sm-6">'
			+ '<input type="file" name="song" accept="audio/mpeg">' + '</div>'
			+ '</div>' + '</form>');
	$form.find("#singer").append($singer_select);
	$("#modal .modal-title").text('更改歌词');
	$("#modal .modal-body").append($form);
	$("#modal").modal("show");
	$("#modal #submit").click(
			function() {
				var form = $form[0];
				var songName = form.songName.value;
				if (songName === '') {
					alert_add("#modal .modal-body", "请填写歌名");
					return;
				}
				var singerId = form.singer.value;
				if (singerId === '0') {
					alert_add("#modal .modal-body", "请选择歌手");
					return;
				}
				var albumId = form.album && form.album.value;
				if (albumId === '0') {
					alert_add("#modal .modal-body", "请选择歌手");
					return;
				}
				var lyric = form.lyric.files[0];
				if (lyric) {
					var extension = lyric.name.substring(lyric.name
							.lastIndexOf("\.") + 1);
					if (extension.toLowerCase() !== "lrc") {
						alert_add("#modal .modal-body", "仅支持LRC格式歌词");
						return;
					}
				}
				var song = form.song.files[0];
				if (!song) {
					alert_add("#modal .modal-body", "请选择歌曲文件");
					return;
				}
				var formData = new FormData();
				formData.append("songName", songName);
				formData.append("singer.singerId", singerId);
				formData.append("album.albumId", albumId);
				if (lyric)
					formData.append("lyricFile", lyric);
				formData.append("songFile", song);
				$.ajax({
					url : "addSong",
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
							alert_add("#modal .modal-body", "添加歌曲失败");
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
var initAlbumSelector = function(dom_selector) {
	var $selector = $(dom_selector);
	var singerId = dom_selector.value;
	var $album_select = $selector.parents("form").find("select[name=album]");
	if ('0' === singerId) {
		$album_select.parents(".form-group").remove();
		return;
	}
	var albums;
	$.ajax({
		url : "getAlbumBySingerId",
		type : "POST",
		async : false,
		data : {
			singerId : singerId
		},
		dataType : "json",
		success : function(ret) {
			albums = ret;
		}
	});
	if (!albums) {
		alert("获取歌手列表失败");
		return;
	}
	var options = '';
	for ( var i in albums) {
		var album = albums[i];
		options += ('<option value="' + album.albumId + '">' + album.albumName + '</option>');
	}
	if ($album_select.length !== 0) {
		$album_select.find("option[value='0']").siblings().remove();
		$album_select.append(options);
	} else {
		var $singer_selector_div = $selector.parents(".form-group");
		var $div = $('<div class="form-group">'
				+ '<label class="col-sm-2 control-label">专辑</label>'
				+ '<div class="col-sm-6">' + '</div>' + '</div>');

		var $album_select = $('<select name="album" class="form-control"><option value="0">请选择专辑</option>'
				+ options + '</select>');
		$div.find("div").append($album_select);
		$singer_selector_div.after($div);
	}
}