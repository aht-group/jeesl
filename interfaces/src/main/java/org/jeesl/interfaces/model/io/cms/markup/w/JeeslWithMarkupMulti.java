package org.jeesl.interfaces.model.io.cms.markup.w;

import java.util.Map;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;

public interface JeeslWithMarkupMulti <M extends JeeslIoMarkup<?>>
{
	Map<String,M> getMarkup();
	void setMarkup(Map<String,M> translation);
}