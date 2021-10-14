package net.sf.ahtutils.interfaces.web;

import java.util.Map;

public interface UtilsJsfOptionTableBean
{
	boolean getSupportsSymbol();
	boolean getSupportsGraphic();
	boolean getSupportsColour();

	Map<Long, Boolean> getAllowAdditionalElements();

	void changeGraphicType();
}