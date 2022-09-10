package org.jeesl.web.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.TranscoderException;
import org.jeesl.api.facade.system.graphic.JeeslGraphicFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicFactory;
import org.jeesl.factory.svg.SvgFigureFactory;
import org.jeesl.factory.svg.SvgSymbolFactory;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.openfuxml.content.media.Image;
import org.openfuxml.media.transcode.Svg2SvgTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractGraphicSymbolizerServlet<L extends JeeslLang, D extends JeeslDescription,
												S extends EjbWithId,
												G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
												GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>>
	extends AbstractSymbolizerServlet<L,D,G,GT,GC,GS>
	implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractGraphicSymbolizerServlet.class);
	
	private final Class<GC> cF;
	
	private final SvgSymbolFactory<L,D,G,GT,GC,GS> fSvgGraphic;
	private final SvgFigureFactory<L,D,G,GT,GC,GS> fSvgFigure;
	
	private boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	public AbstractGraphicSymbolizerServlet(SvgFactoryBuilder<L,D,G,GT,GC,GS> fbStatus)
	{
		this.cF = fbStatus.getClassFigure();
		fSvgGraphic = fbStatus.symbol();
		fSvgFigure = fbStatus.figure();
	}
	
	public AbstractGraphicSymbolizerServlet(final Class<GC> cF)
	{
		this.cF=cF;
		fSvgGraphic = SvgSymbolFactory.factory();
		fSvgFigure = SvgFigureFactory.factory();
	}
	
	@SuppressWarnings("unchecked")
	protected void symbolizer(JeeslGraphicFacade<L,D,S,G,GT,GC,GS> fGraphic, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
		Image m = super.getGraphicInfo(request,response);
		
	    if(m == null)
	    {
	    	response.sendError(HttpServletResponse.SC_NOT_FOUND);
	    	return;
	    }
	    else
	   	{
	    	String cacheKey = EjbGraphicFactory.toCacheKey(m);
	    	boolean inCache = this.cacheContainsKey(cacheKey);
			if(debugOnInfo) {logger.info("Using cached value: "+inCache+" ("+cacheKey+")");}
	    	if(inCache)
	    	{	
	    		try
	    		{
	    			G g = this.cacheGet(cacheKey);
					process(request,response,fGraphic,g,m);
				}
	    		catch (TranscoderException e) {logger.error(e.getMessage());response.sendError(HttpServletResponse.SC_NOT_FOUND);}
		   		catch (UtilsProcessingException e) {logger.error(e.getMessage());response.sendError(HttpServletResponse.SC_NOT_FOUND);}
	    	}
	    	else
	    	{
	    		try
	    		{
	    			Class<EjbWithGraphic<G>> c = (Class<EjbWithGraphic<G>>)Class.forName(m.getVersion()).asSubclass(EjbWithGraphic.class);
	    			if(EjbWithGraphic.class.isAssignableFrom(c))
	    			{
	    				G g = fGraphic.fGraphic(c,Long.valueOf(m.getId()));
	    				this.cachePut(cacheKey,g);
		            	process(request,response,fGraphic,g,m);
	    			}
	    			else {logger.error("Class "+c.getName()+" not assingable from "+EjbWithGraphic.class.getName());response.sendError(HttpServletResponse.SC_NOT_FOUND);}
	    		}
	    		catch (TranscoderException e) {logger.error(e.getMessage());response.sendError(HttpServletResponse.SC_NOT_FOUND);}
	    		catch (JeeslNotFoundException e) {logger.error(e.getMessage());response.sendError(HttpServletResponse.SC_NOT_FOUND);}
		   		catch (UtilsProcessingException e) {logger.error(e.getMessage());response.sendError(HttpServletResponse.SC_NOT_FOUND);}
		   		catch (ClassNotFoundException e) {logger.error(e.getMessage());response.sendError(HttpServletResponse.SC_NOT_FOUND);}
	    	}
	   	}
    }
	
	protected boolean cacheContainsKey(String key) {return false;}
	protected void cachePut(String key, G g) {}
	protected G cacheGet(String key) {return null;}
	
	protected void process(HttpServletRequest request, HttpServletResponse response, G graphic, Image image) throws IOException, TranscoderException, UtilsProcessingException
    {
		byte[] bytes = null;
    	
		String id = image.getId();
		int size = (int) image.getHeight().getValue();
		
		if(graphic==null){throw new UtilsProcessingException("graphic is null");}
		if(graphic.getType()==null){throw new UtilsProcessingException("graphic.type is null");}
    	if(graphic.getType().getCode().equals(JeeslGraphicType.Code.symbol.toString()))
		{
    		if(debugOnInfo){logger.info("Build SVG: size " + size + " id:" + id);}
		    SVGGraphics2D g = fSvgGraphic.build(size,graphic);
		    bytes = Svg2SvgTranscoder.transcode(g);
		    respond(request,response,bytes,"svg");
		}
	    else if(graphic.getType().getCode().equals(JeeslGraphicType.Code.svg.toString()))
	    {
	    	respond(request,response,graphic.getData(),"svg");
	    }
	}
	
	protected void process(HttpServletRequest request, HttpServletResponse response, JeeslGraphicFacade<L,D,S,G,GT,GC,GS> fGraphic, G graphic, Image image) throws IOException, TranscoderException, UtilsProcessingException
    {
		byte[] bytes = null;
    	
		String id = image.getId();
		int size = (int) image.getHeight().getValue();
		
		if(graphic==null){throw new UtilsProcessingException("graphic is null");}
		if(graphic.getType()==null){throw new UtilsProcessingException("graphic.type is null");}
	    if(graphic.getType().getCode().equals(JeeslGraphicType.Code.symbol.toString()))
		{
	    	if(debugOnInfo){logger.info("Build SVG: size " + size + " id:" + id);}
	    	List<GC> figures = fGraphic.allForParent(cF,graphic);
			if(figures.isEmpty())
			{
		    	SVGGraphics2D g = fSvgGraphic.build(size,graphic);
		    	bytes = Svg2SvgTranscoder.transcode(g);
		    	respond(request,response,bytes,"svg");
			}
			else
			{
				SVGGraphics2D g = fSvgFigure.build(figures,size);
		    	bytes = Svg2SvgTranscoder.transcode(g);
		    	respond(request,response,bytes,"svg");
			}
		}
	    else if(graphic.getType().getCode().equals(JeeslGraphicType.Code.svg.toString()))
	    {
	    	respond(request,response,graphic.getData(),"svg");
	    }
	    else if(graphic.getType().getCode().equals(JeeslGraphicType.Code.png.toString()))
	    {
	    	respond(request,response,graphic.getData(),"png");
	    }
	}
}