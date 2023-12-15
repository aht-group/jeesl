package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Query;
import net.sf.ahtutils.xml.status.Model;
import net.sf.ahtutils.xml.status.Parent;

public class XmlModelFactory <L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlModelFactory.class);
	
	private final String localeCode;
	private final Model q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	
	public XmlModelFactory(Query query){this(query.getLang(),query.getModel());}
	public XmlModelFactory(String localeCode, Model q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(Objects.nonNull(q.getLangs())) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public Model build(S ejb)
	{
		Model xml = new Model();

		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}
		
		if(ObjectUtils.allNotNull(q.getLabel(),localeCode)){xml.setLabel(XmlLangFactory.label(localeCode,ejb));}
		
		if(ObjectUtils.allNotNull( q.getParent(),ejb.getParent()))
		{
			Parent parent = new Parent();
			parent.setCode(ejb.getParent().getCode());
			xml.setParent(parent);
		}
		
		return xml;
	}
	
	public static Model build(String code)
	{
		Model xml = new Model();
		xml.setCode(code);
		return xml;
	}
	
	public static Model label(String code, String label)
	{
		Model xml = new Model();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}