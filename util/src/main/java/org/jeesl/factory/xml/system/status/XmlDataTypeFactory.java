package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.DataType;
import net.sf.ahtutils.xml.status.Status;

public class XmlDataTypeFactory<S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlDataTypeFactory.class);
		
	private String lang;
	private DataType q;
	
//	public XmlDataTypeFactory(SubType q){this(null,q);}
	public XmlDataTypeFactory(String lang,DataType q)
	{
		this.lang=lang;
		this.q=q;
	}
	
	public DataType build(S ejb){return build(ejb,null);}
	public DataType build(S ejb, String group)
	{
		DataType xml = new DataType();
		if(q.isSetId()){xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(q.isSetLangs())
		{
			XmlLangsFactory<L> f = new XmlLangsFactory<L>(q.getLangs());
			xml.setLangs(f.getUtilsLangs(ejb.getName()));
		}
		if(q.isSetDescriptions())
		{

		}
		
		if(q.isSetLabel() && lang!=null)
		{
			if(ejb.getName()!=null)
			{
				if(ejb.getName().containsKey(lang)){xml.setLabel(ejb.getName().get(lang).getLang());}
				else
				{
					String msg = "No translation "+lang+" available in "+ejb;
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
		xml.setId(0);
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