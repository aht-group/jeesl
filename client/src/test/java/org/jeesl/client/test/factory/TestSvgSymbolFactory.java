package org.jeesl.client.test.factory;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.FileUtils;
import org.jeesl.client.JeeslBootstrap;
import org.jeesl.client.model.ejb.system.graphic.Graphic;
import org.jeesl.client.model.ejb.system.graphic.GraphicComponent;
import org.jeesl.client.model.ejb.system.graphic.GraphicStyle;
import org.jeesl.client.model.ejb.system.graphic.GraphicType;
import org.jeesl.client.model.ejb.system.locale.Description;
import org.jeesl.client.model.ejb.system.locale.Lang;
import org.jeesl.client.test.AbstractJeeslClientTest;
import org.jeesl.factory.svg.SvgSymbolFactory;
import org.junit.Test;
import org.openfuxml.media.transcode.Svg2SvgTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractJeeslTest;

public class TestSvgSymbolFactory extends AbstractJeeslClientTest
{
	final static Logger logger = LoggerFactory.getLogger(TestSvgSymbolFactory.class);

	private SvgSymbolFactory<Lang,Description,Graphic,GraphicType,GraphicComponent,GraphicStyle> svgF;
	
	public TestSvgSymbolFactory()
	{
		svgF = SvgSymbolFactory.factory();
	}
	
	public void circle() throws IOException, TranscoderException
	{
		Graphic rule = new Graphic();
		for(int i=0;i<=12;i++)
		{
			rule.setSize(i);
			SVGGraphics2D g = svgF.build(12,rule);
			byte[] bytes = Svg2SvgTranscoder.transcode(g);
			FileUtils.writeByteArrayToFile(new File(fTarget,"circle-"+i+".svg"), bytes);
		}
	}
	
    @Test public void dummy() {}
	
	public void square() throws IOException, TranscoderException
	{
		Graphic rule = new Graphic();
		for(int i=0;i<=12;i++)
		{
			rule.setSize(i);
			SVGGraphics2D g = svgF.square(12,rule); //@Lasse, only this this method, otherwise it will influence our production systems
			byte[] bytes = Svg2SvgTranscoder.transcode(g);
			FileUtils.writeByteArrayToFile(new File(fTarget,"square-"+i+".svg"), bytes);
		}
	}
	
	public static void main(String[] args) throws TranscoderException, IOException, ParserConfigurationException
	{
		JeeslBootstrap.init();
		AbstractJeeslTest.initTargetDirectory();
		
		TestSvgSymbolFactory test = new TestSvgSymbolFactory();
		AbstractJeeslTest.initTargetDirectory();
		
//		test.circle();
		test.square();
	}
}