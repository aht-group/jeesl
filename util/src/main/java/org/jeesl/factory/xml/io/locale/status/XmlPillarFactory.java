package org.jeesl.factory.xml.io.locale.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Pillar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlPillarFactory<S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlPillarFactory.class);
		
	private String localeCode;
	private Pillar q;
	
//	public XmlSectorFactory(Query q){this(q.getLang(),q.getType());}
	public XmlPillarFactory(Pillar q){this(null,q);}
	public XmlPillarFactory(String localeCode, Pillar q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Pillar build(S ejb){return build(ejb,null);}
	public Pillar build(S ejb, String group)
	{
		Pillar xml = new Pillar();
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
	
	public static <E extends Enum<E>> Pillar code(E code){return code(code.toString());}
	public static Pillar code(String code){return build(null,null,code);}
	public static Pillar label(String code,String label){return build(null,code,label);}
	
	public static Pillar build(String key, String code, String label)
	{
		Pillar xml = new Pillar();
		xml.setKey(key);
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}