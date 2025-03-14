package org.jeesl.factory.xml.io.locale.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Satisfaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSatisfactionFactory <L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlSatisfactionFactory.class);
		
	private Satisfaction q;
	
	public XmlSatisfactionFactory(Satisfaction q)
	{
		this.q=q;
	}
	
	public Satisfaction build(S ejb){return build(ejb,null);}
	public Satisfaction build(S ejb, String group)
	{
		Satisfaction xml = new Satisfaction();
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(Objects.nonNull(q.getLangs()))
		{
			XmlLangsFactory<L> f = new XmlLangsFactory<L>(q.getLangs());
			xml.setLangs(f.getUtilsLangs(ejb.getName()));
		}
		if(Objects.nonNull(q.getDescriptions()))
		{

		}
		return xml;
	}
	
	public static Satisfaction build(String code)
	{
		Satisfaction xml = new Satisfaction();
		xml.setCode(code);
		return xml;
	}
}