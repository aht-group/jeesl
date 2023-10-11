var timeout;
function onChange() {
	if (timeout) { clearTimeout(timeout); }
	
	timeout = setTimeout(() => {
		console.log('change called');
		if (!deeplTranslate) {
			console.log('deeplTranslate does not exist!');
			return;
		}
		
		deeplTranslate();
	}, 3000);
}

$(function() {
	$('.jeesl-deepl-textarea').on('input', onChange);
});