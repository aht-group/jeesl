package org.jeesl.interfaces.util.query.module;

import org.jeesl.interfaces.model.module.journal.JeeslJournalBook;
import org.jeesl.interfaces.model.module.journal.JeeslJournalConfidentiality;
import org.jeesl.interfaces.model.module.journal.JeeslJournalImpact;
import org.jeesl.interfaces.model.module.journal.JeeslJournalItem;
import org.jeesl.interfaces.model.module.journal.JeeslJournalScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslJournalQuery <BOOK extends JeeslJournalBook<SCOPE,ITEM>,
								SCOPE extends JeeslJournalScope<?,?,SCOPE,?>,
								ITEM extends JeeslJournalItem<?,?,?,?,BOOK,IMPACT,CONF,USER>,
								IMPACT extends JeeslJournalImpact<?,?,IMPACT,?>,
								CONF extends JeeslJournalConfidentiality<?,?,CONF,?>,
								USER extends EjbWithId>
			extends JeeslCoreQuery
{
	
	
	
//	private List<BOOK> books;
	
}