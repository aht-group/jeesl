package org.jeesl.interfaces.bean.system.io.option;

import java.util.Map;

public interface UtilsJsfOptionTableBean
{
	boolean getSupportsSymbol();
	boolean getSupportsGraphic();
	boolean getSupportsColour();

	Map<Long, Boolean> getAllowAdditionalElements();

	void changeGraphicType();
}