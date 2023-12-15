package net.sf.ahtutils.factory.xml.system;

import java.util.Date;

import net.sf.ahtutils.xml.system.Uptime;
import net.sf.exlp.util.DateUtil;

import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.model.xml.io.locale.status.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUptimeFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlUptimeFactory.class);
		
	public static Uptime build(String type, Date since)
	{
		Type xmlType = XmlTypeFactory.create(type);
		return build(xmlType, since);
	}
	
	public static Uptime build(Type type, Date since)
	{
		Uptime xml = new Uptime();
		xml.setType(type);
		xml.setSince(DateUtil.toXmlGc(since));
		return xml;
	}
}