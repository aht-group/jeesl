package org.jeesl.controller.web.g.module.hd;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.callback.JeeslFileRepositoryCallback;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoCmsFacade;
import org.jeesl.api.facade.module.JeeslHdFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.doc.ofx.cms.generic.JeeslMarkupSectionFactory;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoCmsFactoryBuilder;
import org.jeesl.factory.builder.module.HdFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
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
import org.jeesl.jsf.handler.ui.UiSlotWidthHandler;
import org.jeesl.jsf.handler.ui.edit.UiEditSavedHandler;
import org.jeesl.util.query.ejb.module.EjbHelpdeskQuery;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.model.xml.core.ofx.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslHdTicketGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements SbToggleBean,JeeslFileRepositoryCallback
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslHdTicketGwc.class);

	protected JeeslHdFacade<L,D,R,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,USER> fHd;
	private JeeslIoCmsFacade<L,D,LOC,?,DOC,?,SEC,?,?,?,?,M,MT,?,?> fCms;
	
	protected final HdFactoryBuilder<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,FRC,USER> fbHd;
	private final IoCmsFactoryBuilder<L,D,LOC,?,DOC,?,SEC,?,?,?,?,M,MT,?,?> fbCms;
	
	protected final JeeslMarkupSectionFactory<M> ofxMarkup;
	
	protected final SbMultiHandler<STATUS> sbhStatus; public SbMultiHandler<STATUS> getSbhStatus() {return sbhStatus;}
	protected final SbMultiHandler<CAT> sbhCategory; public SbMultiHandler<CAT> getSbhCategory() {return sbhCategory;}
	protected final SbMultiHandler<SCOPE> sbhScope; public SbMultiHandler<SCOPE> getSbhScope() {return sbhScope;}
	
	private final UiSlotWidthHandler uiSlot; public UiSlotWidthHandler getUiSlot() {return uiSlot;}
	private final UiEditSavedHandler<TICKET> editHandler; public UiEditSavedHandler<TICKET> getEditHandler() {return editHandler;}
	private JeeslFileRepositoryHandler<LOC,?,FRC,?> frh; public JeeslFileRepositoryHandler<LOC,?,FRC,?> getFrh() {return frh;}  public void setFrh(JeeslFileRepositoryHandler<LOC,?,FRC,?> frh) {this.frh = frh;}	

	
	protected final List<LEVEL> levels; public List<LEVEL> getLevels() {return levels;}
	protected final List<PRIORITY> priorities; public List<PRIORITY> getPriorities() {return priorities;}
	protected final List<TICKET> tickets;  public List<TICKET> getTickets() {return tickets;}
	private final List<FAQ> faqs; public List<FAQ> getFaqs() {return faqs;}
	private final List<FGA> answers; public List<FGA> getAnswers() {return answers;}
	private final List<SEC> sections; public List<SEC> getSections() {return sections;}
	protected final List<USER> reporters; public List<USER> getReporters() {return reporters;}

	private R realm;
	private RREF rref;
	protected TICKET ticket; public TICKET getTicket() {return ticket;} public void setTicket(TICKET ticket) {this.ticket = ticket;}
	protected EVENT firstEvent; public EVENT getFirstEvent() {return firstEvent;} public void setFirstEvent(EVENT firstEvent) {this.firstEvent = firstEvent;}
	protected EVENT lastEvent; public EVENT getLastEvent() {return lastEvent;} public void setLastEvent(EVENT lastEvent) {this.lastEvent = lastEvent;}
	private USER reporter; public USER getReporter() {return reporter;} public void setReporter(USER reporter) {this.reporter = reporter;}
	private FAQ faq; public FAQ getFaq() {return faq;} public void setFaq(FAQ faq) {this.faq = faq;}
	private FGA fga; public FGA getFga() {return fga;} public void setFga(FGA fga) {this.fga = fga;}
	private CAT category; public CAT getCategory() {return category;} public void setCategory(CAT category) {this.category = category;}
	protected Section ofxUser; public Section getOfxUser() {return ofxUser;}
	
	public JeeslHdTicketGwc(HdFactoryBuilder<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,FRC,USER> fbHd,
								IoCmsFactoryBuilder<L,D,LOC,?,DOC,?,SEC,?,?,?,?,M,MT,?,?> fbCms)
	{
		super(fbHd.getClassL(),fbHd.getClassD());
		this.fbHd=fbHd;
		this.fbCms=fbCms;
		
		this.editHandler = new UiEditSavedHandler<>();
		uiSlot = new UiSlotWidthHandler();
		
		ofxMarkup = new JeeslMarkupSectionFactory<>();
		
		sbhStatus = new SbMultiHandler<>(fbHd.getClassTicketStatus(),this);
		sbhCategory = new SbMultiHandler<>(fbHd.getClassCategory(),this);
		sbhScope = new SbMultiHandler<>(fbHd.getClassScope(),this);
		
		levels = new ArrayList<>();
		priorities = new ArrayList<>();
		tickets = new ArrayList<>();
		faqs = new ArrayList<>();
		answers = new ArrayList<>();
		sections = new ArrayList<>();
		reporters = new ArrayList<>();
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									JeeslHdFacade<L,D,R,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,USER> fHd,
									JeeslIoCmsFacade<L,D,LOC,?,DOC,?,SEC,?,?,?,?,M,MT,?,?> fCms,
									R realm,
									USER reporter)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fHd=fHd;
		this.fCms=fCms;
		this.realm=realm;
	}

	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		
		sbhStatus.setList(fHd.all(fbHd.getClassTicketStatus(),realm,rref));
		
		sbhCategory.setList(fHd.all(fbHd.getClassCategory(),realm,rref));
		sbhCategory.selectAll();
		
		sbhScope.setList(fHd.all(fbHd.getClassScope()));
		sbhScope.selectAll();
		
		levels.addAll(fHd.all(fbHd.getClassLevel(),realm,rref));
		priorities.addAll(fHd.all(fbHd.getClassPriority(),realm,rref));
		
		this.reloadTickets();
	}

	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
	}
	
	private void reset(boolean rFaq, boolean rAnswers, boolean rSections)
	{
		if(rFaq) {faq=null;}
		if(rAnswers) {answers.clear();}
		if(rSections) {sections.clear();}
	}

	private void reloadTickets()
	{
		EjbHelpdeskQuery<L,D,R,RREF,CAT,TICKET,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> query = EjbHelpdeskQuery.build();
		query.addReporter(reporter);

		tickets.clear();
		tickets.addAll(fHd.fHdTickets(query));
	}

	private void reloadFaqs()
	{
		SCOPE scope = fHd.fByEnum(fbHd.getClassScope(),JeeslHdScope.Code.user);
		faqs.clear();
		faqs.addAll(fHd.allForParent(fbHd.getClassFaq(), JeeslHdFaq.Attributes.category,category, JeeslHdFaq.Attributes.scope,scope));
	}

	public void selectTicket(TICKET t) throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.selectEntity(ticket));
		ticket = fHd.find(fbHd.getClassTicket(),ticket);
		firstEvent = fHd.find(fbHd.getClassEvent(),ticket.getFirstEvent());
		lastEvent = fHd.find(fbHd.getClassEvent(),ticket.getLastEvent());
		ofxUser = ofxMarkup.build(ticket.getMarkupUser());
		if(frh!=null) {frh.init(ticket);}
		this.ticket=t;
		uiSlot.set(0,5,7);
	}

	public void addTicket() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info("addTicket......" + AbstractLogMessage.createEntity(fbHd.getClassTicket()));
		MT type = fHd.fByEnum(fbHd.getClassMarkupType(),JeeslIoMarkupType.Code.xhtml);
		ticket = fbHd.ejbTicket().build(realm,rref,type);
		PRIORITY priority = getDefaultPriority();
		firstEvent = fbHd.ejbEvent().build(ticket,sbhCategory.getList().get(0),sbhStatus.getList().get(0),levels.get(0),priority,reporter);
		lastEvent = fbHd.ejbEvent().build(ticket,sbhCategory.getList().get(0),sbhStatus.getList().get(0),levels.get(0),priority,reporter);
		category = sbhCategory.getList().get(0);
		reloadFaqs();
		this.editHandler.update(ticket);
		this.editHandler.setVisible(false);
		ofxUser = XmlSectionFactory.build();
		if(frh!=null) {frh.init(ticket);}
	}
	private PRIORITY getDefaultPriority()
	{
		for(PRIORITY p : priorities)
		{
			if(p.getCode().equals("medium")) {return p;}
		}
		return priorities.get(0);
	}

	public void saveTicket() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		fbHd.ejbEvent().converter(fHd,lastEvent);
		if(EjbIdFactory.isUnSaved(ticket))
		{
			lastEvent.setInitiator(reporter);
			lastEvent.setReporter(reporter);
			ticket = fHd.saveHdTicket(ticket,lastEvent,reporter);
//			callBackNewTicket(ticket);
		}
		else {ticket = fHd.save(ticket);}
		this.editHandler.saved(ticket);
		ofxUser = ofxMarkup.build(ticket.getMarkupUser());
		if(frh!=null)
		{
			frh.saveDeferred(ticket);
			ticket.setFrContainer(frh.getContainer());
			ticket = fHd.save(ticket);
		}
		reloadTickets();
	}

	public void disableTicket() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info("disabling ticket");
		toggleTicket("discarded");
	}

	public void enableTicket() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info("enabling ticket");
		toggleTicket("open");
	}

	public void toggleTicket(String statusCode) throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		STATUS status = fHd.fByCode(fbHd.getClassTicketStatus(),realm,rref,statusCode);
		//update status
		fbHd.ejbEvent().converter(fHd,lastEvent);
		ticket.setLastEvent(lastEvent);
		ticket.getLastEvent().setStatus(status);

		logger.info("saving hd ticket");
		ticket = fHd.saveHdTicket(ticket,lastEvent,reporter);
		logger.info("saving hd finished");


		//ticket = fHd.save(ticket);
		this.editHandler.saved(ticket);
		ofxUser = ofxMarkup.build(ticket.getMarkupUser());
		if(frh!=null)
		{
			frh.saveDeferred(ticket);
			ticket.setFrContainer(frh.getContainer());
			ticket = fHd.save(ticket);
		}
		reloadTickets();
	}


	public boolean showDisableButton()
	{
		if(ticket!=null && ticket.getId()>0)
		{
			logger.info (ticket.getLastEvent().getStatus().getCode());
			if(!ticket.getLastEvent().getStatus().getCode().equals("discarded"))
			{
				return true;
			}
			return false;
		}
		return false;
	}


	public void handleCategoryChange()
	{
		logger.info(AbstractLogMessage.selectEntity(category));
		reset(true,true,true);
		category =  this.getLastEvent().getCategory();
		editHandler.setVisible(false);
		reloadFaqs();
	}

	public void faqNotFound()
	{
		logger.info("Faq not found...... setting visible.....");
		this.editHandler.setVisible(true);
	}

	public void selectFaq()
	{
		fga=null;
		sections.clear();
		logger.info(AbstractLogMessage.selectEntity(faq));
		faq = efLang.persistMissingLangs(fHd,lp.getLocales(),faq);
		faq = efDescription.persistMissingLangs(fHd,lp.getLocales(),faq);
		reloadAnswers();
	}

	private void reloadAnswers()
	{
		answers.clear();
		answers.addAll(fHd.allForParent(fbHd.getClassFga(), faq));
	}
	
	public void selectAnswer()
	{
		logger.info(AbstractLogMessage.selectEntity(fga));
		sections.clear();
		reloadSections();
	}

	public void reloadSections()
	{
		SEC root = fCms.load(fga.getDocument().getRoot(),true);
		sections.addAll(fbCms.ejbSection().toSections(root));
	}

	@Override public void callbackFrContainerSaved(EjbWithId id) throws JeeslConstraintViolationException, JeeslLockingException{}
	@Override public void callbackFrMetaSelected() {}
}