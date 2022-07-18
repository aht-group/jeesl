package net.sf.ahtutils.interfaces.model.issue;

import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;

public interface UtilsTask<T extends UtilsTask<T>> extends EjbWithId,EjbWithCode,EjbWithName,EjbSaveable
{	
	T getParent();
	void setParent(T parentTask);
	
	List<T> getChilds();
	void setChilds(List<T> childs);
}
