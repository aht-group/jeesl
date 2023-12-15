package net.sf.ahtutils.factory.xml.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlActionFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlActionFactory.class);
		
	private String localeCode;
	private Action q;
	
	public XmlActionFactory(String localeCode, Action q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Action build(S ejb){return build(ejb,null);}
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Action build(S ejb, String group)
	{
		Action xml = new Action();
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
	
	public static Action build(String code)
	{
		Action xml = new Action();
		xml.setCode(code);
		return xml;
	}
	
	public static Action buildLabel(String code, String label)
	{
		Action xml = new Action();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}