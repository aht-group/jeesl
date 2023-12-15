package net.sf.ahtutils.factory.xml.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Status;
import net.sf.ahtutils.xml.status.SubPhase;

public class XmlSubPhaseFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlSubPhaseFactory.class);
		
	private String localeCode;
	private SubPhase q;
	
	public XmlSubPhaseFactory(SubPhase q){this(null,q);}
	public XmlSubPhaseFactory(String localeCode,SubPhase q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> SubPhase build(S ejb){return build(ejb,null);}
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> SubPhase build(S ejb, String group)
	{
		SubPhase xml = new SubPhase();
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
		
		return xml;
	}
	
	public static SubPhase id(){return id(0);}
	private static SubPhase id(long id)
	{
		SubPhase xml = new SubPhase();
		xml.setId(id);
		return xml;
	}
	
	public static SubPhase build(String code)
	{
		SubPhase xml = new SubPhase();
		xml.setCode(code);
		return xml;
	}
	
	public static SubPhase buildLabel(String code, String label)
	{
		SubPhase xml = new SubPhase();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static SubPhase build(Status status)
	{
		SubPhase xml = new SubPhase();
		xml.setCode(status.getCode());
		xml.setDescriptions(status.getDescriptions());
		xml.setLangs(status.getLangs());
		return xml;
	}
	
	public static List<Long> toIds(List<SubPhase> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(SubPhase phase : list)
		{
			if(phase.isSetId()){result.add(phase.getId());}
		}
		return result;
	}
}