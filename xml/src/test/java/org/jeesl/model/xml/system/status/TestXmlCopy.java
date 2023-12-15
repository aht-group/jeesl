package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Copy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlCopy extends AbstractXmlStatusTest<Copy>
{
	final static Logger logger = LoggerFactory.getLogger(Copy.class);
	
	public TestXmlCopy(){super(Copy.class);}
	public static Copy create(boolean withChildren){return (new TestXmlCopy()).build(withChildren);} 
    
    public Copy build(boolean withChilds)
    {
    	Copy xml = new Copy();
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setImage("test/green.png");
    	xml.setPosition(1);
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlCopy test = new TestXmlCopy();
		test.saveReferenceXml();
    }
}