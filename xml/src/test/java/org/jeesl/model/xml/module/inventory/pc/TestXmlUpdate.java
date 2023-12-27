package org.jeesl.model.xml.module.inventory.pc;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlUpdate extends AbstractXmlInventoryPcTest<Update>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlUpdate.class);
	
	public TestXmlUpdate(){super(Update.class);}
	public static Update create(boolean withChildren){return (new TestXmlUpdate()).build(withChildren);}

    public Update build(boolean withChilds)
    { 
    	Update xml = new Update();
    	xml.setId(123l);
    	xml.setCode("KB3365213");
    	xml.setDescription("myDescription");
    	xml.setVersion("myVersion");
    	xml.setUuid("myUuid");
    	
    	xml.setRecord(getDefaultXmlDate());
    	
    	if(withChilds)
    	{
    		
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlUpdate test = new TestXmlUpdate();
		test.saveReferenceXml();
    }
}