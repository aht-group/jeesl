package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.ColumnGroup;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlColumnGroup extends AbstractXmlReportTest<ColumnGroup>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlColumnGroup.class);
	
	public TestXmlColumnGroup(){super(ColumnGroup.class);}
	public static ColumnGroup create(boolean withChildren){return (new TestXmlColumnGroup()).build(withChildren);}   
    
    public ColumnGroup build(boolean withChildren)
    {
    	ColumnGroup xml = new ColumnGroup();
    	xml.setCode("myCode");
    	xml.setPosition(1);
    	xml.setVisible(true);
    	xml.setShowLabel(true);
    	xml.setShowWeb(true);
    	
    	if(withChildren)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setLayout(TestXmlLayout.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlColumnGroup test = new TestXmlColumnGroup();
		test.saveReferenceXml();
    }
}