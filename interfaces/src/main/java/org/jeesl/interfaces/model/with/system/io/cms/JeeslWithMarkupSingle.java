package org.jeesl.interfaces.model.with.system.io.cms;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;

public interface JeeslWithMarkupSingle <M extends JeeslIoMarkup<?>>
{
	M getMarkup();
	void setMarkup(M markup);
}