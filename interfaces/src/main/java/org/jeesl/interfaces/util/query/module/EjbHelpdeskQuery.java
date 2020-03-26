package org.jeesl.interfaces.util.query.module;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.module.hd.event.JeeslHdEvent;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEventType;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdLevel;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdPriority;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicket;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicketCategory;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicketStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.mcs.JeeslMcsRealm;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbHelpdeskQuery<L extends JeeslLang,D extends JeeslDescription,
							R extends JeeslMcsRealm<L,D,R,?>, RREF extends EjbWithId,
							TICKET extends JeeslHdTicket<R,EVENT,?>,
							CAT extends JeeslHdTicketCategory<L,D,R,CAT,?>,
							STATUS extends JeeslHdTicketStatus<L,D,R,STATUS,?>,
							EVENT extends JeeslHdEvent<TICKET,CAT,STATUS,TYPE,LEVEL,PRIORITY,USER>,
							TYPE extends JeeslHdEventType<L,D,TYPE,?>,
							LEVEL extends JeeslHdLevel<L,D,R,LEVEL,?>,
							PRIORITY extends JeeslHdPriority<L,D,R,PRIORITY,?>,
							USER extends JeeslSimpleUser>
			extends AbstractEjbQuery
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbHelpdeskQuery.class);
	

	private EjbHelpdeskQuery()
	{       
		reset();
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					R extends JeeslMcsRealm<L,D,R,?>, RREF extends EjbWithId,
					TICKET extends JeeslHdTicket<R,EVENT,?>,
					CAT extends JeeslHdTicketCategory<L,D,R,CAT,?>,
					STATUS extends JeeslHdTicketStatus<L,D,R,STATUS,?>,
					EVENT extends JeeslHdEvent<TICKET,CAT,STATUS,TYPE,LEVEL,PRIORITY,USER>,
					TYPE extends JeeslHdEventType<L,D,TYPE,?>,
					LEVEL extends JeeslHdLevel<L,D,R,LEVEL,?>,
					PRIORITY extends JeeslHdPriority<L,D,R,PRIORITY,?>,
					USER extends JeeslSimpleUser>
			EjbHelpdeskQuery<L,D,R,RREF,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER>
			build() {return new EjbHelpdeskQuery<>();}

	@Override public void reset()
	{
		realms=null;
		reporters=null;
		status=null;
	}
	
	private List<R> realms; public List<R> getRealm() {return realms;}
	public EjbHelpdeskQuery<L,D,R,RREF,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> add(R realm) {if(realms==null) {realms = new ArrayList<R>();} realms.add(realm); return this;}

	private List<USER> reporters; public List<USER> getReporters() {return reporters;}
	public EjbHelpdeskQuery<L,D,R,RREF,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> addReporter(USER user) {if(reporters==null) {reporters = new ArrayList<>();} reporters.add(user); return this;}
	
	private List<STATUS> status; public List<STATUS> getStatus() {return status;}
	public EjbHelpdeskQuery<L,D,R,RREF,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> addStatus(List<STATUS> list) {if(status==null) {status = new ArrayList<>();} this.status.addAll(list); return this;}
	public EjbHelpdeskQuery<L,D,R,RREF,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> nullStatus() {if(status==null) {status = new ArrayList<>();} status.clear(); return this;}
	
}