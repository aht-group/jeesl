package org.jeesl.factory.xml.system.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Outcome;
import net.sf.ahtutils.xml.status.Status;

public class XmlOutcomeFactory<S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlOutcomeFactory.class);
	
	private static boolean errorPrinted = false;
	
	private String localeCode;
	private Outcome q;
	
	public XmlOutcomeFactory(Outcome q){this(null,q);}
	public XmlOutcomeFactory(String localeCode,Outcome q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Outcome build(S ejb){return build(ejb,null);}
	public Outcome build(S ejb, String group)
	{
		Outcome xml = new Outcome();
		if(q.isSetId()){xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
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
		else if(q.isSetLabel() && localeCode==null)
		{
			logger.warn("Should render label, but localeCode is null");
			if(!errorPrinted)
			{
				logger.warn("This StackTrace will only shown once!");
				for (StackTraceElement ste : Thread.currentThread().getStackTrace())
				{
				    System.err.println(ste);
				}
				errorPrinted=true;
			}
		}
		
		return xml;
	}
	
	public static <E extends Enum<E>> Outcome build(E code){return create(code.toString());}
	public static Outcome create(String code)
	{
		Outcome xml = new Outcome();
		xml.setCode(code);
		return xml;
	}
	
	public static Outcome label(String code, String label)
	{
		Outcome xml = new Outcome();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Outcome id()
	{
		Outcome xml = new Outcome();
		xml.setId(0);
		return xml;
	}
	
	public static List<Long> toIds(List<Outcome> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(Outcome c : list)
		{
			if(c.isSetId()){result.add(c.getId());}
		}
		return result;
	}
	
	public static Outcome build(Status status)
	{
		Outcome type = new Outcome();
		type.setCode(status.getCode());
		type.setDescriptions(status.getDescriptions());
		type.setLangs(status.getLangs());
		return type;
	}
}