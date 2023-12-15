package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Stage;

public class TestXmlStage extends AbstractXmlStatusTest<Stage>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlStage.class);
	
	public TestXmlStage(){super(Stage.class);}
	public static Stage create(boolean withChildren){return (new TestXmlStage()).build(withChildren);} 
    
    public Stage build(boolean withChilds)
    {
    	Stage xml = new Stage();
    	xml.setId(123l);
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
    		xml.getLang().add(TestXmlLang.create(false));
    		xml.setTransistions(TestXmlTransistions.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlStage test = new TestXmlStage();
		test.saveReferenceXml();
    }
}