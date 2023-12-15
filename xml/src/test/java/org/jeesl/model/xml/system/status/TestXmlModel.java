package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlModel extends AbstractXmlStatusTest<Model>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlModel.class);
	
	public TestXmlModel(){super(Model.class);}
	public static Model create(boolean withChildren){return (new TestXmlModel()).build(withChildren);} 
    
    public Model build(boolean withChilds)
    {
    	Model xml = new Model();
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
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlModel test = new TestXmlModel();
		test.saveReferenceXml();
    }
}