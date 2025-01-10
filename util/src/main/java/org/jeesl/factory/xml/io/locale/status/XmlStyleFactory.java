package org.jeesl.factory.xml.io.locale.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.io.locale.status.Style;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlStyleFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlStyleFactory.class);
		
	private String localeCode;
	private Style q;
		
	public XmlStyleFactory(String localeCode, Style q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Style build(S ejb){return build(ejb,null);}
	public <E extends Enum<E>> Style build(E group, S ejb){return build(ejb,group.toString());}
	public Style build(S ejb, String group)
	{
		Style xml = new Style();
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
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(q.getDescriptions());
			xml.setDescriptions(f.create(ejb.getDescription()));
		}
		
		if(ObjectUtils.allNotNull(q.getLabel(),localeCode))
		{
			if(ejb.getName()!=null)
			{
				if(ejb.getName().containsKey(localeCode)){xml.setLabel(ejb.getName().get(localeCode).getLang());}
				else{logger.warn("No translation "+localeCode+" available in "+ejb);}
			}
			else{logger.warn("No @name available in "+ejb);}
		}
		return xml;
	}
	
	public static Style build(String code)
	{
		Style xml = new Style();
		xml.setCode(code);
		return xml;
	}
	public static Style buildLabel(String code, String label)
	{
		Style xml = new Style();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Style build(Status status)
	{
		Style xml = new Style();
		xml.setCode(status.getCode());
		xml.setDescriptions(status.getDescriptions());
		xml.setLangs(status.getLangs());
		return xml;
	}
}