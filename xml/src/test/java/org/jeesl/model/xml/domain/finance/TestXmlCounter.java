package org.jeesl.model.xml.domain.finance;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.finance.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlCounter extends AbstractXmlFinanceTest<Counter>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCounter.class);
	
	public TestXmlCounter(){super(Counter.class);}
	public static Counter create(boolean withChildren){return (new TestXmlCounter()).build(withChildren);}
    
    public Counter build(boolean withChilds)
    {
    	Counter xml = new Counter();
    	xml.setId(123);
    	xml.setCode("myCode");
    	xml.setLabel("myLabel");
    	xml.setCounter(2345);
    	    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlCounter test = new TestXmlCounter();
		test.saveReferenceXml();
    }
}