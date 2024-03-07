package org.jeesl.factory.xml.io.mail;

import org.jeesl.interfaces.model.io.mail.link.JeeslLink;
import org.jeesl.model.xml.io.mail.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLinkFactory<LI extends JeeslLink<?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlLinkFactory.class);
	
	public static <LI extends JeeslLink<?,?,?>> XmlLinkFactory<LI> instance()
	{
		return new XmlLinkFactory<>();
	}
	
	public Link build(LI ejb, String url)
	{
		Link xml = new Link();
		xml.setCode(ejb.getCode());
		xml.setUrl(url);
		return xml;
	}
}