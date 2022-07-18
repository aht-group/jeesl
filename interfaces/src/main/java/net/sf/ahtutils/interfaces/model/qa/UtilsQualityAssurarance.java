package net.sf.ahtutils.interfaces.model.qa;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface UtilsQualityAssurarance<
					STAFF extends UtilsQaStaff<?,?,?,?,QASH>,
					QAC extends UtilsQaCategory<?,?>,
					QASH extends UtilsQaStakeholder<?>>
			extends Serializable,EjbWithId,EjbSaveable
{
	List<QAC> getCategories();
	void setCategories(List<QAC> categories);
	
	List<STAFF> getStaff();
	void setStaff(List<STAFF> staff);
	
	List<QASH> getStakeholders();
	void setStakeholders(List<QASH> stakeholders);
}