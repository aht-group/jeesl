package org.jeesl.model.xml.system.io.template;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.template.Tokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTokens extends AbstractXmlTemplateTest<Tokens>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTokens.class);
	
	public TestXmlTokens(){super(Tokens.class);}
	public static Tokens create(boolean withChildren){return (new TestXmlTokens()).build(withChildren);} 
    
    public Tokens build(boolean withChilds)
    {
    	Tokens xml = new Tokens();
    	
    	if(withChilds)
    	{
    		xml.getToken().add(TestXmlToken.create(false));
    		xml.getToken().add(TestXmlToken.create(false));
    	}	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlTokens test = new TestXmlTokens();
		test.saveReferenceXml();
    }
}