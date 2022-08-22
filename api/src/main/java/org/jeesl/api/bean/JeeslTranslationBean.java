package org.jeesl.api.bean;

import java.util.List;

import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;

public interface JeeslTranslationBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>>
		extends JeeslLocaleProvider<LOC>
{
	void ping();
	List<String> getLangKeys();
	String get(String localeCode, String translationKey);
}