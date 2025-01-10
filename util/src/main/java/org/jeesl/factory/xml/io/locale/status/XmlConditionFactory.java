package org.jeesl.factory.xml.io.locale.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.factory.xml.system.io.locale.XmlLabelFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Condition;
import org.jeesl.model.xml.io.locale.status.Outcome;
import org.jeesl.model.xml.io.locale.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlConditionFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlConditionFactory.class);
	
	private Condition q;
	
	private XmlLabelFactory<L> xfLabel;
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	
	public XmlConditionFactory(Condition q){this(null,q);}
	public XmlConditionFactory(String localeCode,Condition q)
	{
		this.q=q;
		
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getLabel())) {xfLabel = new XmlLabelFactory<>(localeCode);}
		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public Condition build(S ejb){return build(ejb,null);}
	public Condition build(S ejb, String group)
	{
		Condition xml = new Condition();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescription.create(ejb.getDescription()));}
		if(Objects.nonNull(q.getLabel())) {xml.setLabel(xfLabel.build(ejb));}
		
		return xml;
	}
	
	public static <E extends Enum<E>> Condition build(E code){return create(code.toString());}
	public static Condition create(String code)
	{
		Condition xml = new Condition();
		xml.setCode(code);
		return xml;
	}
	
	public static Condition label(String code, String label)
	{
		Condition xml = new Condition();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Condition id()
	{
		Condition xml = new Condition();
		xml.setId(0l);
		return xml;
	}
	
	public static List<Long> toIds(List<Outcome> list)
	{
		List<Long> result = new ArrayList<Long>();
		for(Outcome c : list)
		{
			if(Objects.nonNull(c.getId())) {result.add(c.getId());}
		}
		return result;
	}
	
	public static Outcome build(Status status)
	{
		Outcome type = new Outcome();
		type.setCode(status.getCode());
		type.setDescriptions(status.getDescriptions());
		type.setLangs(status.getLangs());
		return type;
	}
}