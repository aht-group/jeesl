package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Phase;
import net.sf.ahtutils.xml.status.Status;

public class XmlPhaseFactory<L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlPhaseFactory.class);
		
	private String lang;
	private Phase q;
	
	public XmlPhaseFactory(String lang, Phase q)
	{
		this.lang=lang;
		this.q=q;
	}
	
	public Phase build(S ejb){return build(ejb,null);}
	public Phase build(S ejb, String group)
	{
		Phase xml = new Phase();
		if(ejb!=null)
		{
			if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
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
			else if(q.isSetLabel() && lang==null){logger.warn("Should render label, but lang is null");}
		}
		
		return xml;
	}
	
	public static Phase build(String code)
	{
		Phase xml = new Phase();
		xml.setCode(code);
		return xml;
	}
	
	public static Phase buildLabel(String code, String label)
	{
		Phase xml = new Phase();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Phase build(Status status)
	{
		Phase xml = new Phase();
		xml.setCode(status.getCode());
		xml.setDescriptions(status.getDescriptions());
		xml.setLangs(status.getLangs());
		return xml;
	}
}