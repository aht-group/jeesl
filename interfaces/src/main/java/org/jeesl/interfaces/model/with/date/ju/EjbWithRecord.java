package org.jeesl.interfaces.model.with.date.ju;

import java.util.Date;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface EjbWithRecord extends EjbWithId
{
	public enum Attribute{record}
//	priv static String attributeRecord = "record";
	
	Date getRecord();
	void setRecord(Date record);
}
