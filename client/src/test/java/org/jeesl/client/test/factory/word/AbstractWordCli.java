package org.jeesl.client.test.factory.word;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;

public class AbstractWordCli
{
	final static Logger logger = LoggerFactory.getLogger(AbstractWordCli.class);

	protected final MultiResourceLoader mrl;
	
	public AbstractWordCli() 
	{
		mrl = MultiResourceLoader.instance();
	}
}