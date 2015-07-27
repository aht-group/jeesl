package net.sf.ahtutils.factory.svg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.interfaces.model.symbol.UtilsGraphic;
import net.sf.ahtutils.interfaces.model.symbol.UtilsGraphicStyle;
import net.sf.ahtutils.xml.symbol.Symbol;

public class SvgSymbolFactory<L extends UtilsLang,
									D extends UtilsDescription,
									G extends UtilsGraphic<L,D,GT,GS>,
									GT extends UtilsStatus<GT,L,D>,
									GS extends UtilsStatus<GS,L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(SvgSymbolFactory.class);
		
	private DOMImplementation impl;
	
	public SvgSymbolFactory()
	{
		impl = SVGDOMImplementation.getDOMImplementation();
	}
	
    public static <L extends UtilsLang,
					D extends UtilsDescription,
					G extends UtilsGraphic<L,D,GT,GS>,
					GT extends UtilsStatus<GT,L,D>,
					GS extends UtilsStatus<GS,L,D>>
    	SvgSymbolFactory<L,D,G,GT,GS> factory()
	{
	    return new SvgSymbolFactory<L,D,G,GT,GS>();
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
		
		UtilsGraphicStyle.Code style = UtilsGraphicStyle.Code.circle;
		if(rule.getStyle()!=null && rule.getStyle().getCode()!=null)
		{
			style = UtilsGraphicStyle.Code.valueOf(rule.getStyle().getCode());
		}
		
		return build(impl,canvasSize,style,size,color);
	}
	
	public static SVGGraphics2D build(int canvasSize, Symbol rule)
	{
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		int size = 5; if(rule.isSetSize()){size = rule.getSize();}
		String color = "000000";if(rule.isSetColor()){color = rule.getColor();}
		
		UtilsGraphicStyle.Code style = UtilsGraphicStyle.Code.circle;
		if(rule.getStyle()!=null && rule.getStyle().getCode()!=null)
		{
			style = UtilsGraphicStyle.Code.valueOf(rule.getStyle().getCode());
		}
		return build(impl,canvasSize,style,size,color);
	}
	
	private static SVGGraphics2D build(DOMImplementation impl, int canvasSize, UtilsGraphicStyle.Code style, int size, String color)
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