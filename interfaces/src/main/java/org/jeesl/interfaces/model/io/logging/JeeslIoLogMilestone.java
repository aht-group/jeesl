package org.jeesl.interfaces.model.io.logging;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDateTime;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;

public interface JeeslIoLogMilestone<LOG extends JeeslIoLog<?,?,?>>
		extends Serializable,EjbWithId,EjbRemoveable,EjbPersistable,EjbSaveable,
		JeeslWithRecordDateTime,EjbWithName
{	
	public static enum Attributes{log};
	
	LOG getLog();
	void setLog(LOG log);
	
	
	long getMilliTotal();
	void setMilliTotal(long milliTotal);
	
	long getMilliStep();
	void setMilliStep(long milliStep);
	
	long getMilliRelative();
	void setMilliRelative(long milliRelative);
	

	String getMessage();
	void setMessage(String message);
	
	Integer getElements();
	void setElements(Integer elements);
}