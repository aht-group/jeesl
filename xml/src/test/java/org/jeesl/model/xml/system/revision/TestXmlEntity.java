package org.jeesl.model.xml.system.revision;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.model.xml.system.status.TestXmlCategory;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.jeesl.model.xml.text.TestXmlRemark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlEntity extends AbstractXmlRevisionTest<Entity>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlEntity.class);
	
	public TestXmlEntity(){super(Entity.class);}
	public static Entity create(boolean withChildren){return (new TestXmlEntity()).build(withChildren);} 
    
    public Entity build(boolean withChilds)
    {
    	Entity xml = new Entity();
    	xml.setId(123);
    	xml.setCode("myCode");
    	xml.setPosition(2);
    	xml.setVisible(true);
    	
    	if(withChilds)
    	{
    		xml.setCategory(TestXmlCategory.create(false));
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setRemark(TestXmlRemark.create(false));
    		xml.getAttribute().add(TestXmlAttribute.create(false));
    		xml.getAttribute().add(TestXmlAttribute.create(false));
    	}
    	    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlEntity test = new TestXmlEntity();
		test.saveReferenceXml();
    }
}