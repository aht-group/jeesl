package org.jeesl.factory.xml.system.status;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.status.Program;

public class XmlProgramFactory<S extends UtilsStatus<S,L,D>,L extends UtilsLang, D extends UtilsDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlProgramFactory.class);
		
	private String lang;
	private Program q;
	
//	public XmlProgramFactory(Query q){this(q.getLang(),q.getType());}
	public XmlProgramFactory(Program q){this(null,q);}
	public XmlProgramFactory(String lang,Program q)
	{
		this.lang=lang;
		this.q=q;
	}
	
	public Program build(S ejb){return build(ejb,null);}
	public Program build(S ejb, String group)
	{
		Program xml = new Program();
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
	
	public static <E extends Enum<E>> Program build(E code){return build(code.toString());}
	public static <E extends Enum<E>> Program build(String code){return build(code.toString(),null);}
	public static Program build(String code,String label)
	{
		Program xml = new Program();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}