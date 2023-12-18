package org.jeesl.model.xml.system.revision;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.cms.text.TestXmlRemark;
import org.jeesl.model.xml.io.label.Attribute;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.jeesl.model.xml.system.status.TestXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlAttribute extends AbstractXmlRevisionTest<Attribute>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlAttribute.class);
	
	public TestXmlAttribute(){super(Attribute.class);}
	public static Attribute create(boolean withChildren){return (new TestXmlAttribute()).build(withChildren);} 
    
    public Attribute build(boolean withChilds)
    {
    	Attribute xml = new Attribute();
    	xml.setId(123l);
    	xml.setPosition(2);
    	xml.setCode("myCode");	
    	xml.setXpath("myXPath");

    	xml.setWeb(true);
    	xml.setPrint(true);
    	xml.setName(true);
    	xml.setEnclosure(true);
    	xml.setUi(true);
    	xml.setBean(true);
    	xml.setConstruction(true);
    	
    	if(withChilds)
    	{
    		xml.setType(TestXmlType.create(false));
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setRemark(TestXmlRemark.create(false));
    	}
    	    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlAttribute test = new TestXmlAttribute();
		test.saveReferenceXml();
    }
}