/**
 * Since a transition to height: auto is not possible, we need to determine the menu height on runtime
 * the moment the menu is displayed, i.e. either when the page is loaded or when the display size is
 * scaled down below the threshold at which the top navigation is displayed as burger menu.
 * 
 * We also need to set or remove a CSS class to show or hide the menu when clicking the burger menu icon.
 */
let menuHeightStyle;
let treeHeightStyle;

var calculateMenuHeight = function() {
	let cssRules = '@media (max-width: 768px) {';
	
	$('.jeesl-dropdown-list').each((index, element) => cssRules += '#' + $(element).attr('id') + '.jeesl-active { height: ' + ($(element).children('.jeesl-dropdown-item').toArray().map(child => $(child).outerHeight()).reduce((previous, current) => previous + current, 0) + 30) + 'px; }');
	
	menuHeightStyle.append(cssRules + '}');
}

//function calculateTreeHeight() {
//	$('.ui-tree-container > .ui-treenode').each((index, element) => setTreeHeight($(element)));
//}

function toggleTreeItem() {
//	let node = $(this).parent().parent();
//	node.toggleClass('jeesl-active');
//	
//	setTreeHeight(findTreeRoot(node));
	
	$(this).parent().next('.ui-treenode-children').toggleClass('jeesl-active');
}

function findTreeRoot(treeNode) {
	return treeNode.parent().hasClass('ui-treenode-children') ? findTreeRoot(treeNode.parent().parent()) : treeNode;
}

function setTreeHeight(node) {
	let height = 0;
	let childList = node.children('.ui-treenode-children');
	
	if (node.hasClass('jeesl-active')) {
		height += childList.first()
						   .children('.ui-treenode')
						   .map((index, item) => setTreeHeight($(item)))
						   .toArray()
						   .reduce((accumulator, currentValue) => accumulator + currentValue, 0)
				+ childList.first().children('.ui-tree-droppoint:last-child').length * 4;
	}
	
	childList.height(height);
	return height + 50;
}

function toggleMenu() {
	$('.jeesl-menu-bar-dropdown').filter((index, button) => button !== this).removeClass('jeesl-active').siblings('.jeesl-dropdown-list').removeClass('jeesl-active');
	
	$(this).filter(current => $(current).find('.jeesl-greyscale')).toggleClass('jeesl-active').siblings('.jeesl-dropdown-list').toggleClass('jeesl-active');
}

function reloadStatusBar() {
	let newButtons = $('.jeesl-status-bar .jeesl-menu-bar-dropdown');
	let newDropdowns = $('.jeesl-status-bar .jeesl-dropdown-list').attr('id', (index, oldValue) => 'jeesl-dropdown-' + ($('.jeesl-dropdown-list').length + index));
	
	calculateMenuHeight(newDropdowns);
	newButtons.click(toggleMenu);
}

$(function() {
	menuHeightStyle = $('<style>').prop('type', 'text/css').appendTo('head');
	treeHeightStyle = $('<style>').prop('type', 'text/css').appendTo('head');
	
	let menuButtons = $('.jeesl-menu-bar-dropdown');
	let dropdowns = $('.jeesl-dropdown-list').attr('id', (index, oldValue) => 'jeesl-dropdown-' + index);
	
	calculateMenuHeight(dropdowns);
	menuButtons.click(toggleMenu);
	
	$('.ui-tree-toggler').click(toggleTreeItem);
});