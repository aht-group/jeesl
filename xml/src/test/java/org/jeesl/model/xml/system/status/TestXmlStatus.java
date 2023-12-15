package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.system.symbol.TestXmlGraphic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlStatus extends AbstractXmlStatusTest<Status>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlStatus.class);
	
	public TestXmlStatus(){super(Status.class);}
	public static Status create(boolean withChildren){return (new TestXmlStatus()).build(withChildren);} 
    
    public Status build(boolean withChilds)
    {
    	Status xml = new Status();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setSymbol("mySymbol");
    	xml.setImage("test/green.png");
    	xml.setStyle("myStyle");
    	xml.setPosition(1);
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.getLang().add(TestXmlLang.create(false));
    		xml.setTransistions(TestXmlTransistions.create(false));
    		xml.setParent(TestXmlParent.create(false));
    		xml.setGraphic(TestXmlGraphic.create(false));
    		
    		xml.getTracked().add(TestXmlTracked.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlStatus test = new TestXmlStatus();
		test.saveReferenceXml();
    }
}