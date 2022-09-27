const searchField = $('.jeesl-menu-bar-search .ui-inputfield');
const cancelButton = $('.jeesl-menu-bar-search .search-btn-light');

let previousHeight = 0;

searchField.on('input', inputChange);
cancelButton.click((event) => {
	event.preventDefault();
	searchField.val('');
	searchField.trigger('input');
})
inputChange();

function inputChange() {
	searchField.toggleClass('ui-state-filled', !!searchField.val());
	cancelButton.toggleClass('ui-state-visible', !!searchField.val());
}

function toggleDetails(eventArgs) {
	let currentListItem = $(this).parent().parent();
	let closeRequested = currentListItem.children('.jeesl-field-tip').eq(0).height() > listItemHeight;
	
	eventArgs.data.overlay.find('.ui-selectonemenu-item').removeClass('jeesl-active');
	eventArgs.data.overlay.find('.jeesl-field-tip').height(listItemHeight);
	if (!closeRequested) {
		currentListItem.addClass('jeesl-active');
		let fieldTip = $(this).parent();
		fieldTip.height(fieldTip.children('.jeesl-field-tip-content').eq(0).outerHeight() + listItemHeight);
	}
}

function showResults() {
	let input = $(requestQuery.replaceAll(':','\\:'));
	let overlay = $(resultQuery.replaceAll(':','\\:')).parent();
	
	let targetHeight = overlay.height();
	input.val() ? overlay.height(previousHeight).addClass('jeesl-visible').animate({ height: targetHeight }, 400, () => {
		overlay.height('auto');
		previousHeight = overlay.height();
	}) : hideResults(overlay);
	
//	input.val() ? overlay.addClass('jeesl-visible') : overlay.removeClass('jeesl-visible');
	
	overlay.find('.jeesl-field-tip-icon').click({ overlay: overlay }, toggleDetails);
}

function hideResults(overlay) {
	overlay = overlay || $(resultQuery.replaceAll(':','\\:')).parent();
	
	overlay.height(previousHeight).animate({ height: 0 }, 400, () => {
		overlay.removeClass('jeesl-visible');
		overlay.height('auto');
		previousHeight = 0;
	});
}