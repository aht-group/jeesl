package org.jeesl.factory.xml.system.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.io.locale.XmlLabelFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
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
	
	private XmlLabelFactory<L> xfLabel;
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	
	public XmlCategoryFactory(Category q){this(null,q);}
	public XmlCategoryFactory(String localeCode, Category q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getLabel())) {xfLabel = new XmlLabelFactory<>(localeCode);}
		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public Category build(S ejb){return build(ejb,null);}
	public Category build(S ejb, String group)
	{
		Category xml = new Category();
		if(q.isSetId()){xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescription.create(ejb.getDescription()));}
		if(Objects.nonNull(q.getLabel())) {xml.setLabel(xfLabel.build(ejb));}
		
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