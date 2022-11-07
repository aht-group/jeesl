package org.jeesl.interfaces.model.with.system.io.cms;

import java.util.Map;

import org.jeesl.interfaces.model.system.locale.JeeslMarkup;

public interface JeeslWithMarkupMulti <M extends JeeslMarkup<?>>
{
	Map<String,M> getMarkup();
	void setMarkup(Map<String,M> translation);
}