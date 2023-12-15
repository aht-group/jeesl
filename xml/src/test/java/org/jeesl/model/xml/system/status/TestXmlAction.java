package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Action;

public class TestXmlAction extends AbstractXmlStatusTest<Action>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlAction.class);
	
	public TestXmlAction(){super(Action.class);}
	public static Action create(boolean withChildren){return (new TestXmlAction()).build(withChildren);}   
   
    public Action build(boolean withChilds)
    {
    	Action xml = new Action();
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
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlAction test = new TestXmlAction();
		test.saveReferenceXml();
    }
}