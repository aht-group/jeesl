package org.jeesl.web.mbean.prototype.module.hd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.callback.JeeslFileRepositoryCallback;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoCmsFacade;
import org.jeesl.api.facade.module.JeeslHdFacade;
import org.jeesl.controller.handler.ui.edit.UiEditSavedHandler;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoCmsFactoryBuilder;
import org.jeesl.factory.builder.module.HdFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
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
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.module.EjbHelpdeskQuery;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractHdTicketBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
								TICKET extends JeeslHdTicket<R,EVENT,M,FRC>,
								CAT extends JeeslHdTicketCategory<L,D,R,CAT,?>,
								STATUS extends JeeslHdTicketStatus<L,D,R,STATUS,?>,
								EVENT extends JeeslHdEvent<TICKET,CAT,STATUS,TYPE,LEVEL,PRIORITY,USER>,
								TYPE extends JeeslHdEventType<L,D,TYPE,?>,
								LEVEL extends JeeslHdLevel<L,D,R,LEVEL,?>,
								PRIORITY extends JeeslHdPriority<L,D,R,PRIORITY,?>,
								MSG extends JeeslHdMessage<TICKET,M,SCOPE,USER>,
								M extends JeeslMarkup<MT>,
								MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
								FAQ extends JeeslHdFaq<L,D,R,CAT,SCOPE>,
								SCOPE extends JeeslHdScope<L,D,SCOPE,?>,
								FGA extends JeeslHdFga<FAQ,DOC,SEC>,
								DOC extends JeeslIoCms<L,D,LOC,?,SEC>,
								SEC extends JeeslIoCmsSection<L,SEC>,
								FRC extends JeeslFileContainer<?,?>,
								USER extends JeeslSimpleUser
								>
					extends AbstractHelpdeskBean<L,D,LOC,R,RREF,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,FRC,USER>
					implements Serializable,JeeslFileRepositoryCallback//,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractHdTicketBean.class);

	private JeeslIoCmsFacade<L,D,LOC,?,DOC,?,SEC,?,?,?,?,?,?,?> fCms;
	private final IoCmsFactoryBuilder<L,D,LOC,?,DOC,?,SEC,?,?,?,?,?,?,?> fbCms;
	
	private final UiEditSavedHandler<TICKET> editHandler; public UiEditSavedHandler<TICKET> getEditHandler() {return editHandler;}

	
	private final List<FAQ> faqs; public List<FAQ> getFaqs() {return faqs;}
	private final List<FGA> answers; public List<FGA> getAnswers() {return answers;}
	private final List<SEC> sections; public List<SEC> getSections() {return sections;}
	protected final List<USER> reporters; public List<USER> getReporters() {return reporters;}

	private USER reporter; public USER getReporter() {return reporter;} public void setReporter(USER reporter) {this.reporter = reporter;}
	private FAQ faq; public FAQ getFaq() {return faq;} public void setFaq(FAQ faq) {this.faq = faq;}
	private FGA fga; public FGA getFga() {return fga;} public void setFga(FGA fga) {this.fga = fga;}
	private CAT category; public CAT getCategory() {return category;} public void setCategory(CAT category) {this.category = category;}

	public AbstractHdTicketBean(HdFactoryBuilder<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,FRC,USER> fbHd,
			IoCmsFactoryBuilder<L,D,LOC,?,DOC,?,SEC,?,?,?,?,?,?,?> fbCms)
	{
		super(fbHd);
		this.fbCms=fbCms;
		this.editHandler = new UiEditSavedHandler<>();
		
		faqs = new ArrayList<>();
		answers = new ArrayList<>();
		sections = new ArrayList<>();
		reporters = new ArrayList<>();
	}

	protected void postConstructHdTicket(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslHdFacade<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,USER> fHd,
									JeeslIoCmsFacade<L,D,LOC,?,DOC,?,SEC,?,?,?,?,?,?,?> fCms,
									R realm,
									USER reporter)
	{
		super.postConstructHd(bTranslation,bMessage,fHd,realm);
		this.fCms=fCms;
		this.reporter=reporter;
	}

	@Override protected void updatedRealmReference()
	{
		reloadTickets();
	}

	@Override public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
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
		EjbHelpdeskQuery<L,D,R,RREF,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,USER> query = EjbHelpdeskQuery.build();
		query.addReporter(reporter);

		tickets.clear();
		tickets.addAll(fHd.fHdTickets(query));
	}

	private void reloadFaqs()
	{
		SCOPE scope = fHd.fByEnum(fbHd.getClassScope(),JeeslHdScope.Code.user);
		faqs.clear();
		faqs.addAll(fHd.allForParent(fbHd.getClassFaq(), JeeslHdFaq.Attributes.category.toString(),category, JeeslHdFaq.Attributes.scope.toString(),scope));
	}

	@Override
	public void selectedTicket()
	{
		lastEvent = fHd.find(fbHd.getClassEvent(),ticket.getLastEvent());
		this.editHandler.update(ticket);
	}

	public void addTicket() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info("addTicket......" + AbstractLogMessage.addEntity(fbHd.getClassTicket()));
		MT type = fHd.fByEnum(fbHd.getClassMarkupType(),JeeslIoCmsMarkupType.Code.xhtml);
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
			callBackNewTicket(ticket);
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
		faq = efLang.persistMissingLangs(fHd,bTranslation.getLocales(),faq);
		faq = efDescription.persistMissingLangs(fHd,bTranslation.getLocales(),faq);
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

	protected abstract void callBackNewTicket(TICKET ticket);

	@Override public void callbackFrContainerSaved(EjbWithId id) throws JeeslConstraintViolationException, JeeslLockingException{}
	@Override public void callbackFrMetaSelected() {}
}