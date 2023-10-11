package org.jeesl.interfaces.model.module.journal;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithJournal <DIARY extends JeeslJournalBook<?,?>>
		extends EjbWithId
{
	public enum Attributes{log}
	
	DIARY getLog();
	void setLog(DIARY log);
}