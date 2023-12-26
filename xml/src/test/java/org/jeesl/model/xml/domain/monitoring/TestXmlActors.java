package org.jeesl.model.xml.domain.monitoring;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.monitoring.Actors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlActors extends AbstractXmlMonitoringTest<Actors>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlActors.class);
	
	public TestXmlActors(){super(Actors.class);}
	public static Actors create(boolean withChildren){return (new TestXmlActors()).build(withChildren);} 
    
    public Actors build(boolean withChilds)
    {
    	Actors xml = new Actors();
    	
    	if(withChilds)
    	{
    		xml.getActor().add(TestXmlActor.create(false));
    		xml.getActor().add(TestXmlActor.create(false));
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