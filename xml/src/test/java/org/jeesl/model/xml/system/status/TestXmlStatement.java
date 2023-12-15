package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlStatement extends AbstractXmlStatusTest<Statement>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlStatement.class);
	
	public TestXmlStatement(){super(Statement.class);}
	public static Statement create(boolean withChildren){return (new TestXmlStatement()).build(withChildren);} 
    
    public Statement build(boolean withChilds)
    {
    	Statement xml = new Statement();
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
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlStatement test = new TestXmlStatement();
		test.saveReferenceXml();
    }
}