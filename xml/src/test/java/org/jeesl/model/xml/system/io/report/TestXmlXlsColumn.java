package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.XlsColumn;
import org.jeesl.model.xml.system.status.TestXmlDataType;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlXlsColumn extends AbstractXmlReportTest<XlsColumn>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlXlsColumn.class);
	
	public TestXmlXlsColumn(){super(XlsColumn.class);}
	public static XlsColumn create(boolean withChildren){return (new TestXmlXlsColumn()).build(withChildren);}   
    
    public XlsColumn build(boolean withChildren)
    {
    	XlsColumn xml = new XlsColumn();
    	xml.setCode("myCode");
    	xml.setPosition(1);
    	xml.setVisible(true);
    	xml.setShowLabel(true);
    	xml.setShowWeb(true);
    	
    	xml.setColumn("A");
    	xml.setLabel("myLabel");
    	xml.setRequired(true);
    	xml.setExample("myExample");
    	xml.setAutoWidth(false);
    	xml.setWidth(2);
    	
    	if(withChildren)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setDataType(TestXmlDataType.create(false));
    		xml.setQueries(TestXmlQueries.create(false));
    		xml.setLayout(TestXmlLayout.create(false));
    		xml.setStyles(TestXmlStyles.create(false));
    		xml.setXlsTransformation(TestXmlXlsTransformation.create(false));
    		xml.setDataAssociation(TestXmlDataAssociation.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlXlsColumn test = new TestXmlXlsColumn();
		test.saveReferenceXml();
    }
}