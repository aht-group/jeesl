package org.jeesl.model.xml.system.security;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlActions extends AbstractXmlSecurityTest<Actions>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlActions.class);
	
	public TestXmlActions(){super(Actions.class);}
	public static Actions create(boolean withChildren){return (new TestXmlActions()).build(withChildren);}
    
    public Actions build(boolean withChilds)
    {
    	Actions xml = new Actions();
    	
    	if(withChilds)
    	{
    		xml.getAction().add(TestXmlAction.create(false));
    		xml.getAction().add(TestXmlAction.create(false));
    	}
    	return xml;
    }
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlActions test = new TestXmlActions();
		test.saveReferenceXml();
    }
}