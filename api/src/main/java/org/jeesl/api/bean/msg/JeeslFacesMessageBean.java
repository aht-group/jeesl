package org.jeesl.api.bean.msg;

import java.io.Serializable;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslFacesMessageBean extends Serializable
{
	void growlSuccess(String key);
	void growlError(String key);
	void growlInfoText(String text);
	
	<E extends Enum<E>> void errorText(E id, String text);
	void errorText(String text);
	
	<T extends EjbWithId> void growlSuccessSaved(T t);
	void growlSuccessRemoved();
	
	void errorConstraintViolationDuplicateObject();
	<E extends Enum<E>> void errorConstraintViolationDuplicateObject(E id);
	void errorConstraintViolationInUse();
	void errorConstraintViolationInUse(String id);
}