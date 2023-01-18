package org.jeesl.interfaces.model.with.primitive.code;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface EjbWithCode extends EjbWithId
{	
	public enum Attribute{code}
	
	public String getCode();
	public void setCode(String code);
}