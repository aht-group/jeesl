package net.sf.ahtutils.factory.xml.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Freeze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlFreezeFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlFreezeFactory.class);
		
	private Freeze q;
	
	public XmlFreezeFactory(Freeze q)
	{
		this.q=q;
	}
	
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Freeze build(S ejb)
	{
		Freeze xml = new Freeze();
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
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
	
	public static Freeze build(String code){return build(code, null);}
	public static Freeze build(String code, String label)
	{
		Freeze xml = new Freeze();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}