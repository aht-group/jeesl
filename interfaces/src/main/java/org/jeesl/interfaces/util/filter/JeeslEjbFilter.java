package org.jeesl.interfaces.util.filter;

import java.util.Map;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.primefaces.model.FilterMeta;

public interface JeeslEjbFilter <T extends EjbWithId>
{
	boolean matches(Map<String,FilterMeta> filters, T ejb);
}