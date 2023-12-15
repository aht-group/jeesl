package org.jeesl.factory.xml.system.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Reason;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.io.locale.status.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlReasonFactory<S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlReasonFactory.class);
		
	private String localeCode;
	private Reason q;
	
//	public XmlResonFactory(Query q){this(q.getLang(),q.getType());}
	public XmlReasonFactory(Reason q){this(null,q);}
	public XmlReasonFactory(String localeCode, Reason q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Reason build(S ejb){return build(ejb,null);}
	public Reason build(S ejb, String group)
	{
		Reason xml = new Reason();
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
	
	public static Reason build(String code,String label){return create(null,code,label);}
	
	public static <E extends Enum<E>> Reason build(E code){return create(code.toString());}
	public static Reason create(String code){return create(null,code);}
	public static Reason create(String key, String code){return create(key,code,null);}
	public static Reason create(String key, String code, String label)
	{
		Reason xml = new Reason();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Reason id()
	{
		Reason xml = new Reason();
		xml.setId(0l);
		return xml;
	}
	
	public static Reason build(Status status)
	{
		Reason type = new Reason();
		type.setCode(status.getCode());
		type.setDescriptions(status.getDescriptions());
		type.setLangs(status.getLangs());
		return type;
	}
	
	public static Reason buildLabel(String code, String label)
	{
		Reason type = new Reason();
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