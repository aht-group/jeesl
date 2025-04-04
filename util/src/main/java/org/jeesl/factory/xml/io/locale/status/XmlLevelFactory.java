package org.jeesl.factory.xml.io.locale.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLevelFactory <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlLevelFactory.class);
		
	private String localeCode;
	private Level q;
	
	public XmlLevelFactory(String localeCode,Level q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Level build(S ejb){return build(ejb,null);}
	public Level build(S ejb, String group)
	{
		Level xml = new Level();
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
	
	public static Level build()
	{
		return new Level();
	}
	
	public static Level build(String code)
	{
		Level xml = new Level();
		xml.setCode(code);
		return xml;
	}
	
	public static Level build(String code, String label)
	{
		Level xml = build(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Level build(int position, String label)
	{
		Level xml = build();
		xml.setPosition(position);
		xml.setLabel(label);
		return xml;
	}
}