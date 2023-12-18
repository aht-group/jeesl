package org.jeesl.model.xml.io.cms.text;

import org.jeesl.AbstractXmlTest;
import org.jeesl.model.xml.system.core.AbstractXmlSystemTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractXmlTextOldTest <T extends Object> extends AbstractXmlTest<T>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractXmlSystemTest.class);
	
	public AbstractXmlTextOldTest(Class<T> cXml)
	{
		super(cXml,"text");
	}
}