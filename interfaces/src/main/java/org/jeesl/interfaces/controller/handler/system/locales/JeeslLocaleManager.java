package org.jeesl.interfaces.controller.handler.system.locales;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.system.locale.JeeslLocale;

public interface JeeslLocaleManager<LOC extends JeeslLocale<?,?,LOC,?>> extends Serializable
{	
//	void x();
	
	void addLocale(LOC loc);
	List<String> getLocaleCodes();
	
	String getPrimaryLocaleCode();
	boolean hasLocale(String localeCode);
}