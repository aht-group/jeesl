package net.sf.ahtutils.interfaces.model.qa;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.model.interfaces.with.EjbWithRecord;

public interface UtilsQaTestInfo<QATC extends JeeslStatus<QATC,?,?>>
			extends Serializable,EjbSaveable,EjbWithRecord,EjbWithId
{
	QATC getCondition();
	void setCondition(QATC condition);
	
	String getDescription();
    void setDescription(String description);
}