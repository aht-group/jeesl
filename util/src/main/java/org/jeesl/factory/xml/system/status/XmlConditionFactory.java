package org.jeesl.factory.xml.system.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Condition;
import net.sf.ahtutils.xml.status.Outcome;
import net.sf.ahtutils.xml.status.Status;

public class XmlConditionFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlConditionFactory.class);
	
	private static boolean errorPrinted = false;
	
	private String localeCode;
	private Condition q;
	
	public XmlConditionFactory(Condition q){this(null,q);}
	public XmlConditionFactory(String localeCode,Condition q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Condition build(S ejb){return build(ejb,null);}
	public Condition build(S ejb, String group)
	{
		Condition xml = new Condition();
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
	
	public static <E extends Enum<E>> Condition build(E code){return create(code.toString());}
	public static Condition create(String code)
	{
		Condition xml = new Condition();
		xml.setCode(code);
		return xml;
	}
	
	public static Condition label(String code, String label)
	{
		Condition xml = new Condition();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Condition id()
	{
		Condition xml = new Condition();
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