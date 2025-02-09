package org.jeesl.factory.xml.io.locale.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithSymbol;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.jeesl.model.xml.io.locale.status.Location;
import org.jeesl.model.xml.io.locale.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLocationFactory<L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlLocationFactory.class);
	
	private final String localeCode;
	private final Location q;
		
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	
	public XmlLocationFactory(String localeCode, Location q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(Objects.nonNull(q.getLangs())) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public List<Location> build(List<S> list)
	{
		List<Location> xml = new ArrayList<Location>();
		for(S s : list){xml.add(build(s));}
		return xml;
	}
	
	public Location build(S ejb) {return build(ejb,null);}
	public Location build(S ejb, String group)
	{
		Location xml = new Location();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}		
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		if(Objects.nonNull(q.getSymbol()) && (ejb instanceof EjbWithSymbol)){xml.setSymbol(ejb.getSymbol());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(ejb.isVisible());}
		
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}		
		if(ObjectUtils.allNotNull(q.getLabel(),localeCode)){xml.setLabel(XmlLangFactory.label(localeCode,ejb));}
		
//		if(ObjectUtils.allNotNull(q.getParent(),ejb.getParent()))
//		{
//			Parent parent = new Parent();
//			parent.setCode(ejb.getParent().getCode());
//			xml.setParent(parent);
//		}
		
		return xml;
	}
	
	public static <E extends Enum<E>> Location build(E code)
	{
		return create(code.toString());
	}
	
	public static Location create(String code)
	{
		Location xml = new Location();
		xml.setCode(code);
		return xml;
	}
	
	public static Location build(String code, Langs langs)
	{
		Location xml = create(code);
		xml.setLangs(langs);
		return xml;
	}
	
	public static Location buildLabel(String code, String label)
	{
		Location xml = new Location();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Location id(){return id(0);}
	public static Location id(long id)
	{
		Location xml = new Location();
		xml.setId(id);
		return xml;
	}
	
	public static List<Long> toIds(List<Status> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(Status type : list)
		{
			if(Objects.nonNull(type.getId())) {result.add(type.getId());}
		}
		return result;
	}
}