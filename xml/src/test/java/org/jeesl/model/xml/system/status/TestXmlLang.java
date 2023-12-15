package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlLang extends AbstractXmlStatusTest<Lang>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlLang.class);
	
	public TestXmlLang(){super(Lang.class);}
	public static Lang create(boolean withChildren){return (new TestXmlLang()).build(withChildren);}
	public static Lang create(boolean withChildren, String key, String translation){return (new TestXmlLang()).build(withChildren,key,translation);}
    
    public Lang build(boolean withChilds){return build(withChilds,"myKey","myTranslation");}
    public Lang build(boolean withChilds, String key, String translation)
    {
    	Lang xml = new Lang();
    	xml.setVersion(1);
    	xml.setKey(key);
    	xml.setTranslation(translation);
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlLang test = new TestXmlLang();
		test.saveReferenceXml();
    }
}