package org.jeesl.interfaces.model.module.ts.data;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslTsDataPoint <DATA extends JeeslTsData<?,?,?,?>,
									MP extends JeeslTsMultiPoint<?,?,?,?>>
		extends EjbWithId,EjbSaveable,Serializable,EjbRemoveable,EjbPersistable,EjbWithParentAttributeResolver
{
	public enum Attributes {data}
	
	DATA getData();
	void setData(DATA data);
	
	MP getMultiPoint();
	void setMultiPoint(MP multiPoint);
	
	Double getValue();
	void setValue(Double value);
}