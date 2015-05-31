package net.sf.ahtutils.factory.xml.status;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.status.Status;
import net.sf.ahtutils.xml.status.Style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlStyleFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlStyleFactory.class);
		
	private String lang;
	private Style q;
		
	public XmlStyleFactory(String lang,Style q)
	{
		this.lang=lang;
		this.q=q;
	}
	
	public <S extends UtilsStatus<S,L,D>,L extends UtilsLang, D extends UtilsDescription> Style build(S ejb){return build(ejb,null);}
	public <S extends UtilsStatus<S,L,D>,L extends UtilsLang, D extends UtilsDescription> Style build(S ejb, String group)
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