$(function() {
	function collapseSection(element) {
		element.attr('data-collapsed', true);
		element.stop().animate({ height: '33px' }, 1000);
	}
				
	function expandSection(element) {
		element.attr('data-collapsed', false);
		element.stop().animate({ height: (element.prop('scrollHeight') + 2) + 'px' }, 1000);
	}
	
	function toggleAccordion() {
		var section = $(this).parent();
		
		if (section.attr('data-collapsed') === 'true') {
			$('.html-accordion .html-container section[data-collapsed=false]').each(function() {
				collapseSection($(this));
			});
			
			expandSection(section);
		} else {
			collapseSection(section);
		}
	}
	
	var root = $('.html-accordion .html-container > div');
	var mainSection = root.children().first();
	
	$('.html-accordion .html-container > div > section > section').has('> h1:first-child, ' +
																	   '> h2:first-child, ' +
																	   '> h3:first-child, ' +
																	   '> h4:first-child, ' +
																	   '> h5:first-child, ' +
																	   '> h6:first-child')
																  .appendTo(root);
	
	if (mainSection.children().length === 0 || (mainSection.children().length === 1 && mainSection.children().has('h1, h2, h3, h4, h5'))) {
		mainSection.remove();
	}

	$('.html-accordion .html-container > div > section > h1:first-child, ' +
	  '.html-accordion .html-container > div > section > h2:first-child, ' +
	  '.html-accordion .html-container > div > section > h3:first-child, ' +
	  '.html-accordion .html-container > div > section > h4:first-child, ' +
	  '.html-accordion .html-container > div > section > h5:first-child').addClass('html-accordion-header');
	
	$('.html-accordion-header').parent().each(function() {
		$(this).css('height', 33 + 'px');
		$(this).attr('data-collapsed', true);
	});
	$('.html-accordion-header').click(toggleAccordion);
});