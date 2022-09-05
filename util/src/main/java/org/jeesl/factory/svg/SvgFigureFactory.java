package org.jeesl.factory.svg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.ext.awt.geom.Polygon2D;
import org.apache.batik.svggen.SVGGraphics2D;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;

public class SvgFigureFactory<L extends JeeslLang, D extends JeeslDescription,
								G extends JeeslGraphic<GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
								F extends JeeslGraphicComponent<G,GT,F,FS>, FS extends JeeslGraphicShape<L,D,FS,G>>
{
	final static Logger logger = LoggerFactory.getLogger(SvgFigureFactory.class);
	
	private DOMImplementation impl;
	
	private SvgFigureFactory()
	{
		impl = SVGDOMImplementation.getDOMImplementation();
	}
	
    public static <L extends JeeslLang, D extends JeeslDescription,
    			G extends JeeslGraphic<GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
    			F extends JeeslGraphicComponent<G,GT,F,FS>, FS extends JeeslGraphicShape<L,D,FS,G>>
		SvgFigureFactory<L,D,G,GT,F,FS> factory()
	{
    	return new SvgFigureFactory<L,D,G,GT,F,FS>();
	}
	
	public SVGGraphics2D build(List<F> list, int canvasSize)
	{
		SVGDocument doc = (SVGDocument) impl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
		SVGGraphics2D g = new SVGGraphics2D(doc);
		
		g.translate(canvasSize / 2, canvasSize / 2);
		
		for (F f : list)
		{
			boolean triangleForm = false;
			g.create();
			Shape shape = null;
			g.setPaint(Color.decode("#" + f.getColor()));
			switch (JeeslGraphicShape.Code.valueOf(f.getStyle().getCode()))
			{
				case circle:
					shape = new Ellipse2D.Double(f.getOffsetX(), f.getOffsetY(), f.getSize(), f.getSize());
					g.translate(-(f.getSize() / 2), -(f.getSize() / 2));
					break;
				
				case square:
					shape = new Rectangle2D.Double(f.getOffsetX(), f.getOffsetY(), f.getSize(), f.getSize());
					g.translate(-(f.getSize() / 2), -(f.getSize() / 2));
					break;
				
				case triangle:
					triangleForm = true;
					g.translate(-(canvasSize / 2) + (f.getOffsetY()), -(canvasSize / 2) + (f.getOffsetX()));
					float[] xx = { (float) (canvasSize - ((float) f.getSize() * 2)),
							(float) (((float) f.getSize() * 2)), canvasSize / 2 };
					float[] yy = { (float) (canvasSize - ((float) f.getSize() * 2)),
							(float) ((float) canvasSize - (f.getSize() * 2)), (float) ((f.getSize() * 2)) };
					shape = new Polygon2D(xx, yy, 3);
					Rectangle2D bounds = shape.getBounds();
					double angle = (Math.PI * f.getRotation()) / 180;
					AffineTransform transform = new AffineTransform();
					transform.rotate(angle, bounds.getCenterX(), bounds.getCenterY());
					Path2D path = (shape instanceof Path2D) ? (Path2D) shape : new GeneralPath(shape);
					Shape rotated = path.createTransformedShape(transform);
					
					g.fill(rotated);
					g.translate(-g.getTransform().getTranslateX(), -g.getTransform().getTranslateY());
					g.translate(canvasSize / 2, canvasSize / 2);
			}
			if (f.getRotation() != 0)
			{
				if (triangleForm != true)
				{
					double angle = (Math.PI * f.getRotation()) / 180;
					g.rotate(angle, f.getSize() / 2, f.getSize() / 2);
					g.fill(shape);
					g.rotate(-angle, f.getSize() / 2, f.getSize() / 2);
					g.translate(-g.getTransform().getTranslateX(), -g.getTransform().getTranslateY());
					g.translate(canvasSize / 2, canvasSize / 2);
				}
			}
			if (f.getRotation() == 0)
			{
				if (triangleForm != true)
				{
					g.fill(shape);
					g.translate(-g.getTransform().getTranslateX(), -g.getTransform().getTranslateY());
					g.translate(canvasSize / 2, canvasSize / 2);
				}
			}
		}
		
		g.setSVGCanvasSize(new Dimension(canvasSize, canvasSize));
		return g;
	}
}
