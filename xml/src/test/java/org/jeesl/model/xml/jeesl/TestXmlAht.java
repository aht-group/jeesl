package org.jeesl.model.xml.jeesl;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.domain.finance.TestXmlCurrency;
import org.jeesl.model.xml.system.security.TestXmlUser;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.jeesl.model.xml.xsd.aht.Aht;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlAht extends AbstractXmlAhtTest<Aht>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlAht.class);
	
	public TestXmlAht(){super(Aht.class);}
	public static Aht create(boolean withChildren){return (new TestXmlAht()).build(withChildren);}
    
    public Aht build(boolean withChilds)
    {
    	Aht xml = new Aht();
        	
    	if(withChilds)
    	{
    		xml.getStatus().add(TestXmlStatus.create(false));xml.getStatus().add(TestXmlStatus.create(false));
    		xml.getCurrency().add(TestXmlCurrency.create(false));xml.getCurrency().add(TestXmlCurrency.create(false));
    		xml.getUser().add(TestXmlUser.create(false));xml.getUser().add(TestXmlUser.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlAht test = new TestXmlAht();
		test.saveReferenceXml();
    }
}