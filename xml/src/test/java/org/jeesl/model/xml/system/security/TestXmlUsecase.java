package org.jeesl.model.xml.system.security;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlUsecase extends AbstractXmlSecurityTest<Usecase>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlUsecase.class);
	
	public TestXmlUsecase(){super(Usecase.class);}
	public static Usecase create(boolean withChildren){return (new TestXmlUsecase()).build(withChildren);}
    
    public Usecase build(boolean withChilds)
    {
    	Usecase xml = new Usecase();
    	xml.setId(123);
    	xml.setPosition(1);
    	xml.setVisible(true);
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));

    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlUsecase test = new TestXmlUsecase();
		test.saveReferenceXml();
    }
}