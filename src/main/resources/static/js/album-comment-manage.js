var albumCommManage = function() {
	cleanEasyUIContainer();
	$("#title").text("专辑评论管理");
	$('#easy-ui-container').datagrid({
		url : "getAlbumComments",
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
			formatter : album_comOperationFormat
		} ] ]
	});
	paginationFormat("easy-ui-container");
}

var album_comOperationFormat = function(val, row, index) {
	return "<a href='javascript:removeComment(\"album\"," + row.comId
			+ ");'>删除</a>"
}