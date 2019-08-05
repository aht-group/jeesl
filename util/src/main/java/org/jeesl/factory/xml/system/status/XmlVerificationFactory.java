package org.jeesl.factory.xml.system.status;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.status.Verification;

public class XmlVerificationFactory<L extends UtilsLang, D extends UtilsDescription, S extends UtilsStatus<S,L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlVerificationFactory.class);
		
	private String localeCode;
	private Verification q;
	
//	public XmlProgramFactory(Query q){this(q.getLang(),q.getType());}
	public XmlVerificationFactory(Verification q){this(null,q);}
	public XmlVerificationFactory(String localeCode, Verification q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public Verification build(S ejb){return build(ejb,null);}
	public Verification build(S ejb, String group)
	{
		Verification xml = new Verification();
		if(q.isSetId()){xml.setId(ejb.getId());}
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
	
	public static <E extends Enum<E>> Verification build(E code){return build(code.toString());}
	public static <E extends Enum<E>> Verification build(String code){return build(code.toString(),null);}
	public static Verification build(String code,String label)
	{
		Verification xml = new Verification();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}