package net.sf.ahtutils.factory.xml.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Organisation;
import org.jeesl.model.xml.io.locale.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlOrganisationFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlOrganisationFactory.class);
	
	private String localeCode;
	private Organisation q;
	
	public XmlOrganisationFactory(String localeCode, Organisation q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Organisation build(S ejb){return build(ejb,null);}
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Organisation build(S ejb, String group)
	{
		Organisation xml = new Organisation();
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
	
	public static Organisation build(String code)
	{
		Organisation xml = new Organisation();
		xml.setCode(code);
		return xml;
	}
	
	public static Organisation build(String code,String label)
	{
		Organisation xml = new Organisation();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Organisation build(Status status)
	{
		Organisation xml = new Organisation();
		xml.setCode(status.getCode());
		xml.setDescriptions(status.getDescriptions());
		xml.setLangs(status.getLangs());
		return xml;
	}
}