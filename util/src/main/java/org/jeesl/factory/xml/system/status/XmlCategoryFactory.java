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

import net.sf.ahtutils.xml.status.Category;
import net.sf.ahtutils.xml.status.Status;

public class XmlCategoryFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlCategoryFactory.class);
	
	private static boolean errorPrinted = false;
	
	private String localeCode;
	private Category q;
	
	public XmlCategoryFactory(Category q){this(null,q);}
	public XmlCategoryFactory(String localeCode, Category q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Category build(S ejb){return build(ejb,null);}
	public Category build(S ejb, String group)
	{
		Category xml = new Category();
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
	
	public static <E extends Enum<E>> Category build(E code){return create(code.toString());}
	public static Category create(String code)
	{
		Category xml = new Category();
		xml.setCode(code);
		return xml;
	}
	
	public static Category label(String code, String label)
	{
		Category xml = new Category();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Category id()
	{
		Category xml = new Category();
		xml.setId(0);
		return xml;
	}
	
	public static List<Long> toIds(List<Category> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(Category c : list)
		{
			if(c.isSetId()){result.add(c.getId());}
		}
		return result;
	}
	
	public static Category build(Status status)
	{
		Category type = new Category();
		type.setCode(status.getCode());
		type.setDescriptions(status.getDescriptions());
		type.setLangs(status.getLangs());
		return type;
	}
}