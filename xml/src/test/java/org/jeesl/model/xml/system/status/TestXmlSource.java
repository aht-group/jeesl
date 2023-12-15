package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSource extends AbstractXmlStatusTest<Source>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSource.class);
	
	public TestXmlSource(){super(Source.class);}
	public static Source create(boolean withChildren){return (new TestXmlSource()).build(withChildren);} 
    
    public Source build(boolean withChilds)
    {
    	Source xml = new Source();
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setImage("test/green.png");
    	xml.setPosition(1);
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSource test = new TestXmlSource();
		test.saveReferenceXml();
    }
}