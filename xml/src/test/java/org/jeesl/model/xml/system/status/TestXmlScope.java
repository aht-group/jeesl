package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlScope extends AbstractXmlStatusTest<Scope>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlScope.class);
	
	public TestXmlScope(){super(Scope.class);}
	public static Scope create(boolean withChildren){return (new TestXmlScope()).build(withChildren);} 
    
    public Scope build(boolean withChilds)
    {
    	Scope xml = new Scope();
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
    		xml.getLang().add(TestXmlLang.create(false));
    		xml.setTransistions(TestXmlTransistions.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlScope test = new TestXmlScope();
		test.saveReferenceXml();
    }
}