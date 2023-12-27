package org.jeesl.model.xml.system.io.ssi;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.ssi.core.Systems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSystems extends AbstractXmlSsiTest<Systems>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSystems.class);
	
	public TestXmlSystems(){super(Systems.class);}
	public static Systems create(boolean withChildren){return (new TestXmlSystems()).build(withChildren);} 
       
    public Systems build(boolean withChilds)
    {
    	Systems xml = new Systems();

    	if(withChilds)
    	{
    		xml.getSystem().add(TestXmlSystem.create(false));
    		xml.getSystem().add(TestXmlSystem.create(false));
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSystems test = new TestXmlSystems();
		test.saveReferenceXml();
    }
}