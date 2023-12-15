package org.jeesl.model.xml.jeesl;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlContainer extends AbstractXmlJeeslTest<org.jeesl.model.xml.xsd.Container>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlContainer.class);
	
	public TestXmlContainer(){super(org.jeesl.model.xml.xsd.Container.class);}
	public static org.jeesl.model.xml.xsd.Container create(boolean withChildren){return (new TestXmlContainer()).build(withChildren);}
    
    public org.jeesl.model.xml.xsd.Container build(boolean withChilds)
    {
    	org.jeesl.model.xml.xsd.Container xml = new org.jeesl.model.xml.xsd.Container();
        	
    	if(withChilds)
    	{
    		xml.getStatus().add(TestXmlStatus.create(false));xml.getStatus().add(TestXmlStatus.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlContainer test = new TestXmlContainer();
		test.saveReferenceXml();
    }
}