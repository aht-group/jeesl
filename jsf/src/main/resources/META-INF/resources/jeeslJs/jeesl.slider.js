$(function() {
	$('.jeesl-galleria-wrapper').slick({
		adaptiveHeight: true,
		arrows: false,
		asNavFor: '.jeesl-galleria-filmstrip-wrapper',
		draggable: false,
		fade: true
	});
	$('.jeesl-galleria-filmstrip-wrapper').slick({
		asNavFor: '.jeesl-galleria-wrapper',
		autoplay: true,
		autoplaySpeed: 5000,
		centerMode: true,
		draggable: false,
		focusOnSelect: true,
		slidesToShow: 5
	})
})