package org.jeesl.model.xml.domain.monitoring;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.monitoring.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlComponent extends AbstractXmlMonitoringTest<Component>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlComponent.class);
	
	public TestXmlComponent(){super(Component.class);}
	public static Component create(boolean withChildren){return (new TestXmlComponent()).build(withChildren);}
    
    public Component build(boolean withChilds)
    {
    	Component xml = new Component();
    	xml.setId(123);
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	
    	if(withChilds)
    	{
    		xml.getIndicator().add(TestXmlIndicator.create(false));xml.getIndicator().add(TestXmlIndicator.create(false));
    	}
    	
    	return xml;
    }
    
    public void save() {save(create(true),fXml);}
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlComponent test = new TestXmlComponent();
		test.saveReferenceXml();
    }
}