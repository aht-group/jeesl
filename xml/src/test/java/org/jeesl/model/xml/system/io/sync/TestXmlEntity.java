package org.jeesl.model.xml.system.io.sync;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.ssi.sync.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlEntity extends AbstractXmlSyncTest<Entity>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlEntity.class);
	
	public TestXmlEntity(){super(Entity.class);}
	public static Entity create(boolean withChildren){return (new TestXmlEntity()).build(withChildren);}
    
    public Entity build(boolean withChilds)
    {
    	Entity xml = new Entity();
    	xml.setVersion(0l);
    	xml.setType("myType");
    	xml.setValue("123");
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlEntity test = new TestXmlEntity();
		test.saveReferenceXml();
    }
}