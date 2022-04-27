const searchField = $('.jeesl-menu-bar-search .ui-inputfield');
const cancelButton = $('.jeesl-menu-bar-search .search-btn-light');

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