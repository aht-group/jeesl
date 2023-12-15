package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Responsible;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlResponsible extends AbstractXmlStatusTest<Responsible>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlResponsible.class);
	
	public TestXmlResponsible(){super(Responsible.class);}
	public static Responsible create(boolean withChildren){return (new TestXmlResponsible()).build(withChildren);} 
    
    public Responsible build(boolean withChilds)
    {
    	Responsible xml = new Responsible();
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
		TestXmlResponsible test = new TestXmlResponsible();
		test.saveReferenceXml();
    }
}