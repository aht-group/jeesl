package org.jeesl.api.facade.module;

import java.time.LocalDate;
import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuScope;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuStatus;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuViewport;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.time.JeeslTimeDayOfWeek;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslTafuFacade <L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,?>,
									T extends JeeslTafuTask<R,TS,SC,M>,
									TS extends JeeslTafuStatus<L,D,TS,?>,
									SC extends JeeslTafuScope<L,D,R,SC,?>,
									VP extends JeeslTafuViewport<L,D,VP,?>,
									DOW extends JeeslTimeDayOfWeek<L,D,DOW,?>,
									M extends JeeslMarkup<?>>
			extends JeeslFacade
{	
	<RREF extends EjbWithId> List<T> fTafuBacklog(R realm, RREF rref, LocalDate vpStart, LocalDate vpEnd);
	<RREF extends EjbWithId> List<T> fTafuActive(R realm, RREF rref, LocalDate vpStart, LocalDate vpEnd);
}