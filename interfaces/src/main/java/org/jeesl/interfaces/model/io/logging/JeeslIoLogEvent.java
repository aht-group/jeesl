package org.jeesl.interfaces.model.io.logging;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;

public interface JeeslIoLogEvent<LOG extends JeeslIoLog<?,?,?>
								>
		extends Serializable,EjbWithId,EjbRemoveable,EjbPersistable,EjbSaveable,
				EjbWithNonUniqueCode,EjbWithName
{	
	public static enum Attributes{log};
	
	LOG getLog();
	void setLog(LOG log);

	int getCounter();
	void setCounter(int counter);
}