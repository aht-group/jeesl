package net.sf.ahtutils.interfaces.model.qa;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.crud.EjbCrudWithParent;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.with.date.EjbWithDateRange;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.model.interfaces.with.EjbWithName;

public interface UtilsQaScheduleSlot<GROUP extends UtilsQaGroup<?,?,?>,QASD extends UtilsQaSchedule<?,?>>
			extends Serializable,EjbCrudWithParent,EjbPersistable,EjbRemoveable,EjbWithId,EjbWithDateRange,EjbWithName
{
	QASD getSchedule();
	void setSchedule(QASD schedule);
	
	List<GROUP> getGroups();
	void setGroups(List<GROUP> groups);
}