package org.jeesl.factory.xml.system.lang;

import net.sf.ahtutils.xml.status.Description;

import java.util.Objects;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlDescriptionFactory<D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlDescriptionFactory.class);
		
	private Description q;
	
	public XmlDescriptionFactory(Description q)
	{
		this.q=q;
	}
	
	public Description create(D ejb)
	{
		Description xml = new Description();
		if(Objects.nonNull(q.getValue())) {xml.setValue(ejb.getLang());}
		if(Objects.nonNull(q.getKey())) {xml.setKey(ejb.getLkey());}
		return xml;
	}
	
	public static Description create(String key, String value)
	{
		Description xml = build(value);
		xml.setKey(key);
		return xml;
	}
	
	public static Description build(String value)
	{
		Description xml = new Description();
		xml.setValue(value);
		return xml;
	}
}