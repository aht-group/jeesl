package org.jeesl.model.xml.system.security;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTemplates extends AbstractXmlSecurityTest<Templates>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTemplates.class);
	
	public TestXmlTemplates(){super(Templates.class);}
	public static Templates create(boolean withChildren){return (new TestXmlTemplates()).build(withChildren);}
    
    public Templates build(boolean withChilds)
    {
    	Templates xml = new Templates();
    	
    	if(withChilds)
    	{
    		xml.getTemplate().add(TestXmlTemplate.create(false));
    		xml.getTemplate().add(TestXmlTemplate.create(false));
    	}
    	return xml;
    }
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlTemplates test = new TestXmlTemplates();
		test.saveReferenceXml();
    }
}