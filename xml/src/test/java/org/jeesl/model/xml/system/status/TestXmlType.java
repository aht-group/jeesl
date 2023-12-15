package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Type;

public class TestXmlType extends AbstractXmlStatusTest<Type>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlType.class);
	
	public TestXmlType(){super(Type.class);}
	public static Type create(boolean withChildren){return (new TestXmlType()).build(withChildren);}   
    
    public Type build(boolean withChilds)
    {
    	Type xml = new Type();
    	xml.setId(123l);
    	xml.setKey("myKey");
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setImage("test/green.png");
    	xml.setPosition(1);
    	
    	if(withChilds)
    	{
    		xml.setParent(TestXmlParent.create(false));
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.getLang().add(TestXmlLang.create(false));
    		xml.setTransistions(TestXmlTransistions.create(false));
    		xml.getSubType().add(TestXmlSubType.create(false));xml.getSubType().add(TestXmlSubType.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlType test = new TestXmlType();
		test.saveReferenceXml();
    }
}