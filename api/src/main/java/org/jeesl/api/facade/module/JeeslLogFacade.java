package org.jeesl.api.facade.module;

import java.time.LocalDate;
import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.journal.JeeslJournalItem;
import org.jeesl.interfaces.model.module.journal.JeeslJournalScope;
import org.jeesl.interfaces.model.module.journal.JeeslJournalBook;
import org.jeesl.interfaces.model.module.journal.JeeslJournalImpact;
import org.jeesl.interfaces.model.module.journal.JeeslJournalConfidentiality;
import org.jeesl.interfaces.model.module.journal.JeeslWithJournal;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.EjbDiaryQuery;

public interface JeeslLogFacade <L extends JeeslLang, D extends JeeslDescription,
									BOOK extends JeeslJournalBook<SCOPE,ITEM>,
									SCOPE extends JeeslJournalScope<L,D,SCOPE,?>,
									ITEM extends JeeslJournalItem<L,D,?,?,BOOK,IMPACT,CONF,USER>,
									IMPACT extends JeeslJournalImpact<L,D,IMPACT,?>,
									CONF extends JeeslJournalConfidentiality<L,D,CONF,?>,
									USER extends EjbWithId
									>
			extends JeeslFacade
{	
	<OWNER extends JeeslWithJournal<BOOK>> OWNER fDiaryOwner(Class<OWNER> cOwner, BOOK diary) throws JeeslNotFoundException;
	List<ITEM> fDiaryItems(EjbDiaryQuery<BOOK,SCOPE,ITEM,IMPACT,CONF,USER> query);
	List<ITEM> fLogItems(List<BOOK> books);
	List<ITEM> fLogItems(List<BOOK> books, List<SCOPE> scopes, List<CONF> confidentialities, LocalDate ldStart, LocalDate ldEnd);
}