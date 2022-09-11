package org.jeesl.model.xml.navigation;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.navigation.MenuItem;
import org.jeesl.model.xml.system.security.TestXmlView;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlMenuItem extends AbstractXmlNavigationTest<MenuItem>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlMenuItem.class);
	
	public TestXmlMenuItem(){super(MenuItem.class);}
	public static MenuItem create(boolean withChildren){return (new TestXmlMenuItem()).build(withChildren);}
    
    public MenuItem build(boolean withChilds)
    {
    	MenuItem xml = new MenuItem();
      	xml.setActive(true);
      	xml.setCode("myCode");
      	xml.setName("myName");
      	xml.setHref("myHref");
    	
    	if(withChilds)
    	{
    		xml.getMenuItem().add(TestXmlMenuItem.create(false));
    		xml.getMenuItem().add(TestXmlMenuItem.create(false));
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setView(TestXmlView.create(false));
    	}
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlMenuItem test = new TestXmlMenuItem();
		test.saveReferenceXml();
    }
}