package org.jeesl.api.facade.module;

import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEvent;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEventType;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdFaq;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdFga;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdLevel;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdMessage;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdPriority;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdScope;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicket;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicketCategory;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicketStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.mcs.JeeslMcsRealm;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.EjbHelpdeskQuery;

public interface JeeslHdFacade <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslMcsRealm<L,D,R,?>, 
								TICKET extends JeeslHdTicket<R,EVENT,M,?>,
								CAT extends JeeslHdTicketCategory<L,D,R,CAT,?>,
								STATUS extends JeeslHdTicketStatus<L,D,R,STATUS,?>,
								EVENT extends JeeslHdEvent<TICKET,CAT,STATUS,TYPE,LEVEL,PRIORITY,USER>,
								TYPE extends JeeslHdEventType<L,D,TYPE,?>,
								LEVEL extends JeeslHdLevel<L,D,R,LEVEL,?>,
								PRIORITY extends JeeslHdPriority<L,D,R,PRIORITY,?>,
								MSG extends JeeslHdMessage<TICKET,M,SCOPE,USER>,
								M extends JeeslMarkup<MT>,
								MT extends JeeslIoCmsMarkupType<?,?,MT,?>,
								FAQ extends JeeslHdFaq<L,D,R,CAT,SCOPE>,
								SCOPE extends JeeslHdScope<L,D,SCOPE,?>,
								FGA extends JeeslHdFga<FAQ,DOC,SEC>,
								DOC extends JeeslIoCms<L,D,?,SEC,?>,
								SEC extends JeeslIoCmsSection<L,SEC>,
								USER extends JeeslSimpleUser>
			extends JeeslFacade
{	
	TICKET saveHdTicket(TICKET ticket, EVENT event, USER user) throws JeeslConstraintViolationException, JeeslLockingException;
	
	<RREF extends EjbWithId> List<TICKET> fHdTickets(EjbHelpdeskQuery<L,D,R,RREF,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> query);
}