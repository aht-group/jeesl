package net.sf.ahtutils.controller.factory.xhtml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jeesl.JeeslMavenBootstrap;
import org.jeesl.processor.JeeslXhtmlParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mozilla.javascript.EvaluatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import net.sf.ahtutils.test.AbstractUtilsMavenTst;

public class TestXhtmlValidator extends AbstractUtilsMavenTst
{
	final static Logger logger = LoggerFactory.getLogger(TestXhtmlValidator.class);

	private JeeslXhtmlParser xhtmlParser;

	@BeforeEach
	public void init() throws EvaluatorException, IOException, ParserConfigurationException, SAXException
	{
		xhtmlParser = new JeeslXhtmlParser("src/test/resources/data/factory/xhtml");
    }
	
	@Test public void dummy(){}
	
	@Test
	public void test()
	{
		xhtmlParser.parse();
	}

	public static void main(String[] args) throws Exception
    {
		JeeslMavenBootstrap.init();

		TestXhtmlValidator test = new TestXhtmlValidator();
		test.init();
    }
}