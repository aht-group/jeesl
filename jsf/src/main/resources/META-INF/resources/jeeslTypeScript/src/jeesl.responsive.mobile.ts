/**
 * In order to set the correct style element for mobile devices, we need to overwrite the
 * calculateMenuHeight method to exclude the media query.
 */
window.calculateMenuHeight = function() {
    let cssRules: string = '' + cssRulesMenuHeightCalculate();
    /*
    $('.jeesl-dropdown-list').each((index: number, element: HTMLElement) => {
        // Check if element is undefined and the element has children with the class jeesl-dropdown-item
        if($(element) !== undefined && $(element).children('.jeesl-dropdown-item')!== undefined) {
            cssRules += '#' +
            $(element).attr('id') +
            '.jeesl-active { height: ' +
                    $(element).children('.jeesl-dropdown-item')
                       .toArray()
                       .map((child: HTMLElement) => $(child).outerHeight())
                       .filter((value: number | undefined): value is number => value !== undefined)
                       .reduce((previousValue: number, currentValue: number, currentIndex: number, array: number[]) => previousValue + currentValue, 0)
                    + 15 + (Number(!$(element).hasClass('jeesl-dropdown-list-multi')) * 15)
                    + 'px; }';
        }
    });
    */
    menuHeightStyle.append(cssRules);
}