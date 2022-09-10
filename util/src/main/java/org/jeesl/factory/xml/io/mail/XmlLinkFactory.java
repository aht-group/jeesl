package org.jeesl.factory.xml.io.mail;

import org.jeesl.interfaces.model.io.mail.JeeslMailLink;
import org.jeesl.model.xml.system.io.mail.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLinkFactory<LI extends JeeslMailLink<?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlLinkFactory.class);
	
	public static <LI extends JeeslMailLink<?,?,?>> XmlLinkFactory<LI> instance()
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