package net.sf.ahtutils.factory.xml.status;

import net.sf.ahtutils.xml.status.Frequency;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
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
	
	public <S extends JeeslStatus<S,L,D>,L extends JeeslLang, D extends JeeslDescription> Frequency build(S ejb){return build(ejb,null);}
	public <S extends JeeslStatus<S,L,D>,L extends JeeslLang, D extends JeeslDescription> Frequency build(S ejb, String group)
	{
		Frequency xml = new Frequency();
		if(q.isSetCode()){xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
		xml.setGroup(group);
		
		if(q.isSetLangs())
		{
			XmlLangsFactory<L> f = new XmlLangsFactory<L>(q.getLangs());
			xml.setLangs(f.getUtilsLangs(ejb.getName()));
		}
		if(q.isSetDescriptions())
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