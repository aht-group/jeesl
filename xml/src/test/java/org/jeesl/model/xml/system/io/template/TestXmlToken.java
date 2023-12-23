package org.jeesl.model.xml.system.io.template;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.template.Token;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.jeesl.model.xml.system.util.text.TestXmlExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlToken extends AbstractXmlTemplateTest<Token>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlToken.class);
	
	public TestXmlToken(){super(Token.class);}
	public static Token create(boolean withChildren){return (new TestXmlToken()).build(withChildren);} 
    
    public Token build(boolean withChilds)
    {
    	Token xml = new Token();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	xml.setPosition(2);
    	xml.setVisible(true);
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setExample(TestXmlExample.create(false));
    	}	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlToken test = new TestXmlToken();
		test.saveReferenceXml();
    }
}