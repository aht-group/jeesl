package org.jeesl.factory.json.system.io.label;

import org.jeesl.interfaces.model.io.label.entity.JeeslLabelNumberPrecision;
import org.jeesl.model.json.io.label.JsonLabelNumberPrecision;

public class JsonIoLabelNumberPrecisionFactory
{
	public static <P extends JeeslLabelNumberPrecision<?,?,P,?>> void apply(P ejb, JsonLabelNumberPrecision json)
	{
		String[] array = ejb.getSymbol().split(",");
		json.setMinFractionDigits(Integer.valueOf(array[0]));
		json.setMaxFractionDigits(Integer.valueOf(array[1]));
	}
}