package org.jeesl.model.xml.domain.monitoring;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.monitoring.Actor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlActor extends AbstractXmlMonitoringTest<Actor>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlActor.class);
	
	public TestXmlActor(){super(Actor.class);}
	public static Actor create(boolean withChildren){return (new TestXmlActor()).build(withChildren);}
    
    public Actor build(boolean withChilds)
    {
    	Actor xml = new Actor();
    	xml.setId(123);
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	xml.setUri("myUri");
    	
    	if(withChilds)
    	{

    	}
    	
    	return xml;
    }

	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlActor test = new TestXmlActor();
		test.saveReferenceXml();
    }
}