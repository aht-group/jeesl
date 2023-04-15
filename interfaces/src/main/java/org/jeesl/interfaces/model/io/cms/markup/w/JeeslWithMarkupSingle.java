package org.jeesl.interfaces.model.io.cms.markup.w;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithMarkupSingle <M extends JeeslIoMarkup<?>> extends EjbWithId
{
	public enum Attributes{id,markup}
	
	M getMarkup();
	void setMarkup(M markup);
}