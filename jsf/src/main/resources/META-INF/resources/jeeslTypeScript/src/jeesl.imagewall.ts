
$(function() {
  //justifiedGallery is a jQuery plugin imported from jquer.justifiedGallery.js
  ($('.jeesl-image-wall-wrapper') as any).justifiedGallery({ // Use type assertion to call the function
	border: 0,
	margins: 5,
	rowHeight: 80
  });
});