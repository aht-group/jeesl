package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.DataType;
import org.jeesl.model.xml.io.locale.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlDataTypeFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlDataTypeFactory.class);
		
	private String localeCode;
	private DataType q;
	
//	public XmlDataTypeFactory(SubType q){this(null,q);}
	public XmlDataTypeFactory(String localeCode, DataType q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public DataType build(S ejb){return build(ejb,null);}
	public DataType build(S ejb, String group)
	{
		DataType xml = new DataType();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(Objects.nonNull(q.getLangs()))
		{
			XmlLangsFactory<L> f = new XmlLangsFactory<L>(q.getLangs());
			xml.setLangs(f.getUtilsLangs(ejb.getName()));
		}
		if(Objects.nonNull(q.getDescriptions()))
		{

		}
		
		if(ObjectUtils.allNotNull(q.getLabel(),localeCode))
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
	
	public static DataType id()
	{
		DataType xml = new DataType();
		xml.setId(0l);
		return xml;
	}
	
	public static DataType build(String code)
	{
		DataType xml = new DataType();
		xml.setCode(code);
		return xml;
	}
	
	public static DataType buildLabel(String code, String label)
	{
		DataType xml = new DataType();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static DataType transform(Status status)
	{
		DataType type = new DataType();
		type.setCode(status.getCode());
		type.setDescriptions(status.getDescriptions());
		type.setLangs(status.getLangs());
//		if(status.isSetParent()){type.setParent(status.getParent());}
		return type;
	}
}