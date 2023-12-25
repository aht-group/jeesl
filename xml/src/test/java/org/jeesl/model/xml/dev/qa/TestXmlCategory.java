package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.qa.Category;

public class TestXmlCategory extends AbstractXmlQaTest<Category>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCategory.class);
	
	public TestXmlCategory(){super(Category.class);}
	public static Category create(boolean withChildren){return (new TestXmlCategory()).build(withChildren);}
    
    public Category build(boolean withChilds)
    {
    	Category xml = new Category();
    	xml.setId(123);
    	xml.setName("myName");
    	xml.setCode("myCode");
    	
    	if(withChilds)
    	{
    		xml.setQa(TestXmlQa.create(false));
    		xml.getTest().add(TestXmlTest.create(false));
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