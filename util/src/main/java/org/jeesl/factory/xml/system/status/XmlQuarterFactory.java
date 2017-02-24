package org.jeesl.factory.xml.system.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.factory.xml.status.XmlDescriptionsFactory;
import net.sf.ahtutils.factory.xml.status.XmlLangsFactory;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.status.Quarter;
import net.sf.ahtutils.xml.status.Status;

public class XmlQuarterFactory<S extends UtilsStatus<S,L,D>,L extends UtilsLang, D extends UtilsDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlStyleFactory.class);
	
	private final String localeCode;
	private final Quarter q;
		
	public XmlQuarterFactory(String localeCode, Quarter q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Quarter build(S ejb){return build(ejb,null);}
	public <E extends Enum<E>> Quarter build(E group, S ejb){return build(ejb,group.toString());}
	public Quarter build(S ejb, String group)
	{
		Quarter xml = new Quarter();
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
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(q.getDescriptions());
			xml.setDescriptions(f.create(ejb.getDescription()));
		}
		
		if(q.isSetLabel() && localeCode!=null)
		{
			if(ejb.getName()!=null)
			{
				if(ejb.getName().containsKey(localeCode)){xml.setLabel(ejb.getName().get(localeCode).getLang());}
				else{logger.warn("No translation "+localeCode+" available in "+ejb);}
			}
			else{logger.warn("No @name available in "+ejb);}
		}
		return xml;
	}
	
	public static Quarter build(String code)
	{
		Quarter xml = new Quarter();
		xml.setCode(code);
		return xml;
	}
	public static Quarter build(String code, String label)
	{
		Quarter xml = new Quarter();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Quarter build(Status status)
	{
		Quarter xml = new Quarter();
		xml.setCode(status.getCode());
		xml.setDescriptions(status.getDescriptions());
		xml.setLangs(status.getLangs());
		return xml;
	}
}