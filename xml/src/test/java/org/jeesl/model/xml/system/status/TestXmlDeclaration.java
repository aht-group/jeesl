package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Declaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlDeclaration extends AbstractXmlStatusTest<Declaration>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlDeclaration.class);
	
	public TestXmlDeclaration(){super(Declaration.class);}
	public static Declaration create(boolean withChildren){return (new TestXmlDeclaration()).build(withChildren);} 
    
    public Declaration build(boolean withChilds)
    {
    	Declaration xml = new Declaration();
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setImage("test/green.png");
    	xml.setPosition(1);
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		
    		xml.getTracked().add(TestXmlTracked.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlDeclaration test = new TestXmlDeclaration();
		test.saveReferenceXml();
    }
}