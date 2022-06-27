package net.sf.ahtutils.interfaces.model.qa;

import java.io.Serializable;
import java.util.Date;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface UtilsQaResult<STAFF extends UtilsQaStaff<?,?,?,?,?>,
				QAT extends UtilsQaTest<?,?,?,?,?,?>,
				QARS extends JeeslStatus<?,?,QARS>>
			extends Serializable,EjbSaveable,EjbWithRecord,EjbWithId
{
	QAT getTest();
	void setTest(QAT test);
	
	STAFF getStaff();
	void setStaff(STAFF staff);
	
	QARS getStatus();
	void setStatus(QARS status);
	
	String getActualResult();
	void setActualResult(String actualResult);
	
	String getComment();
	void setComment(String comment);
	
	Date getRecord();
	void setRecord(Date record);
}