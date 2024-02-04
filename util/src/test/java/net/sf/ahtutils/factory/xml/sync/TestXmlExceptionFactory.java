package net.sf.ahtutils.factory.xml.sync;

import net.sf.ahtutils.test.AbstractFileProcessingTest;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.xml.system.io.sync.XmlExceptionFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlExceptionFactory extends AbstractFileProcessingTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlExceptionFactory.class);
	
	private JeeslNotFoundException eSingle;
	private JeeslConstraintViolationException eMulti;
	private JeeslConstraintViolationException eIntegrity;
	
	@Before
	public void init()
	{
		eSingle = new JeeslNotFoundException();
		
		eIntegrity = new JeeslConstraintViolationException("TestIntegriy");
		eMulti = new JeeslConstraintViolationException("TestMulti",eIntegrity);
	}
	
	@Test
	public void single()
	{
		org.jeesl.model.xml.io.ssi.sync.Exception xml = XmlExceptionFactory.build(eSingle);
		JaxbUtil.info(xml);
	}
	
	@Test
	public void multi()
	{
		org.jeesl.model.xml.io.ssi.sync.Exception xml = XmlExceptionFactory.build(eMulti);
		JaxbUtil.info(xml);
	}
}