package org.jeesl.factory.xml.io.locale.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.MainType;
import org.jeesl.model.xml.io.locale.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlMainTypeFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlMainTypeFactory.class);
		
	private String localeCode;
	private MainType q;
	
	public XmlMainTypeFactory(MainType q){this(null,q);}
	public XmlMainTypeFactory(String localeCode, MainType q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> MainType build(S ejb){return build(ejb,null);}
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> MainType build(S ejb, String group)
	{
		MainType xml = new MainType();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		
		if(ejb==null) {logger.info("ejb==null)");}
		
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
	
	public static MainType id()
	{
		MainType xml = new MainType();
		xml.setId(0l);
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
	
	public static MainType transform(Status status)
	{
		MainType type = new MainType();
		type.setCode(status.getCode());
		type.setDescriptions(status.getDescriptions());
		type.setLangs(status.getLangs());
		return type;
	}
	
	public static List<Long> toIds(List<MainType> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(MainType type : list)
		{
			if(Objects.nonNull(type.getId())) {result.add(type.getId());}
		}
		return result;
	}
}