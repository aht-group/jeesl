package org.jeesl.controller.rewrite;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRewriteProvider 
{
	final static Logger logger = LoggerFactory.getLogger(TestRewriteProvider.class);
		
	@BeforeAll
    public static void initLogger()
	{
		
    }

	@Test
	public void testUrlMapping()
	{
		Assertions.assertEquals("/data", AbstractRewriteProvider.getUrlMapping("ww", "http://xx.yyy.zzz:8080/ww/data"));
		Assertions.assertEquals("/data", AbstractRewriteProvider.getUrlMapping("xx", "http://xx.yyy.zzz:8080/xx/data?xxxx"));
		Assertions.assertEquals("/data", AbstractRewriteProvider.getUrlMapping("xx", "http://xx.yyy.zzz:8080/xx/data"));
	}
}