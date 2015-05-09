package net.sf.ahtutils.factory.xml.status;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.status.Capability;
import net.sf.ahtutils.xml.status.Copy;
import net.sf.ahtutils.xml.status.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlCapabilityFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlCapabilityFactory.class);
		
	private String lang;
	private Copy q;
		
	public XmlCapabilityFactory(String lang,Copy q)
	{
		this.lang=lang;
		this.q=q;
	}
	
	public <S extends UtilsStatus<S,L,D>,L extends UtilsLang, D extends UtilsDescription> Capability build(S ejb){return build(ejb,null);}
	public <S extends UtilsStatus<S,L,D>,L extends UtilsLang, D extends UtilsDescription> Capability build(S ejb, String group)
	{
		Capability xml = new Capability();
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

		}
		if(q.isSetLabel() && lang!=null)
		{
			if(ejb.getName()!=null)
			{
				if(ejb.getName().containsKey(lang)){xml.setLabel(ejb.getName().get(lang).getLang());}
				else{logger.warn("No translation "+lang+" available in "+ejb);}
			}
			else{logger.warn("No @name available in "+ejb);}
		}
		return xml;
	}
	
	public static Capability build(String code)
	{
		Capability xml = new Capability();
		xml.setCode(code);
		return xml;
	}
	
	public static Capability build(Status status)
	{
		Capability xml = new Capability();
		xml.setCode(status.getCode());
		xml.setDescriptions(status.getDescriptions());
		xml.setLangs(status.getLangs());
		return xml;
	}
}