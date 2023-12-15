package net.sf.ahtutils.factory.xml.status;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Statement;

public class XmlStatementFactory <S extends JeeslStatus<L,D,S>, L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(XmlStatementFactory.class);
		
	private String lang;
	private Statement q;
	
	public XmlStatementFactory(String lang, Statement q)
	{
		this.lang=lang;
		this.q=q;
	}
	
	public Statement build(S ejb){return build(ejb,null);}
	public Statement build(S ejb, String group)
	{
		Statement xml = new Statement();
		xml.setGroup(group);
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(q.isSetPosition()){xml.setPosition(ejb.getPosition());}
		if(q.isSetImage()){xml.setImage(ejb.getImage());}
		if(q.isSetVisible()){xml.setVisible(ejb.isVisible());}
		
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
		
		if(q.isSetLabel())
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
	
	public static Statement build(String code)
	{
		Statement xml = new Statement();
		xml.setCode(code);
		return xml;
	}
}