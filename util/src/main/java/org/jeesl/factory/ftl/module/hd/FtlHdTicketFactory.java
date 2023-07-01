package org.jeesl.factory.ftl.module.hd;

import java.util.HashMap;
import java.util.Map;

import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.hd.JeeslHdCategory;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEvent;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEventType;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdFaq;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdFga;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdLevel;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdMessage;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdPriority;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdScope;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicket;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicketStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtlHdTicketFactory <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									TICKET extends JeeslHdTicket<?,EVENT,M,FRC>,
									CAT extends JeeslHdCategory<L,D,?,CAT,?>,
									STATUS extends JeeslHdTicketStatus<L,D,?,STATUS,?>,
									EVENT extends JeeslHdEvent<TICKET,CAT,STATUS,TYPE,LEVEL,PRIORITY,USER>,
									TYPE extends JeeslHdEventType<L,D,TYPE,?>,
									LEVEL extends JeeslHdLevel<L,D,?,LEVEL,?>,
									PRIORITY extends JeeslHdPriority<L,D,?,PRIORITY,?>,
									MSG extends JeeslHdMessage<TICKET,M,SCOPE,USER>,
									M extends JeeslIoMarkup<MT>,
									MT extends JeeslIoMarkupType<L,D,MT,?>,
									FAQ extends JeeslHdFaq<L,D,?,CAT,SCOPE>,
									SCOPE extends JeeslHdScope<L,D,SCOPE,?>,
									FGA extends JeeslHdFga<FAQ,DOC,SEC>,
									DOC extends JeeslIoCms<L,D,LOC,?,SEC>,
									SEC extends JeeslIoCmsSection<L,SEC>,
									FRC extends JeeslFileContainer<?,?>,
									USER extends JeeslSimpleUser
									>
{
	final static Logger logger = LoggerFactory.getLogger(FtlHdTicketFactory.class);
		
	public Map<String,Object> build(String localeCode, TICKET ticket)
	{		
		Map<String,Object> model = new HashMap<String,Object>();
	
		model.put("hdRequester",ticket.getFirstEvent().getInitiator().getFirstName()+" "+ticket.getFirstEvent().getInitiator().getLastName());
		model.put("hdCategory",ticket.getLastEvent().getCategory().getName().get(localeCode).getLang());
		model.put("hdTopic",ticket.getName());
		
		return model;
	}
}