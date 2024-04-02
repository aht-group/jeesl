package org.jeesl.interfaces.util.query.module;

import java.util.List;

import org.jeesl.interfaces.model.module.hd.JeeslHdCategory;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEvent;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEventType;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdLevel;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdPriority;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicket;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicketStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslHelpdeskQuery<L extends JeeslLang,D extends JeeslDescription,
							R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
							CAT extends JeeslHdCategory<L,D,R,CAT,?>,
							TICKET extends JeeslHdTicket<R,EVENT,?,?>,
							
							STATUS extends JeeslHdTicketStatus<L,D,R,STATUS,?>,
							EVENT extends JeeslHdEvent<TICKET,CAT,STATUS,TYPE,LEVEL,PRIORITY,USER>,
							TYPE extends JeeslHdEventType<L,D,TYPE,?>,
							LEVEL extends JeeslHdLevel<L,D,R,LEVEL,?>,
							PRIORITY extends JeeslHdPriority<L,D,R,PRIORITY,?>,
							USER extends JeeslSimpleUser>
			extends JeeslCoreQuery
{
	List<R> getRealm();
	List<USER> getReporters();
	List<STATUS> getStatus();
}