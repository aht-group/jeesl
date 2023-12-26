package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Cell;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlCell extends AbstractXmlReportTest<Cell>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCell.class);
	
	public TestXmlCell(){super(Cell.class);}
	public static Cell create(boolean withChildren){return (new TestXmlCell()).build(withChildren);}   
    
    public Cell build(boolean withChildren)
    {
    	Cell xml = new Cell();
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	
    	xml.setRowNr(1);
    	xml.setColNr(1);
    	
    	if(withChildren)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlCell test = new TestXmlCell();
		test.saveReferenceXml();
    }
}