package net.sf.ahtutils.factory.xml.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlConditionFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlConditionFactory.class);
		
	private Condition q;
	
	public XmlConditionFactory(Condition q)
	{
		this.q=q;
	}
	
	public <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription> Condition build(S ejb)
	{
		Condition xml = new Condition();
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
}