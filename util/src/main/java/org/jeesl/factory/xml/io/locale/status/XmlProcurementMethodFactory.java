package org.jeesl.factory.xml.io.locale.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.ProcurementMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlProcurementMethodFactory <L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlProcurementMethodFactory.class);
		
	private ProcurementMethod q;
	
	public XmlProcurementMethodFactory(ProcurementMethod q)
	{
		this.q=q;
	}
	
	public ProcurementMethod build(S ejb){return build(ejb,null);}
	public ProcurementMethod build(S ejb, String group)
	{
		ProcurementMethod xml = new ProcurementMethod();
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
	
	public static ProcurementMethod build(String code)
	{
		ProcurementMethod xml = new ProcurementMethod();
		xml.setCode(code);
		return xml;
	}
}