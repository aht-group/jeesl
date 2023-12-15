package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlLangs extends AbstractXmlStatusTest<Langs>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlLangs.class);
	
	public TestXmlLangs(){super(Langs.class);}
	public static Langs create(boolean withChildren){return (new TestXmlLangs()).build(withChildren);} 
    
    public Langs build(boolean withChilds)
    {
    	Langs xml = new Langs();
    	
    	if(withChilds)
    	{
    		xml.getLang().add(TestXmlLang.create(false));
    	}
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlLangs test = new TestXmlLangs();
		test.saveReferenceXml();
    }
}