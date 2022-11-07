package org.jeesl.interfaces.model.with.system.io.cms;

import org.jeesl.interfaces.model.system.locale.JeeslMarkup;

public interface JeeslWithMarkupSingle <M extends JeeslMarkup<?>>
{
	M getMarkup();
	void setMarkup(M markup);
}