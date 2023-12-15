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

import net.sf.ahtutils.xml.status.Area;
import net.sf.ahtutils.xml.status.Status;

public class XmlAreaFactory<S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlAreaFactory.class);
	
	private static boolean errorPrinted = false;
	
	private String localeCode;
	private Area q;
	
	public XmlAreaFactory(Area q){this(null,q);}
	public XmlAreaFactory(String localeCode,Area q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Area build(S ejb){return build(ejb,null);}
	public Area build(S ejb, String group)
	{
		Area xml = new Area();
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
	
	public static <E extends Enum<E>> Area build(E code){return create(code.toString());}
	public static Area create(String code)
	{
		Area xml = new Area();
		xml.setCode(code);
		return xml;
	}
	
	public static Area label(String code, String label)
	{
		Area xml = new Area();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Area id()
	{
		Area xml = new Area();
		xml.setId(0);
		return xml;
	}
	
	public static List<Long> toIds(List<Area> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(Area c : list)
		{
			if(c.isSetId()){result.add(c.getId());}
		}
		return result;
	}
	
	public static Area build(Status status)
	{
		Area type = new Area();
		type.setCode(status.getCode());
		type.setDescriptions(status.getDescriptions());
		type.setLangs(status.getLangs());
		return type;
	}
}