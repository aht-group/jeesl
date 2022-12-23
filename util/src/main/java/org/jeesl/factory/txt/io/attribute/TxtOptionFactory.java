package org.jeesl.factory.txt.io.attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

public class TxtOptionFactory <L extends JeeslLang, D extends JeeslDescription, OPTION extends JeeslAttributeOption<L,D,?>>
{
	private final String localeCode;
	
	public static <L extends JeeslLang, D extends JeeslDescription, OPTION extends JeeslAttributeOption<L,D,?>> 
					TxtOptionFactory<L,D,OPTION> instance(String localeCode) {return new TxtOptionFactory<>(localeCode);}
	public TxtOptionFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public String labels (Set<OPTION> set) {return this.labels(new ArrayList<>(set));}
	public String labels (List<OPTION> list)
	{
		if(list==null || list.isEmpty()){return null;}
		List<String> result = new ArrayList<String>();
		for(OPTION ejb : list){result.add(label(ejb));}
		return StringUtils.join(result, ", ");
	}
	
	public String label(OPTION option) {return option.getName().get(localeCode).getLang();}
}