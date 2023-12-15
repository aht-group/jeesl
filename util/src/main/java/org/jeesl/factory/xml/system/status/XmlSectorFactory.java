package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Sector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSectorFactory<S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlSectorFactory.class);
		
	private String localeCode;
	private Sector q;
	
//	public XmlSectorFactory(Query q){this(q.getLang(),q.getType());}
	public XmlSectorFactory(Sector q){this(null,q);}
	public XmlSectorFactory(String localeCode, Sector q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Sector build(S ejb){return build(ejb,null);}
	public Sector build(S ejb, String group)
	{
		Sector xml = new Sector();
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
	
	public static <E extends Enum<E>> Sector code(E code){return code(code.toString());}
	public static Sector code(String code){return build(null,null,code);}
	public static Sector label(String code,String label){return build(null,code,label);}
	
	public static Sector build(String key, String code, String label)
	{
		Sector xml = new Sector();
		xml.setKey(key);
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}