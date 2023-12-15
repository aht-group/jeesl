package net.sf.ahtutils.factory.xml.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlProcessFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlProcessFactory.class);
		
	private net.sf.ahtutils.xml.status.Process q;
	private String lang;
	
	public XmlProcessFactory(net.sf.ahtutils.xml.status.Process q){this(null,q);}
	public XmlProcessFactory(String lang, net.sf.ahtutils.xml.status.Process q)
	{
		this.lang=lang;
		this.q=q;
	}
	
	public <S extends JeeslStatus<L,D,S>, L extends JeeslLang, D extends JeeslDescription> net.sf.ahtutils.xml.status.Process build(S ejb){return build(ejb,null);}
	public <S extends JeeslStatus<L,D,S>, L extends JeeslLang, D extends JeeslDescription> net.sf.ahtutils.xml.status.Process build(S ejb, String group)
	{
		net.sf.ahtutils.xml.status.Process xml = new net.sf.ahtutils.xml.status.Process();
		xml.setGroup(group);
		if(q.isSetId()){xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
		if(Objects.nonNull(q.getStyle())) {xml.setStyle(ejb.getStyle());}
		if(Objects.nonNull(q.getImage())) {xml.setImage(ejb.getImage());}
		if(q.isSetVisible()){xml.setVisible(ejb.isVisible());}
		
		if(Objects.nonNull(q.getLangs()))
		{
			XmlLangsFactory<L> f = new XmlLangsFactory<L>(q.getLangs());
			xml.setLangs(f.getUtilsLangs(ejb.getName()));
		}
		if(Objects.nonNull(q.getDescriptions()))
		{
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(q.getDescriptions());
			xml.setDescriptions(f.create(ejb.getDescription()));
		}
		
		if(q.isSetLabel() && lang!=null)
		{
			if(ejb.getName()!=null)
			{
				if(ejb.getName().containsKey(lang)){xml.setLabel(ejb.getName().get(lang).getLang());}
				else
				{
					String msg = "No translation "+lang+" available in "+ejb;
					logger.warn(msg);
					xml.setLabel(msg);
				}
			}
			else
			{
				String msg = "No @name available in "+ejb;
				logger.warn(msg);
				xml.setLabel(msg);
			}
		}
		
		return xml;
	}
	
	public static net.sf.ahtutils.xml.status.Process build(String code)
	{
		net.sf.ahtutils.xml.status.Process xml = new net.sf.ahtutils.xml.status.Process();
		xml.setCode(code);
		return xml;
	}
}