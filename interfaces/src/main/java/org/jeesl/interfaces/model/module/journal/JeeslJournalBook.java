package org.jeesl.interfaces.model.module.journal;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslJournalBook <SCOPE extends JeeslJournalDomain<?,?,SCOPE,?>,
								ITEM extends JeeslJournalItem<?,?,?,?,?,?,?,?>>
		extends Serializable,EjbSaveable,EjbWithId
{
	public enum Attributes{scope}
	
	SCOPE getScope();
	void setScope(SCOPE scope);
	
	List<ITEM> getIssues();
	void setIssues(List<ITEM> issues);
}