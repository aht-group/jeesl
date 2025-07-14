package org.jeesl.interfaces.cache;

import org.apache.poi.ss.formula.functions.T;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithSomeCode;

public interface JeesCodeCache <T extends EjbWithSomeCode>
{	
	public T ejb(String code) throws JeeslNotFoundException;
}