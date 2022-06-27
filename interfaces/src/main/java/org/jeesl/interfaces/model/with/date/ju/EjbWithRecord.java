package org.jeesl.interfaces.model.with.date.ju;

import java.util.Date;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface EjbWithRecord extends EjbWithId
{
	public static String attributeRecord = "record";
	
	public Date getRecord();
	public void setRecord(Date record);
}
