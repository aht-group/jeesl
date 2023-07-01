package org.jeesl.factory.ejb.module.hd;

import java.util.Date;

import org.jeesl.factory.builder.module.HdFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.hd.JeeslHdCategory;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEvent;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEventType;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdLevel;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdPriority;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicket;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicketStatus;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbHdEventFactory<TICKET extends JeeslHdTicket<?,EVENT,?,?>,
								CAT extends JeeslHdCategory<?,?,?,CAT,?>,
								STATUS extends JeeslHdTicketStatus<?,?,?,STATUS,?>,
								EVENT extends JeeslHdEvent<TICKET,CAT,STATUS,TYPE,LEVEL,PRIORITY,USER>,
								TYPE extends JeeslHdEventType<?,?,TYPE,?>,
								LEVEL extends JeeslHdLevel<?,?,?,LEVEL,?>,
								PRIORITY extends JeeslHdPriority<?,?,?,PRIORITY,?>,
								USER extends JeeslSimpleUser>
{
	final static Logger logger = LoggerFactory.getLogger(EjbHdEventFactory.class);
	
	private final HdFactoryBuilder<?,?,?,?,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,?,?,?,?,?,?,?,?,?,USER> fbHd;
	
    public EjbHdEventFactory(HdFactoryBuilder<?,?,?,?,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,?,?,?,?,?,?,?,?,?,USER> fbHd)
    {
        this.fbHd = fbHd;
    }
	
	public EVENT build(TICKET ticket, CAT category, STATUS status, LEVEL level, PRIORITY priority, USER reporter)
	{
		try
		{
			EVENT ejb = fbHd.getClassEvent().newInstance();
			ejb.setTicket(ticket);
			ejb.setCategory(category);
			ejb.setStatus(status);
			ejb.setLevel(level);
			ejb.setRecord(new Date());
			ejb.setReporter(reporter);
			ejb.setInitiator(reporter);
			ejb.setReporterPriority(priority);
			ejb.setSupporterPriority(priority);
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
	
	public void converter(JeeslFacade facade, EVENT event)
	{
		if(event.getCategory()!=null) {event.setCategory(facade.find(fbHd.getClassCategory(),event.getCategory()));}
		if(event.getStatus()!=null) {event.setStatus(facade.find(fbHd.getClassTicketStatus(),event.getStatus()));}
		if(event.getLevel()!=null) {event.setLevel(facade.find(fbHd.getClassLevel(),event.getLevel()));}
		if(event.getSupporter()!=null) {event.setSupporter(facade.find(fbHd.getClassUser(),event.getSupporter()));}
		if(event.getReporterPriority()!=null) {event.setReporterPriority(facade.find(fbHd.getClassPriority(),event.getReporterPriority()));}
		if(event.getSupporterPriority()!=null) {event.setSupporterPriority(facade.find(fbHd.getClassPriority(),event.getSupporterPriority()));}
	}
}