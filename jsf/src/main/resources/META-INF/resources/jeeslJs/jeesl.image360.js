$(function() {
	let $viewerElements = $('.jeesl-360-viewer');
	let viewerOptions = { controls: { mouseViewMode: 'drag' } };
	
	let viewers = [];
	
	$viewerElements.each((index, element) => viewers.push(new Marzipano.Viewer(element, viewerOptions)));
	
//	let viewer = new Marzipano.Viewer(viewerElement[0], viewerOptions);
	
	let levels = [{ tileSize: 512, size: 512 }];
	let geometry = new Marzipano.CubeGeometry(levels);
	let view = new Marzipano.RectilinearView();
	
	$viewerElements.each((index, element) => {
		let $element = $(element);
		
		let viewer = new Marzipano.Viewer(element, viewerOptions);
		let source = Marzipano.ImageUrlSource.fromString(sources[element.id]);
		let scene = viewer.createScene({ source: source,
										 geometry: geometry,
										 view: view,
										 pinFirstLevel: true });
		
		$element.height($element.width());
		scene.switchTo();
	});
});