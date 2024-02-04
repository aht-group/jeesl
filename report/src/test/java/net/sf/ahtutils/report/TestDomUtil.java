package net.sf.ahtutils.report;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.io.report.Info;
import org.jeesl.model.xml.io.report.Info.Title;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.sf.ahtutils.test.AbstractAhtUtilsReportTest;
import net.sf.exlp.util.xml.DomUtil;

public class TestDomUtil extends AbstractAhtUtilsReportTest
{
	final static Logger logger = LoggerFactory.getLogger(TestDomUtil.class);
    	
	private Element root;
	private Info expected;
	
	@Before
	public void init() throws IOException, ParserConfigurationException
	{
		Title title = new Title();
		title.setValue("MyTitle");
		
		expected = new Info();
		expected.setTitle(title);
		
		Document doc = JaxbUtil.toW3CDocument(expected);
		root = doc.getDocumentElement();
		
	}
	
	@Test
	public void domUtil()
	{
		Info actual = DomUtil.toJaxb(root, Info.class);
		this.assertJaxbEquals(expected, actual);
	}
}