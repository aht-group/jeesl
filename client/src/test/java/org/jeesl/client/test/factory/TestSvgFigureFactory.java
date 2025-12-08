package org.jeesl.client.test.factory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.TranscoderException;
import org.jeesl.client.app.JeeslBootstrap;
import org.jeesl.client.model.ejb.system.graphic.Graphic;
import org.jeesl.client.model.ejb.system.graphic.GraphicComponent;
import org.jeesl.client.model.ejb.system.graphic.GraphicShape;
import org.jeesl.client.model.ejb.system.graphic.GraphicType;
import org.jeesl.client.model.ejb.system.locale.Description;
import org.jeesl.client.model.ejb.system.locale.Lang;
import org.jeesl.client.test.AbstractJeeslClientTest;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.io.graphic.EjbGraphicComponentFactory;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.factory.svg.SvgFigureFactory;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.junit.jupiter.api.Test;
import org.openfuxml.media.transcode.Svg2SvgTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractJeeslTest;

public class TestSvgFigureFactory extends AbstractJeeslClientTest
{
	final static Logger logger = LoggerFactory.getLogger(TestSvgFigureFactory.class);

	private EjbStatusFactory<Lang,Description,GraphicShape> efStyle;
	private EjbGraphicComponentFactory<Graphic,GraphicComponent,GraphicShape> efFigure;
	private static SvgFigureFactory<Graphic,GraphicType,GraphicComponent,GraphicShape> svgF;
	
	private String colorRed = "FF0000";
	private String colorBlue = "0000FF";
	private String colorGreen = "00FF00";
	private String colorWhite = "FFFFFF";
	private String colorBlack = "000000";
	private String colorPink = "FF88FF";
	
	private GraphicShape styleCircle,styleSquare,styleTriangle;
	private GraphicComponent f1, f2, f3, f4, f5, f6, f7, f8, f9, f10;
	
	public TestSvgFigureFactory()
	{
		SvgFactoryBuilder<Lang,Description,Graphic,GraphicType,GraphicComponent,GraphicShape> ffSvg = SvgFactoryBuilder.factory(Lang.class,Description.class,Graphic.class,GraphicType.class,GraphicComponent.class,GraphicShape.class);
		efFigure = ffSvg.efFigure();
		efStyle = ffSvg.style();
		svgF = SvgFigureFactory.factory();

		init();
	}
	
	public void init()
	{
		styleCircle = efStyle.build(JeeslGraphicShape.Code.shapeCircle); styleCircle.setId(1);
		styleSquare = efStyle.build(JeeslGraphicShape.Code.shapeSquare); styleSquare.setId(2);
		styleTriangle = efStyle.build(JeeslGraphicShape.Code.shapeTriangle); styleTriangle.setId(3);
		
		f1 	 = efFigure.build(    null,   styleCircle,	 true, 	15, 	colorGreen, 0, 0,   0);		f1.setId(1);
		f2   = efFigure.build( 	  null,   styleCircle,	 false, 14, 	colorBlue, -5, 0,   0);		f2.setId(2);
		f3	 = efFigure.build(    null,  styleCircle,	 false, 13, 	colorGreen, 5, 0,   0);		f3.setId(3);
		f4 	 = efFigure.build(    null, styleSquare,	 false, 12, 	colorPink, 	1, 0,  15);		f4.setId(4);
		f5	 = efFigure.build(    null,  styleSquare,	 false, 11, 	colorWhite, 2, 0,  30);		f5.setId(5);
		f6	 = efFigure.build(    null,  styleSquare,	 false, 10, 	colorBlack, 3, 0,  45);		f6.setId(6);
		f7	 = efFigure.build(    null,  styleSquare,	 false,  9, 	colorPink, 	4, 0,  60);		f7.setId(7);
		f8	 = efFigure.build(    null,   styleSquare, 	 false,  8, 	colorBlue, 	5, 0,  75);		f8.setId(8);
		f9	 = efFigure.build(    null, styleTriangle,	 false,  7,		colorRed,	6, 0,  90);		f9.setId(9);
		f10  = efFigure.build(    null, styleTriangle,	 false,  6, 	colorGreen,	7, 0, 105);	  f10.setId(10);
	}
	
	@Test public void dummy() {}
	
	public void testF1()
	{
		logger.info(f1.toString());
		logger.info(f1.getShape().toString());
	}
	
	public void testA() throws IOException, TranscoderException
	{
		List<GraphicComponent> list = Arrays.asList(f1,f4,f5,f6,f7,f8);
		SVGGraphics2D g = svgF.build(list, 20);
		
		File f = new File(fTarget,"a.svg");
		Svg2SvgTranscoder.transcode(g,f);
	}
	
	public void testB() throws IOException, TranscoderException
	{
		List<GraphicComponent> list = Arrays.asList(f1,f2,f3);
		SVGGraphics2D g = svgF.build(list,20);
		
		File f = new File(fTarget,"b.svg");
		Svg2SvgTranscoder.transcode(g,f);
	}
	
	public void testC() throws IOException, TranscoderException
	{
		List<GraphicComponent> list = Arrays.asList(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10);
		SVGGraphics2D g = svgF.build(list,20);
		
		File f = new File(fTarget,"c.svg");
		Svg2SvgTranscoder.transcode(g,f);
	}
		
	public static void main(String[] args) throws TranscoderException, IOException, ParserConfigurationException
	{
		AbstractJeeslTest.initTargetDirectory();
		JeeslBootstrap.init();
		TestSvgFigureFactory cli = new TestSvgFigureFactory();
		
		cli.testF1();
		cli.testA();
		cli.testB();
		cli.testC();
	}
}