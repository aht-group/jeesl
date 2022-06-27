package net.sf.ahtutils.interfaces.model.qa;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface UtilsQaTestInfo<QATC extends JeeslStatus<?,?,QATC>>
			extends Serializable,EjbSaveable,EjbWithRecord,EjbWithId
{
	QATC getCondition();
	void setCondition(QATC condition);
	
	String getDescription();
    void setDescription(String description);
}