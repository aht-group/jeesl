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
import org.jeesl.model.xml.io.locale.status.Outcome;
import org.jeesl.model.xml.io.locale.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlOutcomeFactory<S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlOutcomeFactory.class);
	
	private Outcome q;
	
	private XmlLabelFactory<L> xfLabel;
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	
	public XmlOutcomeFactory(Outcome q){this(null,q);}
	public XmlOutcomeFactory(String localeCode,Outcome q)
	{
		this.q=q;
		
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getLabel())) {xfLabel = new XmlLabelFactory<>(localeCode);}
		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}
	
	public Outcome build(S ejb){return build(ejb,null);}
	public Outcome build(S ejb, String group)
	{
		Outcome xml = new Outcome();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescription.create(ejb.getDescription()));}
		if(Objects.nonNull(q.getLabel())) {xml.setLabel(xfLabel.build(ejb));}
		
		return xml;
	}
	
	public static <E extends Enum<E>> Outcome build(E code){return create(code.toString());}
	public static Outcome create(String code)
	{
		Outcome xml = new Outcome();
		xml.setCode(code);
		return xml;
	}
	
	public static Outcome label(String code, String label)
	{
		Outcome xml = new Outcome();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Outcome id()
	{
		Outcome xml = new Outcome();
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