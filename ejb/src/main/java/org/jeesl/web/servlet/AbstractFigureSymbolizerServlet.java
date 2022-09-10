package org.jeesl.web.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.TranscoderException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.factory.svg.SvgFigureFactory;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.openfuxml.content.media.Image;
import org.openfuxml.media.transcode.Svg2SvgTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractFigureSymbolizerServlet<L extends JeeslLang, D extends JeeslDescription,
												G extends JeeslGraphic<GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
												F extends JeeslGraphicComponent<G,GT,F,FS>, FS extends JeeslGraphicShape<L,D,FS,G>>
	extends AbstractSymbolizerServlet<L,D,G,GT,F,FS>
	implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractFigureSymbolizerServlet.class);
	
	private SvgFigureFactory<L,D,G,GT,F,FS> svgF;
	
	private AbstractFigureSymbolizerServlet()
	{
		svgF = SvgFigureFactory.factory();
	}
		
	protected void process(HttpServletRequest request, HttpServletResponse response, F figure, Image image) throws ServletException, IOException, TranscoderException, UtilsProcessingException
    {
		byte[] bytes = null;
    	
		String id = image.getId();
		int size = (int) image.getHeight().getValue();
		
		if(figure==null){throw new UtilsProcessingException("graphic is null");}
    	
		List<F> figures = new ArrayList<F>();
		figures.add(figure);
		
		logger.info("Build SVG: size " + size + " id:" + id);
    	SVGGraphics2D g = svgF.build(figures,size);
    	bytes = Svg2SvgTranscoder.transcode(g);
    	respond(request,response,bytes,"svg");
	}
}