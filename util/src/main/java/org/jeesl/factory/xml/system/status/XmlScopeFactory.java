package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.io.locale.XmlLabelFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlScopeFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlScopeFactory.class);
	
	private String localeCode;
	private Scope q;

	private XmlLabelFactory<L> xfLabel;
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	
	public XmlScopeFactory(Scope q){this(null,q);}
	public XmlScopeFactory(String localeCode, Scope q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(Objects.nonNull(q.getLabel())) {xfLabel = new XmlLabelFactory<>(localeCode);}
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public static <E extends Enum<E>> Scope build(E code) {return build(code.toString());}
	public static Scope build(String code)
	{
		Scope xml = new Scope();
		xml.setCode(code);
		return xml;
	}
	
	public Scope build(S ejb)
	{
		Scope xml = new Scope();
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		
		if(Objects.nonNull(q.getLabel())) {xml.setLabel(xfLabel.build(ejb));}
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}

		return xml;
	}
}