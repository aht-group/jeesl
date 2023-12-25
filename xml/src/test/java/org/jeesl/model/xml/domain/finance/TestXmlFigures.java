package org.jeesl.model.xml.domain.finance;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.cms.text.TestXmlRemark;
import org.jeesl.model.xml.module.finance.Figures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlFigures extends AbstractXmlFinanceTest<Figures>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlFigures.class);
	
	public TestXmlFigures(){super(Figures.class);}
	public static Figures create(boolean withChildren){return (new TestXmlFigures()).build(withChildren);}
    
    public Figures build(boolean withChilds)
    {
    	Figures xml = new Figures();
    	xml.setId(123);
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	
    	if(withChilds)
    	{
    		xml.getFinance().add(TestXmlFinance.create(false));xml.getFinance().add(TestXmlFinance.create(false));
    		xml.getTime().add(TestXmlTime.create(false));xml.getTime().add(TestXmlTime.create(false));
    		xml.getRemark().add(TestXmlRemark.create(false));xml.getRemark().add(TestXmlRemark.create(false));
    		xml.getCounter().add(TestXmlCounter.create(false));xml.getCounter().add(TestXmlCounter.create(false));
    		
    		xml.getFigures().add(TestXmlFigures.create(false));xml.getFigures().add(TestXmlFigures.create(false));
    	}
    	    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlFigures test = new TestXmlFigures();
		test.saveReferenceXml();
    }
}