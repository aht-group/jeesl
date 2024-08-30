package org.jeesl.factory.txt.system.locale;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.exlp.util.io.StringUtil;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtMultiLangFactory <L extends JeeslLang>
{
	final static Logger logger = LoggerFactory.getLogger(TxtLangsFactory.class);
	
	private final String localeCode;
	
	public static <L extends JeeslLang, LC extends Enum<LC>> TxtMultiLangFactory<L> instance(LC localeCode)
	{
		return new TxtMultiLangFactory<>(localeCode.toString());
	}
	
	private TxtMultiLangFactory(String localeCode)
	{
		this.localeCode = localeCode;
	}
	
	public String label(Map<String,L> map)
	{
		if(Objects.isNull(map)) {return null;}
		else if(!map.containsKey(localeCode)) {return null;}
		else {return map.get(localeCode).getLang();}
	}
	
	public static <L extends JeeslLang, T extends EjbWithLang<L>> String label(String localeCode, List<T> list)
	{
		if(list==null || list.isEmpty()) {return null;}
		List<String> result = new ArrayList<String>();
		for(T ejb : list){result.add(ejb.getName().get(localeCode).getLang());}
		return StringUtils.join(result, ", ");
	}
	
	public static <L extends JeeslLang> void debug(boolean debug, String description, Map<String,L> map)
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