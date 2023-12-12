package org.jeesl.api.bean.msg;

import java.io.Serializable;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslFacesMessageBean extends Serializable
{	
	<T extends EjbWithId> void growlSaved(T t);
	<T extends EjbWithId> void growlDeleted(T o);
	
	void growlInfo(String text);
	void growlError(String text);
	
	<FID extends Enum<FID>> void errorText(FID facesId, String text);
	
	<FID extends Enum<FID>> void constraintDuplicate(FID id);
	<FID extends Enum<FID>> void constraintInUse(FID id);
}