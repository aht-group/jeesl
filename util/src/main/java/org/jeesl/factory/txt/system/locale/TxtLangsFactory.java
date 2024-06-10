package org.jeesl.factory.txt.system.locale;

import java.util.Map;

import org.exlp.util.io.StringUtil;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtLangsFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtLangsFactory.class);
	
	public TxtLangsFactory()
	{
		
	}
	
	@Deprecated //Use TxtMultiLangFactory
	private static <L extends JeeslLang> void debug(boolean debug, String description, Map<String,L> map)
	{
		if(debug)
		{
			logger.info(StringUtil.stars());
			logger.info(description);
			for(String key : map.keySet())
			{
				L l = map.get(key);
				logger.info(l.toString());
			}
		}
	}
}