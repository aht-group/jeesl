package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlFile extends AbstractXmlReportTest<File>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlFile.class);
	
	public TestXmlFile(){super(File.class);}
	public static File create(boolean withChildren){return (new TestXmlFile()).build(withChildren);}    
    
    public File build(boolean withChildren)
    {
    	File xml = new File();
    	xml.setValue("myFileName");
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlFile test = new TestXmlFile();
		test.saveReferenceXml();
    }
}