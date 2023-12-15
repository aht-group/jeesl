package org.jeesl.factory.xml.system.io.locale;

import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLabelFactory<L extends JeeslLang>
{
	final static Logger logger = LoggerFactory.getLogger(XmlLabelFactory.class);
		
	private final String localeCode;
	
	public XmlLabelFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public String build(EjbWithLang<L> ejb)
	{
		if(ejb.getName()!=null)
		{
			if(ejb.getName().containsKey(localeCode)) {return ejb.getName().get(localeCode).getLang();}
			else
			{
				String msg = "No translation "+localeCode+" available in "+ejb;
				logger.warn(msg);
				return msg;
			}
		}
		else
		{
			String msg = "No @name available in "+ejb;
			logger.warn(msg);
			return msg;
		}
	}
}