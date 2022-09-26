package org.jeesl.api.facade.module;

import java.time.LocalDate;
import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.diary.JeeslLogBook;
import org.jeesl.interfaces.model.module.diary.JeeslLogConfidentiality;
import org.jeesl.interfaces.model.module.diary.JeeslLogImpact;
import org.jeesl.interfaces.model.module.diary.JeeslLogItem;
import org.jeesl.interfaces.model.module.diary.JeeslLogScope;
import org.jeesl.interfaces.model.module.diary.JeeslWithDiary;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslLogFacade <L extends JeeslLang, D extends JeeslDescription,
									BOOK extends JeeslLogBook<SCOPE,ITEM>,
									SCOPE extends JeeslLogScope<L,D,SCOPE,?>,
									ITEM extends JeeslLogItem<L,D,?,?,BOOK,IMPACT,CONF,USER>,
									IMPACT extends JeeslLogImpact<L,D,IMPACT,?>,
									CONF extends JeeslLogConfidentiality<L,D,CONF,?>,
									USER extends EjbWithId
									>
			extends JeeslFacade
{	
	<OWNER extends JeeslWithDiary<BOOK>> OWNER fDiaryOwner(Class<OWNER> cOwner, BOOK diary) throws JeeslNotFoundException;
	List<ITEM> fLogItems(List<BOOK> books);
	List<ITEM> fLogItems(List<BOOK> books, List<SCOPE> scopes, List<CONF> confidentialities, LocalDate ldStart, LocalDate ldEnd);
}