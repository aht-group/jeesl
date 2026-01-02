package org.jeesl.interfaces.bean.sb.handler;

import java.io.Serializable;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface SbSingleSelection extends Serializable
{
//	void x();
	void selectSbSingle(EjbWithId ejb);
}