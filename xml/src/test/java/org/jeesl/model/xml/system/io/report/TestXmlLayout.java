package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Layout;
import org.jeesl.model.xml.system.symbol.TestXmlColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlLayout extends AbstractXmlReportTest<Layout>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlLayout.class);
	
	public TestXmlLayout(){super(Layout.class);}
	public static Layout create(boolean withChildren){return (new TestXmlLayout()).build(withChildren);}   
    
    public Layout build(boolean withChildren)
    {
    	Layout xml = new Layout();
    	
    	if(withChildren)
    	{
    		xml.setOffset(TestXmlOffset.create(false));
    		xml.getSize().add(TestXmlSize.create(false));xml.getSize().add(TestXmlSize.create(false));
    		xml.getColor().add(TestXmlColor.create(false));xml.getColor().add(TestXmlColor.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlLayout test = new TestXmlLayout();
		test.saveReferenceXml();
    }
}