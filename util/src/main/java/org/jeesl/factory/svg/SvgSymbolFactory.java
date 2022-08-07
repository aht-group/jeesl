package org.jeesl.factory.svg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;

import net.sf.ahtutils.xml.status.Style;
import net.sf.ahtutils.xml.symbol.Size;
import net.sf.ahtutils.xml.symbol.Symbol;

public class SvgSymbolFactory<L extends JeeslLang, D extends JeeslDescription,
								G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
								F extends JeeslGraphicComponent<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>>
{
	final static Logger logger = LoggerFactory.getLogger(SvgSymbolFactory.class);
		
	private DOMImplementation impl;
	
	public SvgSymbolFactory()
	{
		impl = SVGDOMImplementation.getDOMImplementation();
	}
	
    public static <L extends JeeslLang, D extends JeeslDescription,
				    G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
					F extends JeeslGraphicComponent<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>>
    	SvgSymbolFactory<L,D,G,GT,F,FS> factory()
	{
	    return new SvgSymbolFactory<L,D,G,GT,F,FS>();
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
		
		JeeslGraphicShape.Code style = JeeslGraphicShape.Code.circle;
		if(rule.getStyle()!=null && rule.getStyle().getCode()!=null)
		{
			style = JeeslGraphicShape.Code.valueOf(rule.getStyle().getCode());
		}
		
		return build(impl,canvasSize,style,size,color);
	}
	
	public SVGGraphics2D square(int canvasSize, G rule)
	{
		int size = 5; if(rule.getSize()!=null){size = rule.getSize();}
		String color = "000000"; if(rule.getColor()!=null){color = rule.getColor();}
		
		JeeslGraphicShape.Code style = JeeslGraphicShape.Code.square;
		if(rule.getStyle()!=null && rule.getStyle().getCode()!=null)
		{
			style = JeeslGraphicShape.Code.valueOf(rule.getStyle().getCode());
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
				if(s.getGroup().equals(JeeslGraphicShape.Size.outer.toString()))
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
				if(c.getGroup().equals(JeeslGraphicShape.Color.outer.toString()))
				{
					color = c.getValue();
				}
			}
		}
		
		JeeslGraphicShape.Code style = JeeslGraphicShape.Code.circle;
		if(rule.isSetStyles() && rule.getStyles().isSetStyle())
		{
			for(Style s : rule.getStyles().getStyle())
			{
				if(s.getGroup().equals(JeeslGraphicShape.Group.outer.toString()))
				{
					style = JeeslGraphicShape.Code.valueOf(s.getCode());
				}
			}
		}

		return build(impl,canvasSize,style,size,color);
	}
	
	private static SVGGraphics2D build(DOMImplementation impl, int canvasSize, JeeslGraphicShape.Code style, int size, String color)
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
			case triangle: logger.warn("NYI: Triangle Shape !!"); break;
		}
		    
		g.setPaint(Color.decode("#"+color));
		g.fill(shape);
		      
		g.setSVGCanvasSize(new Dimension(canvasSize, canvasSize));
		    
		return g;
	}
	
	private static SVGGraphics2D test(DOMImplementation impl, int canvasSize, JeeslGraphicShape.Code style, int size, String color)
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
	    	case triangle: logger.warn("NYI: Triangle Shape !!"); break;
	    }
	    
	    g.setPaint(Color.decode("#"+color));
	    g.fill(shape);
	      
	    g.setSVGCanvasSize(new Dimension(canvasSize, canvasSize));
	    
	    return g;
	}
}