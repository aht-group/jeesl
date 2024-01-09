package org.jeesl.factory.txt.io.attribute;

import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

public class TxtCriteriaFactory <L extends JeeslLang, CRITERIA extends JeeslAttributeCriteria<L,?,?,?,?,?,?>>
{
	private final String localeCode;
	
	public static <L extends JeeslLang, CRITERIA extends JeeslAttributeCriteria<L,?,?,?,?,?,?>> 
	TxtCriteriaFactory<L,CRITERIA> instance(String localeCode) {return new TxtCriteriaFactory<>(localeCode);}
	public TxtCriteriaFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public String label (CRITERIA criteria) {return criteria.getName().get(localeCode).getLang();}
}