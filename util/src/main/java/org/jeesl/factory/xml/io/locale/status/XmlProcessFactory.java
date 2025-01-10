package org.jeesl.factory.xml.io.locale.status;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
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
		
	private org.jeesl.model.xml.io.locale.status.Process q;
	private String localeCode;
	
	public XmlProcessFactory(org.jeesl.model.xml.io.locale.status.Process q){this(null,q);}
	public XmlProcessFactory(String localeCode, org.jeesl.model.xml.io.locale.status.Process q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public <S extends JeeslStatus<L,D,S>, L extends JeeslLang, D extends JeeslDescription> org.jeesl.model.xml.io.locale.status.Process build(S ejb){return build(ejb,null);}
	public <S extends JeeslStatus<L,D,S>, L extends JeeslLang, D extends JeeslDescription> org.jeesl.model.xml.io.locale.status.Process build(S ejb, String group)
	{
		org.jeesl.model.xml.io.locale.status.Process xml = new org.jeesl.model.xml.io.locale.status.Process();
		xml.setGroup(group);
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		if(Objects.nonNull(q.getStyle())) {xml.setStyle(ejb.getStyle());}
		if(Objects.nonNull(q.getImage())) {xml.setImage(ejb.getImage());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(ejb.isVisible());}
		
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
		
		if(ObjectUtils.allNotNull(q.getLabel(),localeCode))
		{
			if(ejb.getName()!=null)
			{
				if(ejb.getName().containsKey(localeCode)){xml.setLabel(ejb.getName().get(localeCode).getLang());}
				else
				{
					String msg = "No translation "+localeCode+" available in "+ejb;
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
	
	public static org.jeesl.model.xml.io.locale.status.Process build(String code)
	{
		org.jeesl.model.xml.io.locale.status.Process xml = new org.jeesl.model.xml.io.locale.status.Process();
		xml.setCode(code);
		return xml;
	}
}