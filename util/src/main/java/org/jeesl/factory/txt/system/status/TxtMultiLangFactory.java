package org.jeesl.factory.txt.system.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public class TxtMultiLangFactory
{
	public static <L extends JeeslLang, T extends EjbWithLang<L>>
		String label(String localeCode, List<T> list)
	{
		if(list==null || list.isEmpty()){return null;}
		List<String> result = new ArrayList<String>();
		for(T ejb : list){result.add(ejb.getName().get(localeCode).getLang());}
		return StringUtils.join(result, ", ");
	}
}