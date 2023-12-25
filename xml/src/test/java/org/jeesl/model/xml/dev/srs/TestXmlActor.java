package org.jeesl.model.xml.dev.srs;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlActor extends AbstractXmlSrsTest<Actor>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlActor.class);
	
	public TestXmlActor(){super(Actor.class);}
	public static Actor create(boolean withChildren){return (new TestXmlActor()).build(withChildren);}
    
    public Actor build(boolean withChildren)
    {
    	Actor xml = new Actor();
    	xml.setCode("myCode");
    	xml.setName("myName");
    	if(withChildren)
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