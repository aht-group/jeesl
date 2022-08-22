package org.jeesl.interfaces.controller.handler.system.locales;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.system.locale.JeeslLocale;

public interface JeeslLocaleProvider <LOC extends JeeslLocale<?,?,LOC,?>> extends Serializable
{
	List<LOC> getLocales();
}