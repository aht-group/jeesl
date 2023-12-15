package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.io.locale.XmlLabelFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlPrecisionFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlPrecisionFactory.class);
	
	private String localeCode;
	private Precision q;
	
	private XmlLabelFactory<L> xfLabel;
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	
	public XmlPrecisionFactory(Precision q){this(null,q);}
	public XmlPrecisionFactory(String localeCode, Precision q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(Objects.nonNull(q.getLabel())) {xfLabel = new XmlLabelFactory<>(localeCode);}
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public Precision build(S ejb){return build(ejb,null);}
	public Precision build(S ejb, String group)
	{
		Precision xml = new Precision();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(Objects.nonNull(q.getLabel())) {xml.setLabel(xfLabel.build(ejb));}
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescription.create(ejb.getDescription()));}
		
		return xml;
	}
	
	public static <E extends Enum<E>> Precision build(E code){return create(code.toString());}
	public static Precision create(String code)
	{
		Precision xml = new Precision();
		xml.setCode(code);
		return xml;
	}
	
	public static Precision label(String code, String label)
	{
		Precision xml = new Precision();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}