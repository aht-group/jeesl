package org.jeesl.factory.svg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.jeesl.interfaces.model.system.symbol.JeeslGraphic;
import org.jeesl.interfaces.model.system.symbol.JeeslGraphicStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.status.Style;
import net.sf.ahtutils.xml.symbol.Size;
import net.sf.ahtutils.xml.symbol.Symbol;

public class SvgSymbolFactory<L extends UtilsLang,
									D extends UtilsDescription,
									G extends JeeslGraphic<L,D,G,GT,FS>,
									GT extends UtilsStatus<GT,L,D>,
									FS extends UtilsStatus<FS,L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(SvgSymbolFactory.class);
		
	private DOMImplementation impl;
	
	public SvgSymbolFactory()
	{
		impl = SVGDOMImplementation.getDOMImplementation();
	}
	
    public static <L extends UtilsLang,
					D extends UtilsDescription,
					G extends JeeslGraphic<L,D,G,GT,FS>,
					GT extends UtilsStatus<GT,L,D>,
					FS extends UtilsStatus<FS,L,D>>
    	SvgSymbolFactory<L,D,G,GT,FS> factory()
	{
	    return new SvgSymbolFactory<L,D,G,GT,FS>();
	}
    
	public static SVGGraphics2D build()
	{
		// Create an SVG document.
	    DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
	    SVGDocument doc = (SVGDocument) impl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

	    // Create a converter for this document.
	    SVGGraphics2D g = new SVGGraphics2D(doc);

	    // Do some drawing.
	    Shape circle = new Ellipse2D.Double(0, 0, 50, 50);
	    g.setPaint(Color.red);
	    g.fill(circle);
	    g.translate(60, 0);
	    g.setPaint(Color.green);
	    g.fill(circle);
	    g.translate(60, 0);
	    g.setPaint(Color.blue);
	    g.fill(circle);
	    g.setSVGCanvasSize(new Dimension(180, 50));
	    
	    return g;
	}
	
	public SVGGraphics2D build(int canvasSize, G rule)
	{
		int size = 5; if(rule.getSize()!=null){size = rule.getSize();}
		String color = "000000";if(rule.getColor()!=null){color = rule.getColor();}
		
		JeeslGraphicStyle.Code style = JeeslGraphicStyle.Code.circle;
		if(rule.getStyle()!=null && rule.getStyle().getCode()!=null)
		{
			style = JeeslGraphicStyle.Code.valueOf(rule.getStyle().getCode());
		}
		
		return build(impl,canvasSize,style,size,color);
	}
	
	public SVGGraphics2D square(int canvasSize, G rule)
	{
		int size = 5; if(rule.getSize()!=null){size = rule.getSize();}
		String color = "000000"; if(rule.getColor()!=null){color = rule.getColor();}
		
		JeeslGraphicStyle.Code style = JeeslGraphicStyle.Code.square;
		if(rule.getStyle()!=null && rule.getStyle().getCode()!=null)
		{
			style = JeeslGraphicStyle.Code.valueOf(rule.getStyle().getCode());
		}
		
		return test(impl,canvasSize,style,size,color);
	}
	
	public static SVGGraphics2D build(int canvasSize, Symbol rule)
	{
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		
		int size = 5;
		if(rule.isSetSizes() && rule.getSizes().isSetSize())
		{
			for(Size s : rule.getSizes().getSize())
			{
				if(s.getGroup().equals(JeeslGraphicStyle.Size.outer.toString()))
				{
					size = s.getValue();
				}
			}
		}
		
		String color = "000000";
		if(rule.isSetColors() && rule.getColors().isSetColor())
		{
			for(net.sf.ahtutils.xml.symbol.Color c : rule.getColors().getColor())
			{
				if(c.getGroup().equals(JeeslGraphicStyle.Color.outer.toString()))
				{
					color = c.getValue();
				}
			}
		}
		
		JeeslGraphicStyle.Code style = JeeslGraphicStyle.Code.circle;
		if(rule.isSetStyles() && rule.getStyles().isSetStyle())
		{
			for(Style s : rule.getStyles().getStyle())
			{
				if(s.getGroup().equals(JeeslGraphicStyle.Group.outer.toString()))
				{
					style = JeeslGraphicStyle.Code.valueOf(s.getCode());
				}
			}
		}

		return build(impl,canvasSize,style,size,color);
	}
	
	private static SVGGraphics2D build(DOMImplementation impl, int canvasSize, JeeslGraphicStyle.Code style, int size, String color)
	{
		 SVGDocument doc = (SVGDocument) impl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
		    SVGGraphics2D g = new SVGGraphics2D(doc);

		    double cS = canvasSize; double s = size;
		    double low = (cS - s)/2;
		    
		    logger.trace("Canvas: "+canvasSize+" low:"+low+" size:"+size);
		    
		    Shape shape = null;
		    switch(style)
		    {
		    	case circle:  shape = new Ellipse2D.Double(low, low, size, size);break;
		    	case square:  shape = new Rectangle2D.Double(low, low, size, size);break;
		    }
		    
		    g.setPaint(Color.decode("#"+color));
		    g.fill(shape);
		      
		    g.setSVGCanvasSize(new Dimension(canvasSize, canvasSize));
		    
		    return g;
	}
	
	private static SVGGraphics2D test(DOMImplementation impl, int canvasSize, JeeslGraphicStyle.Code style, int size, String color)
	{
		 SVGDocument doc = (SVGDocument) impl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
		    SVGGraphics2D g = new SVGGraphics2D(doc);

		    double cS = canvasSize; double s = size;
		    double low = (cS - s)/2;
		    
		    logger.trace("Canvas: "+canvasSize+" low:"+low+" size:"+size);
		    
		    Shape shape = null;
		    switch(style)
		    {
		    	case circle:  shape = new Ellipse2D.Double(low, low, size, size);break;
		    	case square:  shape = new Rectangle2D.Double(low, low, size, size);break;
		    }
		    
		    g.setPaint(Color.decode("#"+color));
		    g.fill(shape);
		      
		    g.setSVGCanvasSize(new Dimension(canvasSize, canvasSize));
		    
		    return g;
	}
}