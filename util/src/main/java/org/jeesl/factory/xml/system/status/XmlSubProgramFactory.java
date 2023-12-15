package org.jeesl.factory.xml.system.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.SubProgram;

public class XmlSubProgramFactory<S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlSubProgramFactory.class);
		
	private String lang;
	private SubProgram q;
	
//	public XmlProgramFactory(Query q){this(q.getLang(),q.getType());}
	public XmlSubProgramFactory(SubProgram q){this(null,q);}
	public XmlSubProgramFactory(String lang,SubProgram q)
	{
		this.lang=lang;
		this.q=q;
	}
	
	public SubProgram build(S ejb){return build(ejb,null);}
	public SubProgram build(S ejb, String group)
	{
		SubProgram xml = new SubProgram();
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
	
	public static <E extends Enum<E>> SubProgram build(E code){return build(code.toString());}
	public static <E extends Enum<E>> SubProgram build(String code){return build(code.toString(),null);}
	public static SubProgram build(String code,String label)
	{
		SubProgram xml = new SubProgram();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
}