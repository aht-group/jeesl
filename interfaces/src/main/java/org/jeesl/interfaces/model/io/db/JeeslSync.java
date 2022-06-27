package org.jeesl.interfaces.model.io.db;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithCategory;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;

public interface JeeslSync<L extends JeeslLang, D extends JeeslDescription,
							STATUS extends JeeslStatus<L,D,STATUS>,
							CATEGORY extends JeeslStatus<L,D,CATEGORY>>
			extends EjbWithId,EjbWithCode,EjbWithRecord,Serializable,EjbPersistable,EjbSaveable,
						JeeslWithStatus<STATUS>,
						JeeslWithCategory<CATEGORY>
{
	public static enum Code{never,pending,success,fail};
}