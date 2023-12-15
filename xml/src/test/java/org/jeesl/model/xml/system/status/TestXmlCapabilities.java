package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Capabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlCapabilities extends AbstractXmlStatusTest<Capabilities>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCapabilities.class);
	
	public TestXmlCapabilities(){super(Capabilities.class);}
	public static Capabilities create(boolean withChildren){return (new TestXmlCapabilities()).build(withChildren);}   
    
    public Capabilities build(boolean withChilds)
    {
    	Capabilities xml = new Capabilities();
    	    	
    	if(withChilds)
    	{
    		xml.getCapability().add(TestXmlCapability.create(false));
    		xml.getCapability().add(TestXmlCapability.create(false));
    	}
    	
    	return xml;
    }
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlCapabilities test = new TestXmlCapabilities();
		test.saveReferenceXml();
    }
}