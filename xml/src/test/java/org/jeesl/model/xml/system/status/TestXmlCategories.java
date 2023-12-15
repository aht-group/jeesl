package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Categories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlCategories extends AbstractXmlStatusTest<Categories>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCategories.class);
	
	public TestXmlCategories(){super(Categories.class);}
	public static Categories create(boolean withChildren){return (new TestXmlCategories()).build(withChildren);}   
    
    public Categories build(boolean withChilds)
    {
    	Categories xml = new Categories();
    	xml.setGroup("myGroup");
    	xml.setSize(3);
    	
    	if(withChilds)
    	{
    		xml.getCategory().add(TestXmlCategory.create(false));
    		xml.getCategory().add(TestXmlCategory.create(false));
    	}
    	return xml;
    }

	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlCategories test = new TestXmlCategories();
		test.saveReferenceXml();
    }
}