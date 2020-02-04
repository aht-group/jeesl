package org.jeesl.api.bean;

import java.util.List;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public interface JeeslTranslationBean <L extends JeeslLang, D extends JeeslDescription,
										LOC extends JeeslStatus<LOC,L,D>>
{
	void ping();
	List<String> getLangKeys();
	String get(String localeCode, String translationKey);
	List<LOC> getLocales();
}