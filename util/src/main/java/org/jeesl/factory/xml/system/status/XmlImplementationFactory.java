package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Implementation;

public class XmlImplementationFactory<S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlImplementationFactory.class);
		
	private String localeCode;
	private Implementation q;
	
//	public XmlProgramFactory(Query q){this(q.getLang(),q.getType());}
	public XmlImplementationFactory(Implementation q){this(null,q);}
	public XmlImplementationFactory(String localeCode, Implementation q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Implementation build(S ejb){return build(ejb,null);}
	public Implementation build(S ejb, String group)
	{
		Implementation xml = new Implementation();
		if(q.isSetId()){xml.setId(ejb.getId());}
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
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(q.getDescriptions());
			xml.setDescriptions(f.create(ejb.getDescription()));
		}
		
		if(q.isSetLabel() && localeCode!=null)
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
	
	public static <E extends Enum<E>> Implementation build(E code){return build(code.toString());}
	public static <E extends Enum<E>> Implementation build(String code){return build(code.toString(),null);}
	public static Implementation build(String code,String label)
	{
		Implementation xml = new Implementation();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}