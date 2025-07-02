package org.jeesl.jsf.function;

import java.util.Map;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Title
{
	final static Logger logger = LoggerFactory.getLogger(Title.class);
	
    private Title() {}
    
    public static String titleFor(Object object, String localeCode)
    {
		String title = "";
		if (object instanceof EjbWithName)
		{
			title = ((EjbWithName) object).getName();
		}
		else if (object instanceof EjbWithLang)
		{
			EjbWithLang withLang = (EjbWithLang) object;
			Map<String, JeeslLang> translationsMap = (Map<String, JeeslLang>) withLang.getName();
			title = translationsMap.get(localeCode).getLang();
		}
    	return title;
    }

}
