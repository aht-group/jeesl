package net.sf.ahtutils.factory.xml.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Copy;
import net.sf.ahtutils.xml.status.Status;

public class XmlCopyFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlCopyFactory.class);
		
	private String lang;
	private Copy q;
	
	@Deprecated
	public XmlCopyFactory(Copy q)
	{
		this(null,q);
	}
	
	public XmlCopyFactory(String lang,Copy q)
	{
		this.lang=lang;
		this.q=q;
	}
	
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Copy build(S ejb){return build(ejb,null);}
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Copy build(S ejb, String group)
	{
		Copy xml = new Copy();
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
		if(q.isSetLabel() && lang!=null)
		{
			if(ejb.getName()!=null)
			{
				if(ejb.getName().containsKey(lang)){xml.setLabel(ejb.getName().get(lang).getLang());}
				else
				{
					String msg = "No translation "+lang+" available in "+ejb;
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
	
	public static Copy build(String code)
	{
		Copy xml = new Copy();
		xml.setCode(code);
		return xml;
	}
	
	public static Copy build(Status status)
	{
		Copy xml = new Copy();
		xml.setCode(status.getCode());
		xml.setDescriptions(status.getDescriptions());
		xml.setLangs(status.getLangs());
		return xml;
	}
}