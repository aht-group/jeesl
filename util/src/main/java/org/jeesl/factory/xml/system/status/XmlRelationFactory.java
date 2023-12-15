package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Relation;

public class XmlRelationFactory <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlRelationFactory.class);
	
	private final String localeCode;
	private final Relation q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	
	
//	public XmlRelationFactory(Query query){this(query.getLang(),query.getModel());}
	public XmlRelationFactory(String localeCode, Relation q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(q.isSetLangs()) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(q.isSetDescriptions()){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public Relation build(S ejb)
	{
		Relation xml = new Relation();

		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
		if(q.isSetLangs()) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(q.isSetDescriptions()){xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}
		
		if(q.isSetLabel() && localeCode!=null){xml.setLabel(XmlLangFactory.label(localeCode,ejb));}
		
		return xml;
	}
	
	public static Relation build(String code)
	{
		Relation xml = new Relation();
		xml.setCode(code);
		return xml;
	}
	
	public static Relation build(String code, String label)
	{
		Relation xml = build(code);
		xml.setLabel(label);
		return xml;
	}
}