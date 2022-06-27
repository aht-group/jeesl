package net.sf.ahtutils.interfaces.model.qa;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.crud.EjbCrudWithParent;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.with.date.ju.EjbWithDateRange;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;

public interface UtilsQaScheduleSlot<GROUP extends UtilsQaGroup<?,?,?>,QASD extends UtilsQaSchedule<?,?>>
			extends Serializable,EjbCrudWithParent,EjbPersistable,EjbRemoveable,EjbWithId,EjbWithDateRange,EjbWithName
{
	QASD getSchedule();
	void setSchedule(QASD schedule);
	
	List<GROUP> getGroups();
	void setGroups(List<GROUP> groups);
}