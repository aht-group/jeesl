package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Realm;

public class XmlRealmFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlRealmFactory.class);
		
	private String localeCode;
	private Realm q;
	
	public XmlRealmFactory(Realm q){this(null,q);}
	public XmlRealmFactory(String localeCode, Realm q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Realm build(S ejb){return build(ejb,null);}
	public Realm build(S ejb, String group)
	{
		Realm xml = new Realm();
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
	
	public static <E extends Enum<E>> Realm build(E code){return build(code.toString());}
	public static <E extends Enum<E>> Realm build(String code){return build(code.toString(),null);}
	public static Realm build(String code, String label)
	{
		Realm xml = new Realm();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}