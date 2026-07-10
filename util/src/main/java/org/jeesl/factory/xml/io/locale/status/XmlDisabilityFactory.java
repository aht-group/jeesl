package org.jeesl.factory.xml.io.locale.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Disability;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.io.locale.status.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Query;

public class XmlDisabilityFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlDisabilityFactory.class);
		
	private String localeCode;
	private Status q;
	
	public XmlDisabilityFactory(Status q) {this(null,q);}
	public XmlDisabilityFactory(String localeCode, Status q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Disability build(S ejb){return build(ejb,null);}
	public Disability build(S ejb, String group)
	{
		Disability xml = new Disability();
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
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(q.getDescriptions());
			xml.setDescriptions(f.create(ejb.getDescription()));
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
	
	public static Disability build(String code,String label){return create(null,code,label);}
	
	public static <E extends Enum<E>> Disability build(E code){return create(code.toString());}
	public static Disability create(String code){return create(null,code);}
	public static Disability create(String key, String code){return create(key,code,null);}
	public static Disability create(String key, String code, String label)
	{
		Disability xml = new Disability();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Disability id()
	{
		Disability xml = new Disability();
		xml.setId(0l);
		return xml;
	}
	
	
	public static Disability buildLabel(String code, String label)
	{
		Disability type = new Disability();
		type.setCode(code);
		type.setLabel(label);
		return type;
	}
	
	public static List<Long> toIds(List<Type> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(Type type : list)
		{
			if(Objects.nonNull(type.getId())) {result.add(type.getId());}
		}
		return result;
	}
}