package org.jeesl.factory.xml.system.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Status;
import net.sf.ahtutils.xml.status.SubType;

public class XmlSubTypeFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlSubTypeFactory.class);
		
	private String localeCode;
	private SubType q;
	
	private XmlLangsFactory<L> xfLangs;
	
	public XmlSubTypeFactory(String localeCode, SubType q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(q.isSetLangs()) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
	}
	
	public SubType build(S ejb){return build(ejb,null);}
	public SubType build(S ejb, String group)
	{
		SubType xml = new SubType();
		if(q.isSetId()){xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(q.isSetLangs()) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(q.isSetDescriptions())
		{

		}
		
		if(q.isSetLabel() && localeCode!=null)
		{
			if(ejb.getName()!=null)
			{
				if(ejb.getName().containsKey(localeCode)){xml.setLabel(ejb.getName().get(localeCode).getLang());}
				else
				{
					String msg = "No translation "+localeCode+" available in "+ejb;
					logger.warn(msg);
					xml.setLabel(msg);
				}
			}
			else
			{
				String msg = "No @name available in "+ejb;
				logger.warn(msg);
				xml.setLabel(msg);
			}
		}
		
		return xml;
	}
	
	public static SubType id()
	{
		SubType xml = new SubType();
		xml.setId(0);
		return xml;
	}
	
	public static SubType build(String code)
	{
		SubType xml = new SubType();
		xml.setCode(code);
		return xml;
	}
	
	public static SubType buildLabel(String code, String label)
	{
		SubType xml = new SubType();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static SubType transform(Status status)
	{
		SubType type = new SubType();
		type.setCode(status.getCode());
		type.setDescriptions(status.getDescriptions());
		type.setLangs(status.getLangs());
//		if(status.isSetParent()){type.setParent(status.getParent());}
		return type;
	}
	
	public static List<Long> toIds(List<SubType> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(SubType type : list)
		{
			if(type.isSetId()){result.add(type.getId());}
		}
		return result;
	}
}