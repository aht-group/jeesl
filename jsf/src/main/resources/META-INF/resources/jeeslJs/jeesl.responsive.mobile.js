/**
 * In order to set the correct style element for mobile devices, we need to overwrite the
 * calculateMenuHeight method to exclude the media query.
 */
calculateMenuHeight = function() {
	let cssRules = '';
	$('.jeesl-dropdown-list').each((index, element) =>
		cssRules += '#' +
					$(element).attr('id') +
					'.jeesl-active { height: ' +
					($(element).children('.jeesl-dropdown-item')
							  .toArray()
							  .map(child => $(child).outerHeight())
							  .reduce((previous, current) => previous + current, 0) + 15 + $(element).hasClass('.jeesl-dropdown-list-multi') * 15) +
					'px; }');
	menuHeightStyle.append(cssRules);
}