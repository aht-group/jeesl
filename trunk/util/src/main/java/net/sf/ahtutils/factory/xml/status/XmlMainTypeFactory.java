package net.sf.ahtutils.factory.xml.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.status.MainType;
import net.sf.ahtutils.xml.status.Status;

public class XmlMainTypeFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlMainTypeFactory.class);
		
	private String lang;
	private MainType q;
	
	public XmlMainTypeFactory(MainType q){this(null,q);}
	public XmlMainTypeFactory(String lang,MainType q)
	{
		this.lang=lang;
		this.q=q;
	}
	
	public <S extends UtilsStatus<S,L,D>,L extends UtilsLang, D extends UtilsDescription> MainType build(S ejb){return build(ejb,null);}
	public <S extends UtilsStatus<S,L,D>,L extends UtilsLang, D extends UtilsDescription> MainType build(S ejb, String group)
	{
		MainType xml = new MainType();
		if(q.isSetId()){xml.setId(ejb.getId());}
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
	
	public static MainType id()
	{
		MainType xml = new MainType();
		xml.setId(0);
		return xml;
	}
	
	public static MainType build(String code)
	{
		MainType xml = new MainType();
		xml.setCode(code);
		return xml;
	}
	
	public static MainType buildLabel(String code, String label)
	{
		MainType xml = new MainType();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static MainType build(Status status)
	{
		MainType type = new MainType();
		type.setCode(status.getCode());
		type.setDescriptions(status.getDescriptions());
		type.setLangs(status.getLangs());
		return type;
	}
}