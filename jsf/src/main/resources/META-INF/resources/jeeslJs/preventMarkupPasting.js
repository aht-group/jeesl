// Prevents pasting of markup in contenteditable DIVs (e.g. PrimeFaces Editor)
window.addEventListener('load', function () {
    $("div[contenteditable='true']").each(function() {
       $(this).bind('paste', function (e) {
	 e.preventDefault();
	 var content = (e.originalEvent || e).clipboardData.getData('Text');
	 console.log(content);
	 document.execCommand('insertText', false, content);
	});
    });
});