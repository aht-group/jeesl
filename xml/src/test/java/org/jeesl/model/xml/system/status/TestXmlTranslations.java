package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTranslations extends AbstractXmlStatusTest<Translations>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTranslations.class);
	
	public TestXmlTranslations(){super(Translations.class);}
	public static Translations create(boolean withChildren){return (new TestXmlTranslations()).build(withChildren);}   
    
    public Translations build(boolean withChilds)
    {
    	Translations xml = new Translations();
    	
    	if(withChilds)
    	{
    		xml.getTranslation().add(TestXmlTranslation.create(false));
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlTranslations test = new TestXmlTranslations();
		test.saveReferenceXml();
    }
}