package org.jeesl.api.facade.module;

import java.time.LocalDate;
import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.journal.JeeslJournalBook;
import org.jeesl.interfaces.model.module.journal.JeeslJournalScope;
import org.jeesl.interfaces.model.module.journal.JeeslJournalImpact;
import org.jeesl.interfaces.model.module.journal.JeeslJournalItem;
import org.jeesl.interfaces.model.module.journal.JeeslJournalDomain;
import org.jeesl.interfaces.model.module.journal.JeeslWithJournal;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.JeeslJournalQuery;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;

public interface JeeslJournalFacade <L extends JeeslLang, D extends JeeslDescription,
									BOOK extends JeeslJournalBook<SCOPE,ITEM>,
									SCOPE extends JeeslJournalDomain<L,D,SCOPE,?>,
									ITEM extends JeeslJournalItem<L,D,?,?,BOOK,IMPACT,CONF,USER>,
									IMPACT extends JeeslJournalImpact<L,D,IMPACT,?>,
									CONF extends JeeslJournalScope<L,D,CONF,?>,
									USER extends EjbWithId
									>
			extends JeeslFacade
{	
	<OWNER extends JeeslWithJournal<BOOK>> OWNER fDiaryOwner(Class<OWNER> cOwner, BOOK diary) throws JeeslNotFoundException;
	List<ITEM> fJournalItems(JeeslJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> query);
	List<ITEM> fLogItems(List<BOOK> books);
	List<ITEM> fLogItems(List<BOOK> books, List<SCOPE> scopes, List<CONF> confidentialities, LocalDate ldStart, LocalDate ldEnd);
	
	JsonTuples1<CONF> tpcJournalScope(JeeslJournalQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> query);
}