package org.jeesl.factory.xml.system.status;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusWithSymbol;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Query;
import net.sf.ahtutils.xml.status.Langs;
import net.sf.ahtutils.xml.status.Parent;
import net.sf.ahtutils.xml.status.Status;

public class XmlStatusFactory<L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlStatusFactory.class);
	
	private final String localeCode;
	private final Status q;
		
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	
	public XmlStatusFactory(Query query){this(query.getLang(),query.getStatus());}
	public XmlStatusFactory(Status q){this(null,q);}
	public XmlStatusFactory(String localeCode, Status q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(q.isSetLangs()) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(q.isSetDescriptions()){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
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
		if(q.isSetId()){xml.setId(ejb.getId());}
		if(q.isSetCode()){xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
		if(q.isSetStyle()){xml.setStyle(ejb.getStyle());}
		if(q.isSetImage()){xml.setImage(ejb.getImage());}
		if(q.isSetSymbol() && (ejb instanceof JeeslStatusWithSymbol)){xml.setSymbol(ejb.getSymbol());}
		if(q.isSetVisible()){xml.setVisible(ejb.isVisible());}
		
		if(q.isSetLangs()) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(q.isSetDescriptions()){xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}
		
		if(q.isSetLabel() && localeCode!=null){xml.setLabel(XmlLangFactory.label(localeCode,ejb));}
		
		if(q.isSetParent() && ejb.getParent()!=null)
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
			if(type.isSetId()){result.add(type.getId());}
		}
		return result;
	}
}