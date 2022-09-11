package org.jeesl.model.xml.system.security;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlUsecases extends AbstractXmlSecurityTest<Usecases>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlUsecases.class);
	
	public TestXmlUsecases(){super(Usecases.class);}
	public static Usecases create(boolean withChildren){return (new TestXmlUsecases()).build(withChildren);}
    
    public Usecases build(boolean withChilds)
    {
    	Usecases xml = new Usecases();
    	
    	if(withChilds)
    	{
    		xml.getUsecase().add(TestXmlUsecase.create(false));
    		xml.getUsecase().add(TestXmlUsecase.create(false));
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlUsecases test = new TestXmlUsecases();
		test.saveReferenceXml();
    }
}