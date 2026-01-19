package org.jeesl.factory.xml.io.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.jeesl.model.xml.io.mail.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlAttachmentFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlEmailAddressFactory.class);
	
	private static String mimePdf = "application/pdf";
	
	public static Attachment create(File f) throws IOException
	{
		FileInputStream fis = new FileInputStream(f);
		byte[] data = IOUtils.toByteArray(fis);
		fis.close();
		
		Tika tika = new Tika();

//		String mimeType = Magic.getMagicMatch(f,false).getMimeType();
        String mimeType = tika.detect(f);
		
		
		return build(f.getName(), mimeType, data);
	}
	
	public static Attachment build(String fileName, String mime, InputStream is) throws IOException
    {
		return build(fileName,mime,IOUtils.toByteArray(is));
    }
	
    public static Attachment build(String fileName, String mime, byte[] data)
    {
		org.exlp.model.xml.io.File file = new org.exlp.model.xml.io.File();
    	file.setName(fileName);
    	file.setMime(mime);
    	
    	Attachment attachment = new Attachment();
    	attachment.setData(data);
    	attachment.setFile(file);
    	
    	return attachment;
    }
    
    public static Attachment build(String name, byte[] data)
    {
		org.exlp.model.xml.io.File file = new org.exlp.model.xml.io.File();
    	file.setName(name);
    	
    	if(name.endsWith(".pdf")) {file.setMime(XmlAttachmentFactory.mimePdf);}
    	else
    	{
    		logger.warn("No MIME");
    	}
    	
    	Attachment attachment = new Attachment();
    	attachment.setData(data);
    	attachment.setFile(file);
    	
    	return attachment;
    }
}