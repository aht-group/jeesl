package org.jeesl.interfaces.model.system.job.cache;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.job.JeeslJobTemplate;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.date.ju.EjbWithValidUntil;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslJobCache<TEMPLATE extends JeeslJobTemplate<?,?,?,?,?,?>,
								CONTAINER extends JeeslFileContainer<?,?>>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithNonUniqueCode,EjbWithRecord,EjbWithValidUntil
{
	public static enum Attributes{template,code};
	
	TEMPLATE getTemplate();
	void setTemplate(TEMPLATE template);
	
	byte[] getData();
	void setData(byte[] data);
}