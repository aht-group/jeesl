package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Category;

public class TestXmlCategory extends AbstractXmlStatusTest<Category>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCategory.class);
	
	public TestXmlCategory(){super(Category.class);}
	public static Category create(boolean withChildren){return (new TestXmlCategory()).build(withChildren);}   
    
    public Category build(boolean withChilds)
    {
    	Category xml = new Category();
    	xml.setId(123l);
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
		TestXmlCategory test = new TestXmlCategory();
		test.saveReferenceXml();
    }
}