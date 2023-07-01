package org.jeesl.web.mbean.prototype.module.hd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslHdFacade;
import org.jeesl.doc.ofx.cms.generic.JeeslMarkupSectionFactory;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.HdFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
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
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.openfuxml.content.ofx.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractHelpdeskBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
								TICKET extends JeeslHdTicket<R,EVENT,M,FRC>,
								CAT extends JeeslHdCategory<L,D,R,CAT,?>,
								STATUS extends JeeslHdTicketStatus<L,D,R,STATUS,?>,
								EVENT extends JeeslHdEvent<TICKET,CAT,STATUS,TYPE,LEVEL,PRIORITY,USER>,
								TYPE extends JeeslHdEventType<L,D,TYPE,?>,
								LEVEL extends JeeslHdLevel<L,D,R,LEVEL,?>,
								PRIORITY extends JeeslHdPriority<L,D,R,PRIORITY,?>,
								MSG extends JeeslHdMessage<TICKET,M,SCOPE,USER>,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<L,D,MT,?>,
								FAQ extends JeeslHdFaq<L,D,R,CAT,SCOPE>,
								SCOPE extends JeeslHdScope<L,D,SCOPE,?>,
								FGA extends JeeslHdFga<FAQ,DOC,SEC>,
								DOC extends JeeslIoCms<L,D,LOC,?,SEC>,
								SEC extends JeeslIoCmsSection<L,SEC>,
								FRC extends JeeslFileContainer<?,?>,
								USER extends JeeslSimpleUser
								>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractHelpdeskBean.class);
	
	protected final HdFactoryBuilder<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,FRC,USER> fbHd;

	protected JeeslHdFacade<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,USER> fHd;
	
	protected final JeeslMarkupSectionFactory<M> ofxMarkup;
	
	protected final SbMultiHandler<STATUS> sbhStatus; public SbMultiHandler<STATUS> getSbhStatus() {return sbhStatus;}
	protected final SbMultiHandler<CAT> sbhCategory; public SbMultiHandler<CAT> getSbhCategory() {return sbhCategory;}
	protected final SbMultiHandler<SCOPE> sbhScope; public SbMultiHandler<SCOPE> getSbhScope() {return sbhScope;}
	
	protected JeeslFileRepositoryHandler<LOC,?,FRC,?> frh; public JeeslFileRepositoryHandler<LOC,?,FRC,?> getFrh() {return frh;}  protected void setFrh(JeeslFileRepositoryHandler<LOC,?,FRC,?> frh) {this.frh = frh;}	
	
	protected final List<LEVEL> levels; public List<LEVEL> getLevels() {return levels;}
	protected final List<PRIORITY> priorities; public List<PRIORITY> getPriorities() {return priorities;}
	
	protected final List<TICKET> tickets;  public List<TICKET> getTickets() {return tickets;}
	
	protected R realm;
	protected RREF rref;
	protected TICKET ticket; public TICKET getTicket() {return ticket;} public void setTicket(TICKET ticket) {this.ticket = ticket;}
	protected EVENT firstEvent; public EVENT getFirstEvent() {return firstEvent;} public void setFirstEvent(EVENT firstEvent) {this.firstEvent = firstEvent;}
	protected EVENT lastEvent; public EVENT getLastEvent() {return lastEvent;} public void setLastEvent(EVENT lastEvent) {this.lastEvent = lastEvent;}
	
	protected Section ofxUser; public Section getOfxUser() {return ofxUser;}
	
	public AbstractHelpdeskBean(HdFactoryBuilder<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,FRC,USER> fbHd)
	{
		super(fbHd.getClassL(),fbHd.getClassD());
		this.fbHd=fbHd;
		
		ofxMarkup = new JeeslMarkupSectionFactory<>();
		
		sbhStatus = new SbMultiHandler<>(fbHd.getClassTicketStatus(),this);
		sbhCategory = new SbMultiHandler<>(fbHd.getClassCategory(),this);
		sbhScope = new SbMultiHandler<>(fbHd.getClassScope(),this);
		
		levels = new ArrayList<>();
		priorities = new ArrayList<>();
		
		tickets = new ArrayList<>();
	}

	protected void postConstructHd(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslHdFacade<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,USER> fHd,
									R realm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fHd=fHd;
		this.realm=realm;
	}
	
	protected void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		
		sbhStatus.setList(fHd.all(fbHd.getClassTicketStatus(),realm,rref));
		
		sbhCategory.setList(fHd.all(fbHd.getClassCategory(),realm,rref));
		sbhCategory.selectAll();
		
		sbhScope.setList(fHd.all(fbHd.getClassScope()));
		sbhScope.selectAll();
		
		levels.addAll(fHd.all(fbHd.getClassLevel(),realm,rref));
		priorities.addAll(fHd.all(fbHd.getClassPriority(),realm,rref));
	
		updatedRealmReference();
	}
	protected abstract void updatedRealmReference();
	
	public void selectTicket() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.selectEntity(ticket));
		ticket = fHd.find(fbHd.getClassTicket(),ticket);
		firstEvent = fHd.find(fbHd.getClassEvent(),ticket.getFirstEvent());
		lastEvent = fHd.find(fbHd.getClassEvent(),ticket.getLastEvent());
		ofxUser = ofxMarkup.build(ticket.getMarkupUser());
		if(frh!=null) {frh.init(ticket);}
		selectedTicket();
	}
	protected abstract void selectedTicket();
}