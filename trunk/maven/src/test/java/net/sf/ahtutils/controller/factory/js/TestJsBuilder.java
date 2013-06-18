package net.sf.ahtutils.controller.factory.js;

import java.io.File;
import java.io.IOException;

import net.sf.ahtutils.test.AbstractUtilsMavenTst;
import net.sf.ahtutils.test.UtilsMavenTstBootstrap;

import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.EvaluatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJsBuilder extends AbstractUtilsMavenTst
{
	final static Logger logger = LoggerFactory.getLogger(TestJsBuilder.class);
	
	private JsFactory jsFactory;
			
	@Before
	public void init() throws EvaluatorException, IOException
	{	
		File src = new File("src/test/resources/data/factory/js");
		jsFactory = new JsFactory(src);
		jsFactory.write(null);
	}
	
	@Test
	public void dummy(){}
		
	public static void main(String[] args) throws Exception
    {
		UtilsMavenTstBootstrap.init();
		
		TestJsBuilder test = new TestJsBuilder();
		test.init();
    }
}