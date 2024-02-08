package org.jeesl.factory.xml.system.status;

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
import org.jeesl.model.xml.io.locale.status.Parent;
import org.jeesl.model.xml.io.locale.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Query;

public class XmlStatusFactory<L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlStatusFactory.class);
	
	private final String localeCode;
	private final Status q;
		
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	
	public XmlStatusFactory(Query query) {this(query.getLang(),query.getStatus());}
	public XmlStatusFactory(Status q){this(null,q);}
	public XmlStatusFactory(String localeCode, Status q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(Objects.nonNull(q.getLangs())) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public List<Status> build(List<S> list)
	{
		List<Status> xml = new ArrayList<Status>();
		for(S s : list){xml.add(build(s));}
		return xml;
	}
	
	public Status build(S ejb){return build(ejb,null);}
	
	public Status build(S ejb, String group)
	{
		Status xml = new Status();
		xml.setGroup(group);
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}		
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		
		if(Objects.nonNull(q.getId())) {xml.setPosition(ejb.getPosition());}
		if(Objects.nonNull(q.getStyle())) {xml.setStyle(ejb.getStyle());}
		if(Objects.nonNull(q.getImage())) {xml.setImage(ejb.getImage());}
		if(Objects.nonNull(q.getSymbol()) && (ejb instanceof EjbWithSymbol)){xml.setSymbol(ejb.getSymbol());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(ejb.isVisible());}
		
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}		
		if(ObjectUtils.allNotNull(q.getLabel(),localeCode)){xml.setLabel(XmlLangFactory.label(localeCode,ejb));}
		
		if(ObjectUtils.allNotNull(q.getParent(),ejb.getParent()))
		{
			Parent parent = new Parent();
			parent.setCode(ejb.getParent().getCode());
			xml.setParent(parent);
		}
		
		return xml;
	}
	
	public static <E extends Enum<E>> Status build(E code)
	{
		return create(code.toString());
	}
	
	public static Status create(String code)
	{
		Status xml = new Status();
		xml.setCode(code);
		return xml;
	}
	
	public static Status build(String code, Langs langs)
	{
		Status xml = create(code);
		xml.setLangs(langs);
		return xml;
	}
	
	public static Status buildLabel(String code, String label)
	{
		Status xml = new Status();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Status id(){return id(0);}
	public static Status id(long id)
	{
		Status xml = new Status();
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