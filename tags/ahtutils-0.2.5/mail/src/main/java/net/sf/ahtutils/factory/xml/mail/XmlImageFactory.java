package net.sf.ahtutils.factory.xml.mail;

import net.sf.ahtutils.xml.mail.Image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlImageFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlEmailAddressFactory.class);
	
	public static Image build(String cid, String resource)
	{
		Image image = new Image();
    	image.setCid(cid);
		return image;
	}
}