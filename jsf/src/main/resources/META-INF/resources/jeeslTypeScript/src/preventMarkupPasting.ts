window.addEventListener('load', function (): void {
    $("div[contenteditable='true']").each(function(): void {
        $(this).bind('paste', function (e: JQuery.TriggeredEvent): void {
            e.preventDefault();
            let clipboardData = (e.originalEvent as ClipboardEvent).clipboardData;
            if (clipboardData) {
                let content: string = clipboardData.getData('Text');
                console.log(content);
                document.execCommand('insertText', false, content);
            }
        });
    });
});