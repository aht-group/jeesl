package org.jeesl.model.xml.jeesl;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.jeesl.model.xml.xsd.aht.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlContainerOld extends AbstractXmlAhtTest<Container>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlContainerOld.class);
	
	public TestXmlContainerOld(){super(Container.class);}
	public static Container create(boolean withChildren){return (new TestXmlContainerOld()).build(withChildren);}
    
    public Container build(boolean withChilds)
    {
    	Container xml = new Container();
        	
    	if(withChilds)
    	{
    		xml.getStatus().add(TestXmlStatus.create(false));xml.getStatus().add(TestXmlStatus.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlContainerOld test = new TestXmlContainerOld();
		test.saveReferenceXml();
    }
}