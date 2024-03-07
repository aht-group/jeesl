package org.jeesl.factory.txt.io.ssi.util;

import org.jeesl.JeeslBootstrap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTxtIoSsiSimilarityFactory
{
	final static Logger logger = LoggerFactory.getLogger(TestTxtIoSsiSimilarityFactory.class);

	@Test
	public void size()
	{
		Assertions.assertEquals(0, TxtIoSsiSimilarityFactory.permutations(null).size());
		Assertions.assertEquals(1, TxtIoSsiSimilarityFactory.permutations("").size());
		Assertions.assertEquals(1, TxtIoSsiSimilarityFactory.permutations("Hello").size());
		Assertions.assertEquals(2, TxtIoSsiSimilarityFactory.permutations("Hello World").size());
		Assertions.assertEquals(6, TxtIoSsiSimilarityFactory.permutations("Greeting Hello World").size());
	}

	public static void main(String[] args) throws Exception
	{
		JeeslBootstrap.init();
		TestTxtIoSsiSimilarityFactory cli = new TestTxtIoSsiSimilarityFactory();
		cli.size();
	}
}