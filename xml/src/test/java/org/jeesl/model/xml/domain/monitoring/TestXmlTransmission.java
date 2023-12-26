package org.jeesl.model.xml.domain.monitoring;

import org.exlp.model.xml.identity.User;
import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.monitoring.Transmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTransmission extends AbstractXmlMonitoringTest<Transmission>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTransmission.class);
	
	public TestXmlTransmission(){super(Transmission.class);}
	public static Transmission create(boolean withChildren){return (new TestXmlTransmission()).build(withChildren);}
    
    public Transmission build(boolean withChilds)
    {
    	Transmission xml = new Transmission();
    	
    	if(withChilds)
    	{
    		xml.setUser(new User());
    		xml.getDataSet().add(TestXmlDataSet.create(false));xml.getDataSet().add(TestXmlDataSet.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlTransmission test = new TestXmlTransmission();
		test.saveReferenceXml();
    }
}