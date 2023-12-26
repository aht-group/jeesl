package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractAhtUtilsXmlTest;

public class TestXmlInfo extends AbstractXmlReportTest<Info>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlInfo.class);
	
	public TestXmlInfo(){super(Info.class);}
	public static Info create(boolean withChildren){return (new TestXmlInfo()).build(withChildren);}    
    
    public Info build(boolean withChildren)
    {
    	Info xml = new Info();
    	Info.Title title = new Info.Title();
    	title.setValue("testTitle");
    	xml.setTitle(title);
    	Info.Subtitle subtitle = new Info.Subtitle();
    	subtitle.setValue("testSubTitle");
    	xml.setSubtitle(subtitle);
    	Info.Footer footer = new Info.Footer();
    	footer.setValue("testFooter");
    	xml.setFooter(footer);
    	Info.Record record = new Info.Record();
    	record.setValue(AbstractAhtUtilsXmlTest.getDefaultXmlDate());
    	xml.setRecord(record);
    	
    	xml.setHash(TestXmlHash.create(false));
    	xml.setUser(TestXmlUser.create(false));
    	
    	if(withChildren)
    	{
    		xml.getJr().add(TestXmlJr.create(false));xml.getJr().add(TestXmlJr.create(false));
    		xml.setLabels(TestXmlLabels.create(false));
    	}
    	
    	xml.setFile(TestXmlFile.create(false));
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlInfo test = new TestXmlInfo();
		test.saveReferenceXml();
    }
}