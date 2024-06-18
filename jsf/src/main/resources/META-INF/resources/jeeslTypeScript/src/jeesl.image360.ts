import 'marzipano';
var Marzipano = require('marzipano');
declare const sources: { [key: string]: string };

(function($) {
    let $viewerElements: JQuery = $('.jeesl-360-viewer');
    let viewerOptions: { controls: { mouseViewMode: string } } = { controls: { mouseViewMode: 'drag' } };
    
    let viewers: any[] = [];
    
    $viewerElements.each((index: number, element: HTMLElement) => 
		{
			viewers.push(new Marzipano.Viewer(element, viewerOptions));
		}
		);
    
    let levels: { tileSize: number, size: number }[] = [{ tileSize: 512, size: 512 }];
    let geometry: any = new Marzipano.CubeGeometry(levels);
	let view: any = new Marzipano.RectilinearView();
	
	$viewerElements.each((index: number, element: HTMLElement) => {
		let $element: JQuery = $(element);
		
		let viewer: any = new Marzipano.Viewer(element, viewerOptions);
		let source: any = Marzipano.ImageUrlSource.fromString(sources[element.id]);
		let scene: any = viewer.createScene({ source: source,
										 geometry: geometry,
										 view: view,
										 pinFirstLevel: true });
		let elementWidth: number = $element.width() ?? 0;
		$element.height(elementWidth);
		scene.switchTo();
	});
})(jQuery.noConflict(true));