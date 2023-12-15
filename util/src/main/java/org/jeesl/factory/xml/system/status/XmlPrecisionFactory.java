package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Precision;

public class XmlPrecisionFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlPrecisionFactory.class);
	
	private String localeCode;
	private Precision q;
	
	public XmlPrecisionFactory(Precision q){this(null,q);}
	public XmlPrecisionFactory(String localeCode, Precision q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Precision build(S ejb){return build(ejb,null);}
	public Precision build(S ejb, String group)
	{
		Precision xml = new Precision();
		if(q.isSetId()){xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
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
		else if(q.isSetLabel() && localeCode==null)
		{
			logger.warn("Should render label, but localeCode is null");
		}
		
		return xml;
	}
	
	public static <E extends Enum<E>> Precision build(E code){return create(code.toString());}
	public static Precision create(String code)
	{
		Precision xml = new Precision();
		xml.setCode(code);
		return xml;
	}
	
	public static Precision label(String code, String label)
	{
		Precision xml = new Precision();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}