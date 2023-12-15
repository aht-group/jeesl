package net.sf.ahtutils.factory.xml.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.io.locale.XmlLabelFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlGenderFactory <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlGenderFactory.class);

	private Gender q;
	
	private XmlLabelFactory<L> xfLabel;
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	
	public XmlGenderFactory(String localeCode, Gender q)
	{
		this.q=q;
		
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getLabel())) {xfLabel = new XmlLabelFactory<>(localeCode);}
		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public Gender build(S ejb){return build(ejb,null);}
	public Gender build(S ejb, String group)
	{
		Gender xml = new Gender();
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescription.create(ejb.getDescription()));}
		if(Objects.nonNull(q.getLabel())) {xml.setLabel(xfLabel.build(ejb));}
		return xml;
	}
	
	public static Gender female() {return build("female");}
	public static Gender male() {return build("male");}
	public static Gender build(String code){return build(code,null);}
	public static Gender build(String code,String label)
	{
		Gender xml = new Gender();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}