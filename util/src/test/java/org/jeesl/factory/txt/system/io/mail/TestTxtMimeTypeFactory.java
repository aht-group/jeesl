package org.jeesl.factory.txt.system.io.mail;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.factory.txt.system.io.mail.core.TxtMimeTypeFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTxtMimeTypeFactory extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestTxtMimeTypeFactory.class);
		
	@Test public void pre()
    {	
		System.out.println(TxtMimeTypeFactory.build("x.pdf"));
		System.out.println(TxtMimeTypeFactory.build("x.doc"));
		System.out.println(TxtMimeTypeFactory.build("x.docx"));
    }
}