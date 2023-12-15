package net.sf.ahtutils.factory.xml.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Frequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlFrequencyFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlFrequencyFactory.class);
		
	private Frequency q;
	
	public XmlFrequencyFactory(Frequency q)
	{
		this.q=q;
	}
	
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Frequency build(S ejb){return build(ejb,null);}
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Frequency build(S ejb, String group)
	{
		Frequency xml = new Frequency();
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
	
	public static Frequency build(String code)
	{
		Frequency xml = new Frequency();
		xml.setCode(code);
		return xml;
	}
}