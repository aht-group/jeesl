package org.jeesl.factory.xml.system.status;

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

import net.sf.ahtutils.xml.status.Phase;
import net.sf.ahtutils.xml.status.Status;

public class XmlPhaseFactory<L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlPhaseFactory.class);
		
	private String localeCode;
	private Phase q;
	
	private XmlLabelFactory<L> xfLabel;
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	
	public XmlPhaseFactory(String localeCode, Phase q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getLabel())) {xfLabel = new XmlLabelFactory<>(localeCode);}
		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public Phase build(S ejb){return build(ejb,null);}
	public Phase build(S ejb, String group)
	{
		Phase xml = new Phase();
		if(ejb!=null)
		{
			if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
			if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
			xml.setGroup(group);
			
			if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
			if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescription.create(ejb.getDescription()));}
			if(Objects.nonNull(q.getLabel())) {xml.setLabel(xfLabel.build(ejb));}
		}
		
		return xml;
	}
	
	public static Phase build(String code)
	{
		Phase xml = new Phase();
		xml.setCode(code);
		return xml;
	}
	
	public static Phase buildLabel(String code, String label)
	{
		Phase xml = new Phase();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Phase build(Status status)
	{
		Phase xml = new Phase();
		xml.setCode(status.getCode());
		xml.setDescriptions(status.getDescriptions());
		xml.setLangs(status.getLangs());
		return xml;
	}
}