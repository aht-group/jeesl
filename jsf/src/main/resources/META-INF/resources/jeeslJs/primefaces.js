// Set selectonemenu panel width to width of widget.
// Why the hell is this not the default behaviour? Not even optional?!
if (PrimeFaces.widget.SelectOneMenu) {
    PrimeFaces.widget.SelectOneMenu.prototype.alignPanelWidth = function() {
		var jqWidth = this.jq.outerWidth();
		this.panel.css({ 'min-width': jqWidth + 'px' });
    }
}