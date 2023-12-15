package org.jeesl.model.xml.system.security;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlRole extends AbstractXmlSecurityTest<Role>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRole.class);
	
	public TestXmlRole(){super(Role.class);}
	public static Role create(boolean withChildren){return (new TestXmlRole()).build(withChildren);}
    
    public Role build(boolean withChilds)
    {
    	Role xml = new Role();
    	xml.setId(123l);
    	xml.setPosition(1);
    	xml.setVisible(true);
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setActions(TestXmlActions.create(false));
    		xml.setViews(TestXmlViews.create(false));
    		xml.setUsecases(TestXmlUsecases.create(false));
    		xml.setStaffs(TestXmlStaffs.create(false));
    	}
    	return xml;
    }
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlRole test = new TestXmlRole();
		test.saveReferenceXml();
    }
}