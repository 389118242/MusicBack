var paginationFormat = function(container) {
	var pager = $('#' + container).datagrid('getPager');
	$(pager).pagination({
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
	});
}

var alert_add = function(selector, mess) {
	var $alert = $("<div class='alert alert-warning alert-dismissable'>" + mess
			+ "</div>");
	$(selector).append($alert);
	setTimeout(function() {
		$alert.remove();
	}, 3000);
}

var cleanEasyUIContainer = function() {
	try {
		$('#easy-ui-container').datagrid('getPanel').parent().remove();
		$('#title').after("<div id='easy-ui-container'></div>");
	} catch (e) {

	}
}

var dataReload = function() {
	$('#easy-ui-container').datagrid("reload");
}