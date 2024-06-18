
let menuHeightStyle: JQuery<HTMLElement>;
let treeHeightStyle: JQuery<HTMLElement>;
let listItemHeight: number;

declare function calculateMenuHeight(dropdowns?: JQuery<HTMLElement>): void;
window.calculateMenuHeight = function() {
    let cssRules = '@media (max-width: 768px) {' + cssRulesMenuHeightCalculate() + '}';
    menuHeightStyle.append(cssRules);
}

function cssRulesMenuHeightCalculate(): string {
    let cssRules = ''; 
    $('.jeesl-dropdown-list').each((index: number, element: HTMLElement) => {
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
    });
    return cssRules;
}

function toggleTreeItem(this: HTMLElement): void {
    $(this).parent().next('.ui-treenode-children').toggleClass('jeesl-active');
}

function findTreeRoot(treeNode: JQuery<HTMLElement>): JQuery<HTMLElement> {
    return treeNode.parent().hasClass('ui-treenode-children') ? findTreeRoot(treeNode.parent().parent()) : treeNode;
}

function setTreeHeight(node: JQuery<HTMLElement>): number {
    let height = 0;
    let childList = node.children('.ui-treenode-children');
    
    if (node.hasClass('jeesl-active')) {
        height += childList.first()
                           .children('.ui-treenode')
                           .map((index: number, item: HTMLElement) => setTreeHeight($(item)))
                           .toArray()
                           .reduce((accumulator: number, currentValue: number) => accumulator + currentValue, 0)
                + childList.first().children('.ui-tree-droppoint:last-child').length * 4;
    }
    
    childList.height(height);
    return height + 50;
}

function toggleMenu(this: HTMLElement): void {
    $('.jeesl-menu-bar-dropdown').filter((index: number, button: HTMLElement) => button !== this).removeClass('jeesl-active').siblings('.jeesl-dropdown-list').removeClass('jeesl-active');
    
    $(this).filter((index: number, current : HTMLElement) => current  !== this).find('.jeesl-greyscale')
    .toggleClass('jeesl-active').siblings('.jeesl-dropdown-list').toggleClass('jeesl-active').find('.jeesl-open').removeClass('jeesl-open');
}

function reloadStatusBar(): void {
    let newButtons : JQuery<HTMLElement> = $('.jeesl-status-bar .jeesl-menu-bar-dropdown');
    let newDropdowns : JQuery<HTMLElement> = $('.jeesl-status-bar .jeesl-dropdown-list').attr('id', (index: number, oldValue: string) => 'jeesl-dropdown-' + ($('.jeesl-dropdown-list').length + index));
    
    calculateMenuHeight(newDropdowns);
    newButtons.click(toggleMenu);
}

function toggleSubmenu(this: HTMLElement, eventArgs: JQuery.Event): void {
    let currentSubmenu = $(this).parent();
    let currentListItem = currentSubmenu.parent();
    let closeRequested: boolean;
    let submenuHeight: number | undefined = currentListItem.children('.jeesl-submenu').eq(0).height();
    
    if (listItemHeight !== undefined && submenuHeight !== undefined && submenuHeight > listItemHeight) 
    {
         closeRequested = true;
    } 
    else 
    {    
        closeRequested = false;
    }
   
    let overlay = currentListItem.parent();
    overlay.height('auto');
    let submenus = overlay.find('.jeesl-submenu');

    if (!closeRequested) { currentSubmenu.addClass('jeesl-open'); }
    submenus.each(function() {
        let $this = $(this);
        let dropDownSubOuterHeight: number | undefined = $this.children('.jeesl-dropdown-sub').eq(0).outerHeight();
        $this.stop().animate({ 
            height: $this.parent().is(currentListItem) && !closeRequested ?  (dropDownSubOuterHeight ?? 0) + listItemHeight : listItemHeight }, 400, function() {
                if ($this.height() === listItemHeight) { $this.removeAttr('style'); }
        });
    });
    $.when(submenus).then(() => {
        submenus.each(function() { $(this).toggleClass('jeesl-open', $(this).is(currentSubmenu) && !closeRequested); });
        overlay.height(overlay.children().map((index: number, child: HTMLElement) => $(child).outerHeight()).toArray().reduce((previous: number, current: number) => previous + current, 0) + 30);
        if (overlay.find('.jeesl-open').length === 0) { overlay.removeAttr('style'); }
    });
}

function toggleDatatable(this: HTMLElement, eventArgs: JQuery.Event): void {
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

function initCollapsibleDatatable(parent?: JQuery<HTMLElement>): void {
    let datatable = !!parent ? $(parent).find('.jeesl-datatable-collapsible') : $('.jeesl-datatable-collapsible');
    datatable.not('.jeesl-active').find('.ui-datatable-tablewrapper').height(0);
    datatable.find('.ui-datatable-header').click(toggleDatatable);
}

(function($) {
    menuHeightStyle = $('<style>').prop('type', 'text/css').appendTo('head');
    treeHeightStyle = $('<style>').prop('type', 'text/css').appendTo('head');
    
    let menuButtons = $('.jeesl-menu-bar-dropdown');
    let dropdowns = $('.jeesl-dropdown-list').attr('id', (index: number, oldValue: string) => 'jeesl-dropdown-' + index);
    
    calculateMenuHeight(dropdowns);
    menuButtons.click(toggleMenu);
    
    $('.ui-tree-toggler').click(toggleTreeItem);
    
    let overlay = $('.jeesl-header .jeesl-dropdown-list');
    overlay.find('.jeesl-submenu-icon').click({ overlay: overlay }, toggleSubmenu);
    
    initCollapsibleDatatable();
})(jQuery.noConflict(true));