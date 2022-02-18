package org.jeesl.interfaces.model.module.diary;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithDiary <DIARY extends JeeslLogBook<?,?>>
		extends EjbWithId
{
	public enum Attributes{log}
	
	DIARY getLog();
	void setLog(DIARY log);
}