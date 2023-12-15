package org.jeesl.factory.xml.system.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Reason;
import net.sf.ahtutils.xml.status.Status;
import net.sf.ahtutils.xml.status.Type;

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
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(q.getDescriptions());
			xml.setDescriptions(f.create(ejb.getDescription()));
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
		xml.setId(0);
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
			if(type.isSetId()){result.add(type.getId());}
		}
		return result;
	}
}