package net.sf.ahtutils.factory.xml.status;

import net.sf.ahtutils.model.interfaces.status.UtilsDescription;
import net.sf.ahtutils.xml.status.Description;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlDescriptionFactory<D extends UtilsDescription>
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
		if(q.isSetValue()){xml.setValue(ejb.getLang());}
		if(q.isSetKey()){xml.setKey(ejb.getLkey());}
		return xml;
	}
	
	public static Description create(String key, String value)
	{
		Description xml =build(value);
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