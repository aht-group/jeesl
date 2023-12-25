package org.jeesl.model.xml.module.survey;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.cms.text.TestXmlRemark;
import org.jeesl.model.xml.system.status.TestXmlCategory;
import org.jeesl.model.xml.system.status.TestXmlDescription;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTemplate extends AbstractXmlSurveyTest<Template>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlTemplate.class);
	
	public TestXmlTemplate(){super(Template.class);}
	public static Template create(boolean withChildren){return (new TestXmlTemplate()).build(withChildren);}   

    public Template build(boolean withChilds)
    {
    	Template xml = new Template();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	
    	if(withChilds)
    	{
    		xml.setCategory(TestXmlCategory.create(false));
    		xml.setStatus(TestXmlStatus.create(false));
    		
    		xml.setRemark(TestXmlRemark.create(false));
    		xml.setDescription(TestXmlDescription.create(false));
    		
    		xml.getSection().add(TestXmlSection.create(false));xml.getSection().add(TestXmlSection.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlTemplate test = new TestXmlTemplate();
		test.saveReferenceXml();
    }
}