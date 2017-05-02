var songListCommManage = function() {
	cleanEasyUIContainer();
	$("#title").text("歌曲评论管理");
	$('#easy-ui-container').datagrid({
		url : "getSongListComments",
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
			formatter : songlist_comOperationFormat
		} ] ]
	});
	paginationFormat("easy-ui-container");
}

var songlist_comOperationFormat = function(val, row, index) {
	return "<a href='javascript:removeComment(\"songList\"," + row.comId
			+ ");'>删除</a>"
}