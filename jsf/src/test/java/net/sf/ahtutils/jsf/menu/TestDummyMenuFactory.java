package net.sf.ahtutils.jsf.menu;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.system.navigation.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractAhtUtilsJsfTst;

public class TestDummyMenuFactory extends AbstractAhtUtilsJsfTst
{
	final static Logger logger = LoggerFactory.getLogger(TestDummyMenuFactory.class);
	
	private Menu menu;
	
	@BeforeEach
	public void init()
	{
		menu = DummyMenuFactory.create();
	}
	
	@Test
	public void testInit()
	{
		JaxbUtil.warn(menu);
	}
}