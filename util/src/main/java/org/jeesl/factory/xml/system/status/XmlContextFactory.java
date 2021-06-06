package org.jeesl.factory.xml.system.status;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Context;
import net.sf.ahtutils.xml.status.Status;

public class XmlContextFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlContextFactory.class);
		
	private String localeCode;
	private Context q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	
	public XmlContextFactory(Context q){this(null,q);}
	public XmlContextFactory(String localeCode, Context q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(q.isSetLangs()){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(q.isSetDescriptions()){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public static <E extends Enum<E>> Context build(E code){return build(code.toString());}
	public static <E extends Enum<E>> Context build(String code){return build(code.toString(),null);}
	public static Context build(String code, String label)
	{
		Context xml = new Context();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public Context build(S ejb){return build(ejb,null);}
	public Context build(S ejb, String group)
	{
		Context xml = new Context();
		if(q.isSetId()){xml.setId(ejb.getId());}
		if(q.isSetCode()){xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(q.isSetLangs()){xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(q.isSetDescriptions()){xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}
		
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
	
	public static Context build(Status status)
	{
		Context xml = new Context();
		xml.setCode(status.getCode());
		xml.setPosition(status.getPosition());
		xml.setDescriptions(status.getDescriptions());
		xml.setLangs(status.getLangs());
//		if(status.isSetParent()){xml.setParent(status.getParent());}
		return xml;
	}
}