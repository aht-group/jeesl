$(function() {
	var root = $('.html-tab .html-container > div').addClass('ui-tabs ui-widget ui-widget-content ui-corner-all ui-hidden-container ui-tabs-top');
	var mainSection = root.children().first();
	
	$('.html-tab .html-container > div > section > section').has('> h1:first-child, ' +
															     '> h2:first-child, ' +
															     '> h3:first-child, ' +
															     '> h4:first-child, ' +
															     '> h5:first-child, ' +
																 '> h6:first-child')
															.appendTo(root);

	if (mainSection.children().length === 0 || (mainSection.children().length === 1 && mainSection.children().has('h1, h2, h3, h4, h5'))) {
		mainSection.remove();
	}
	
	var tablist = root.prepend($('<ul></ul>').attr({ role: 'tablist' }).addClass('ui-tabs-nav ui-helper-reset ui-widget-header ui-corner-all')).children().first();
	var tabcontent = root.append($('<div></div>').addClass('ui-tabs-panel')).children().last();
	
	function select(index) {
		$('.html-tab .ui-tabs-header').removeClass('ui-state-active ui-tabs-selected');
		$('.html-tab .ui-tabs-panel > section').addClass('ui-helper-hidden');
		tablist.children().eq(index.data).addClass('ui-state-active ui-tabs-selected');
		tabcontent.children().eq(index.data).removeClass('ui-helper-hidden');
	}
	
	root.children('section').each(function(index) {
		var header = $(this).children().first();
		var tab = $('<li></li>').attr({
								          role: 'tab',
								          'data-index': index,
								          'aria-expanded': false,
								          'aria-selected': false
								      })
							    .addClass('ui-tabs-header ui-state-default ui-corner-top')
							    .append($('<a></a>').attr({ href: '#' })
							    		 			.append(header.text()));
		tab.click(index, select);
		tablist.append(tab);
		header.remove();
		
		tabcontent.append($(this));
	});
	
	select({ data: 0 });
});