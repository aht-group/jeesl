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
	
	$('.jeesl-dropdown-list').each((index, element) =>
		cssRules += '.jeesl-dropdown-list[dropdown-id=' +
					$(element).attr('dropdown-id') +
					'].jeesl-active { height: ' +
					($(element).children('.jeesl-dropdown-item')
							   .toArray()
							   .map(child => $(child).outerHeight())
							   .reduce((previous, current) => previous + current, 0) + 15 + (!$(element).hasClass('jeesl-dropdown-list-multi') * 15)) +
					'px !important; }');
	
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
	
	$(this).filter(current => $(current).find('.jeesl-greyscale'))
		   .toggleClass('jeesl-active')
		   .siblings('.jeesl-dropdown-list')
		   .removeAttr('style')
		   .toggleClass('jeesl-active')
		   .find('.jeesl-open')
		   .delay(200)
		   .queue(function() { $(this).removeClass('jeesl-open').removeAttr('style').dequeue(); });
}

function reloadStatusBar() {
	reloadContent($('.jeesl-status-bar'));
}

function jsfToJQuery(jsfSelector) {
	return $(jsfSelector.replace(' ', ',').replace(/^\:+/, '#').replaceAll(/,\:+/g, ',#').replaceAll(':', '\\:'));
}

function reloadContent(context) {
	let newButtons = context.find('.jeesl-menu-bar-dropdown');
	let newDropdowns = context.find('.jeesl-dropdown-list').attr('dropdown-id', (index, oldValue) => 'jeesl-dropdown-' + ($('.jeesl-dropdown-list').length + index));
	
	calculateMenuHeight(newDropdowns);
	newButtons.click(toggleMenu);
}

function toggleSubmenu(eventArgs) {
	let currentSubmenu = $(this).parent();
	let currentListItem = currentSubmenu.parent();
	let closeRequested = currentListItem.children('.jeesl-submenu').eq(0).height() > listItemHeight;
	let overlay = currentListItem.parent();
	
	overlay.height('auto');
	
	let submenus = overlay.find('.jeesl-submenu');
	
	if (!closeRequested) { currentSubmenu.addClass('jeesl-open'); }
	submenus.each(function() {
		let $this = $(this);
		$this.stop().animate({ height: $this.parent().is(currentListItem) && !closeRequested ? $(this).children('.jeesl-dropdown-sub').eq(0).outerHeight() + listItemHeight : listItemHeight }, 400, function() {
			if ($this.height() === listItemHeight) { $this.removeAttr('style'); }
		});
	});
	$.when(submenus).then(() => {
		submenus.each(function() { $(this).toggleClass('jeesl-open', $(this).is(currentSubmenu) && !closeRequested); });
		overlay.height(overlay.children().map((index, child) => $(child).outerHeight()).toArray().reduce((previous, current) => previous + current, 0) + 30);
		if (overlay.find('.jeesl-open').length === 0) { overlay.removeAttr('style'); }
	});
}

function toggleDatatable(eventArgs) {
	let panel = $(this).parent();
	
	if (panel.hasClass('jeesl-active')) {
		panel.removeClass('jeesl-active');
		panel.find('.ui-datatable-tablewrapper').animate({ height: 0 }, 400);
	} else {
		let tablewrapper = panel.find('.ui-datatable-tablewrapper');
		
		tablewrapper.css('height', 'auto');
		let height = tablewrapper.height();
		tablewrapper.height(0);
		
		panel.addClass('jeesl-active');
		tablewrapper.animate({ height: height }, 400);
	}
}

function initCollapsibleDatatable(parent) {
	let datatable = !!parent ? $(parent).find('.jeesl-datatable-collapsible') : $('.jeesl-datatable-collapsible');
	datatable.not('.jeesl-active').find('.ui-datatable-tablewrapper').height(0);
	datatable.find('.ui-datatable-header').click(toggleDatatable);
}

$(function() {
	menuHeightStyle = $('<style>').prop('type', 'text/css').appendTo('head');
	treeHeightStyle = $('<style>').prop('type', 'text/css').appendTo('head');
	
	let menuButtons = $('.jeesl-menu-bar-dropdown');
	let dropdowns = $('.jeesl-dropdown-list').attr('dropdown-id', (index, oldValue) => 'jeesl-dropdown-' + index);
	
	calculateMenuHeight(dropdowns);
	menuButtons.click(toggleMenu);
	
	$('.ui-tree-toggler').click(toggleTreeItem);
	
	let overlay = $('.jeesl-header .jeesl-dropdown-list');
	overlay.find('.jeesl-submenu-icon').click({ overlay: overlay }, toggleSubmenu);
	
	initCollapsibleDatatable();
});