package org.jeesl.interfaces.model.system.locale;

import java.io.Serializable;
import java.util.List;

public interface JeeslLocaleProvider<LOC extends JeeslLocale<?,?,LOC,?>> extends Serializable
{	
	void addLocale(LOC loc);
	List<String> getLocaleCodes();
	
	String getPrimaryLocaleCode();
	boolean hasLocale(String localeCode);
}