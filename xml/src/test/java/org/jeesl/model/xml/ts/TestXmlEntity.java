package org.jeesl.model.xml.ts;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.ts.Entity;

public class TestXmlEntity extends AbstractXmlTimeseriesTest<Entity>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlEntity.class);
	
	public TestXmlEntity(){super(Entity.class);}
	public static Entity create(boolean withChildren){return (new TestXmlEntity()).build(withChildren);}
    
    public Entity build(boolean withChilds)
    {
    	Entity xml = new Entity();
    	xml.setId(123);
    	xml.setCode("myCode");
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlEntity test = new TestXmlEntity();
		test.saveReferenceXml();
    }
}