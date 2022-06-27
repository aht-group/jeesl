package org.jeesl.interfaces.model.io.mail;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.date.ju.EjbWithValidUntil;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslMailLink<L extends JeeslLang, D extends JeeslDescription,
							S extends JeeslStatus<L,D,S>>
						extends Serializable,EjbPersistable,EjbRemoveable, EjbWithId,EjbWithCode,EjbWithValidUntil
{
	long getRefId();
	void setRefId(long refId);
	
	S getType();
	void setType(S type);
}