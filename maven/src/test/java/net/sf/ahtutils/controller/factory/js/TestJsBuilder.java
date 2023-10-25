package net.sf.ahtutils.controller.factory.js;

import java.io.File;
import java.io.IOException;

import org.jeesl.JeeslMavenBootstrap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mozilla.javascript.EvaluatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractUtilsMavenTst;

public class TestJsBuilder extends AbstractUtilsMavenTst
{
	final static Logger logger = LoggerFactory.getLogger(TestJsBuilder.class);
	
	private JsFactory jsFactory;
			
	@BeforeEach
	public void init() throws EvaluatorException, IOException
	{	
		File src = new File("src/test/resources/data/factory/js");
		String[] libOrder = new String[0];
//		jsFactory = new JsFactory(src, libOrder);
//		jsFactory.write(null);
	}
	
	@Test public void dummy(){}
		
	public static void main(String[] args) throws Exception
    {
		JeeslMavenBootstrap.init();
		
		TestJsBuilder test = new TestJsBuilder();
		test.init();
    }
}