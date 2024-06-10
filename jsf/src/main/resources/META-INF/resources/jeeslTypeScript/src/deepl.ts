declare function deeplTranslate(): void;

$(function() {
    let timeout: NodeJS.Timeout;
    $('.jeesl-deepl-textarea').on('input', () => {
        if (timeout) { 
            clearTimeout(timeout); 
        }

        timeout = setTimeout(() => {
            console.log('change called');
            if (!deeplTranslate) {
                console.log('deeplTranslate does not exist!');
                return;
            }

            deeplTranslate();
        }, 3000);
    });
});


