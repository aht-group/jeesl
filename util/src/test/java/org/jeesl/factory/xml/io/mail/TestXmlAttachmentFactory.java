package org.jeesl.factory.xml.io.mail;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.io.mail.Attachment;
import org.jeesl.test.JeeslBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

public class TestXmlAttachmentFactory
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlAttachmentFactory.class);
	
	Path pMime;
	
	public TestXmlAttachmentFactory(Configuration config)
	{
		pMime = Paths.get(config.getString("dir.mime"));
		logger.info("Mime Examples: "+pMime.toString());
	}
	
	protected void pdf() throws IOException, MagicParseException, MagicMatchNotFoundException, MagicException
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