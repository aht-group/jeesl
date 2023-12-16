package org.jeesl.model.xml.system.revision;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.label.Entities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlEntities extends AbstractXmlRevisionTest<Entities>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlEntities.class);
	
	public TestXmlEntities(){super(Entities.class);}
	public static Entities create(boolean withChildren){return (new TestXmlEntities()).build(withChildren);} 
    
    public Entities build(boolean withChilds)
    {
    	Entities xml = new Entities();
    	
    	if(withChilds)
    	{
    		xml.getEntity().add(TestXmlEntity.create(false));
    		xml.getEntity().add(TestXmlEntity.create(false));
    	}
    	    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlEntities test = new TestXmlEntities();
		test.saveReferenceXml();
    }
}