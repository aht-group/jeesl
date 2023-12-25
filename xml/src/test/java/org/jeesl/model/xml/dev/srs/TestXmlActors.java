package org.jeesl.model.xml.dev.srs;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlActors extends AbstractXmlSrsTest<Actors>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlActors.class);
	
	public TestXmlActors(){super(Actors.class);}
	public static Actors create(boolean withChildren){return (new TestXmlActors()).build(withChildren);}
    
    public Actors build(boolean withChildren)
    {
    	Actors xml = new Actors();
    	xml.setModule("myModule");
    	
    	if(withChildren)
    	{
    		xml.getActors().add(TestXmlActors.create(false));xml.getActors().add(TestXmlActors.create(false));
    		xml.getActor().add(TestXmlActor.create(false));xml.getActor().add(TestXmlActor.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlActors test = new TestXmlActors();
		test.saveReferenceXml();
    }
}