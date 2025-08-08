package org.jeesl.interfaces.util.query.module;

import org.jeesl.interfaces.model.module.journal.JeeslJournalBook;
import org.jeesl.interfaces.model.module.journal.JeeslJournalScope;
import org.jeesl.interfaces.model.module.journal.JeeslJournalImpact;
import org.jeesl.interfaces.model.module.journal.JeeslJournalItem;
import org.jeesl.interfaces.model.module.journal.JeeslJournalDomain;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslJournalQuery <BOOK extends JeeslJournalBook<DOMAIN,ITEM>,
								DOMAIN extends JeeslJournalDomain<?,?,DOMAIN,?>,
								ITEM extends JeeslJournalItem<?,?,?,?,BOOK,IMPACT,SCOPE,USER>,
								IMPACT extends JeeslJournalImpact<?,?,IMPACT,?>,
								SCOPE extends JeeslJournalScope<?,?,SCOPE,?>,
								USER extends EjbWithId>
			extends JeeslCoreQuery
{
	
//	private List<BOOK> books;
	
}