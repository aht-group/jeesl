package org.jeesl.factory.xml.system.status;

import net.sf.ahtutils.xml.status.Status;
import net.sf.ahtutils.xml.status.Style;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlStyleFactory<S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
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
		if(q.isSetCode()){xml.setCode(ejb.getCode());}
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
	
	public static Style build(Status status)
	{
		Style xml = new Style();
		xml.setCode(status.getCode());
		xml.setDescriptions(status.getDescriptions());
		xml.setLangs(status.getLangs());
		return xml;
	}
}