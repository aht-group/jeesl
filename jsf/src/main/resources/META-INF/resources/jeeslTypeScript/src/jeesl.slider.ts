import 'slick-carousel';
(function($): void {
    $(() => {
        ($('.jeesl-carousel-wrapper') as any).slick({
            adaptiveHeight: true,
            autoplay: true,
            autoplaySpeed: 5000,
            centerMode: true,
            nextArrow: '<button type="button" class="slick-next ui-button ui-state-default ui-corner-all"><span class="ui-icon ui-icon-circle-triangle-e"></span></button>',
            prevArrow: '<button type="button" class="slick-prev ui-button ui-state-default ui-corner-all"><span class="ui-icon ui-icon-circle-triangle-w"></span></button>',
            swipeToSlide: true
        }).on('beforeChange', (event: any, slick: any, currentSlide: number, nextSlide: number) => {
            let imageAlt = $(slick.$slides[currentSlide + 1]).find('img').attr('alt');
            imageAlt = typeof imageAlt !== 'undefined' ? imageAlt : '';
            $(<HTMLElement> event.target).parent().parent().find('.ui-panel-title').fadeTo(150, 0, function() {
                $(this).html(imageAlt).fadeTo(150, 1);
            });
        }).on('reinit', (event: any, slick: any) => {
            let imageAlt =  $(slick.$slides[0]).find('img').attr('alt');
            imageAlt = typeof imageAlt !== 'undefined' ? imageAlt : '';
            $(<HTMLElement> event.target).parent().parent().find('.ui-panel-title').html(imageAlt);
        });

        ($('.jeesl-galleria-wrapper') as any).slick({
            adaptiveHeight: true,
            arrows: false,
            asNavFor: '.jeesl-galleria-filmstrip-wrapper',
            draggable: false,
            fade: true
        });

        ($('.jeesl-galleria-filmstrip-wrapper') as any).slick({
            asNavFor: '.jeesl-galleria-wrapper',
            autoplay: true,
            autoplaySpeed: 5000,
            centerMode: true,
            centerPadding: '22px',
            draggable: false,
            focusOnSelect: true,
            slidesToShow: 5
        });
    });
})(jQuery.noConflict(true));