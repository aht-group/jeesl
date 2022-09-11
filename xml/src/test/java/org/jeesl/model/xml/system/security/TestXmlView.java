package org.jeesl.model.xml.system.security;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.navigation.TestXmlNavigation;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlView extends AbstractXmlSecurityTest<View>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlView.class);
	
	public TestXmlView(){super(View.class);}
	public static View create(boolean withChildren){return (new TestXmlView()).build(withChildren);}
    
    public View build(boolean withChilds)
    {
    	View xml = new View();
    	xml.setId(123);
    	xml.setPosition(1);
    	xml.setVisible(true);
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setAccess(TestXmlAccess.create(false));
    		xml.setNavigation(TestXmlNavigation.create(false));
    		xml.setActions(TestXmlActions.create(false));
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlView test = new TestXmlView();
		test.saveReferenceXml();
    }
}