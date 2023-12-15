package net.sf.ahtutils.factory.xml.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Priority;
import net.sf.ahtutils.xml.status.Status;

public class XmlPriorityFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlPriorityFactory.class);
		
	private final String localeCode;
	private final Priority q;
	
	public XmlPriorityFactory(Priority q){this(null,q);}
	public XmlPriorityFactory(String localeCode,Priority q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Priority build(S ejb){return build(ejb,null);}
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Priority build(S ejb, String group)
	{
		Priority xml = new Priority();
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
	
	public static Priority build(String code){return build(code,null);}
	public static Priority build(String code,String label)
	{
		Priority xml = new Priority();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Priority build(Status status)
	{
		Priority type = new Priority();
		type.setCode(status.getCode());
		type.setDescriptions(status.getDescriptions());
		type.setLangs(status.getLangs());
		return type;
	}
}