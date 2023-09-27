package org.jeesl.interfaces.util.query.module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbHelpdeskQuery<L extends JeeslLang,D extends JeeslDescription,
							R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
							CAT extends JeeslHdCategory<L,D,R,CAT,?>,
							TICKET extends JeeslHdTicket<R,EVENT,?,?>,
							
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
					R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
					CAT extends JeeslHdCategory<L,D,R,CAT,?>,
					TICKET extends JeeslHdTicket<R,EVENT,?,?>,
					
					STATUS extends JeeslHdTicketStatus<L,D,R,STATUS,?>,
					EVENT extends JeeslHdEvent<TICKET,CAT,STATUS,TYPE,LEVEL,PRIORITY,USER>,
					TYPE extends JeeslHdEventType<L,D,TYPE,?>,
					LEVEL extends JeeslHdLevel<L,D,R,LEVEL,?>,
					PRIORITY extends JeeslHdPriority<L,D,R,PRIORITY,?>,
					USER extends JeeslSimpleUser>
			EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER>
			build() {return new EjbHelpdeskQuery<>();}

	@Override public void reset()
	{
		realms=null;
		reporters=null;
		status=null;
	}
	
	//Fetches
	public <E extends Enum<E>> EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//ID-List
	@Override public EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> ld1(LocalDate ld1) {this.ld1 = ld1; return this;}
	public EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> ld2(LocalDate ld2) {this.ld2 = ld2; return this;}
	public EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	private List<R> realms; public List<R> getRealm() {return realms;}
	public EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> add(R realm) {if(realms==null) {realms = new ArrayList<R>();} realms.add(realm); return this;}

	private List<USER> reporters; public List<USER> getReporters() {return reporters;}
	public EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> addReporter(USER user) {if(reporters==null) {reporters = new ArrayList<>();} reporters.add(user); return this;}
	
	private List<STATUS> status; public List<STATUS> getStatus() {return status;}
	public EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> addStatus(List<STATUS> list) {if(status==null) {status = new ArrayList<>();} this.status.addAll(list); return this;}
	public EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> nullStatus() {if(status==null) {status = new ArrayList<>();} status.clear(); return this;}
}