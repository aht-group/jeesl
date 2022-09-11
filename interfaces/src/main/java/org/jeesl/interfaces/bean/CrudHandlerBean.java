package org.jeesl.interfaces.bean;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface CrudHandlerBean <T extends EjbWithId>
{
	T crudBuild(Class<T> cT);
	
	T crudPreSave(T t);
	void crudPostSave(T t);
	
	void crudNotifySelect(T t);
	void crudNotifyRemove(T t);
	
	void crudRmConstraintViolation(Class<T> cT);
}