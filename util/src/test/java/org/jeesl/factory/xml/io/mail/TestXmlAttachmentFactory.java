package org.jeesl.factory.xml.io.mail;

import java.io.IOException;
import java.nio.file.Path;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.io.mail.Attachment;
import org.jeesl.test.JeeslBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlAttachmentFactory
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlAttachmentFactory.class);
	
	Path pMime;
	
	public TestXmlAttachmentFactory(Configuration config)
	{
		pMime = JeeslBootstrap.pTemp;
		logger.info("Mime Examples: "+pMime.toString());
	}
	
	protected void pdf() throws IOException
	{
		Path pPdf = pMime.resolve("pdf.pdf");
		logger.info("PDF: "+pPdf.toString());
		
		Attachment xml = XmlAttachmentFactory.create(pPdf.toFile());
		xml.setData(null);
		JaxbUtil.info(xml);
	}
	
	public static void main(String[] args) throws Exception
	{
		Configuration config = JeeslBootstrap.init();
		TestXmlAttachmentFactory cli = new TestXmlAttachmentFactory(config);
		cli.pdf();
	}
}