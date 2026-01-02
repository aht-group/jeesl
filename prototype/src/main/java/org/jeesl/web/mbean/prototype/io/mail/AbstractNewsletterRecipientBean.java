package org.jeesl.web.mbean.prototype.io.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoNewsletterFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoNewsletterFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterCategory;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterRecipient;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterRegistration;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterTopic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNewsletterRecipientBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
														R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
														CAT extends JeeslNewsletterCategory<L,D,R,CAT,?>,
														RS extends JeeslNewsletterRegistration<L,D,RS,?>,
														TOPIC extends JeeslNewsletterTopic<L,D,R,CAT>,
														RCP extends JeeslNewsletterRecipient<CAT,RS,TOPIC>
														>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbSingleBean 
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractNewsletterRecipientBean.class);
	
	protected final IoNewsletterFactoryBuilder<L,D,R,CAT,TOPIC,RCP,RS> fbNewsletter;

	protected JeeslIoNewsletterFacade<L,D,LOC,R,CAT,TOPIC,RCP,RS> fNewsletter;
	
	protected final SbSingleHandler<CAT> sbhCategory; public SbSingleHandler<CAT> getSbhCategory() {return sbhCategory;}
	
	protected final List<RCP> recipients; public List<RCP> getRecipients(){return recipients;}
	protected final List<RS> registrations; public List<RS> getRegistrations(){return registrations;}
	protected final List<TOPIC> topics; public List<TOPIC> getTopics(){return topics;}
	
	protected R realm;
	protected RREF rref;
	protected RCP recipient; public RCP getRecipient() {return recipient;} public void setRecipient(RCP recipient) {this.recipient=recipient;}
	protected TOPIC tblTopic; public TOPIC getTblTopic() {return tblTopic;} public void setTblTopic(TOPIC tblTopic) {this.tblTopic=tblTopic;}
	protected TOPIC opTopic; public TOPIC getOpTopic() {return opTopic;} public void setOpTopic(TOPIC opTopic) {this.opTopic=opTopic;}
	
	public AbstractNewsletterRecipientBean(IoNewsletterFactoryBuilder<L,D,R,CAT,TOPIC,RCP,RS> fbNewsletter)
	{
		super(fbNewsletter.getClassL(),fbNewsletter.getClassD());
		this.fbNewsletter=fbNewsletter;

		sbhCategory = new SbSingleHandler<>(fbNewsletter.getClassCategory(),this);
		recipients = new ArrayList<>();
		registrations = new ArrayList<>();
		topics = new ArrayList<>();
	}
	
	protected void postConstructNewsletter(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslIoNewsletterFacade<L,D,LOC,R,CAT,TOPIC,RCP,RS> fNewsletter,
									R realm)
	{
		super.initJeeslAdmin(bTranslation, bMessage);
		this.fNewsletter=fNewsletter;
		this.realm=realm;
	}
	
	protected void updateRealmReference(RREF rref)
	{
		this.rref=rref;

		sbhCategory.setList(fNewsletter.all(fbNewsletter.getClassCategory(), realm, rref));
		sbhCategory.setDefault();
		registrations.addAll(fNewsletter.all(fbNewsletter.getClassRegistration()));
		
		updatedRealmReference();
	}
	
	protected void updatedRealmReference()
	{
		reloadRecipients();
		topics.addAll(fNewsletter.all(fbNewsletter.getClassTopic(), realm, rref));
	}
	
	public void selectRecipient()
	{
		logger.info(AbstractLogMessage.selectEntity(recipient));
		recipient = fNewsletter.find(fbNewsletter.getClassRecipient(), recipient);
		recipient = fNewsletter.load(fbNewsletter.getClassRecipient(), recipient);
	}
	
	public void addRecipient()
	{
		recipient = fbNewsletter.mail().build();
	}
	
	public void saveRecipient() throws JeeslConstraintViolationException, JeeslLockingException
	{
		recipient = fNewsletter.save(recipient);
		reloadRecipients();
		selectRecipient();
	}
	
	public void rmRecipient() throws JeeslConstraintViolationException
	{
		fNewsletter.rm(recipient);
		cancel();
		reloadRecipients();
	}
	
	public void selectTopic()
	{
		logger.info(AbstractLogMessage.selectEntity(tblTopic));
	}
	
	public void addTopic() throws JeeslConstraintViolationException, JeeslLockingException 
	{
		if(!recipient.getSubscriptions().contains(opTopic))
		{
			recipient.getSubscriptions().add(opTopic);
			recipient = fNewsletter.save(recipient);
			opTopic = null;
			selectRecipient();
		}
	}
	
	public void rmTopic() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(tblTopic!=null && recipient.getSubscriptions().contains(tblTopic))
		{
			recipient.getSubscriptions().remove(tblTopic);
			recipient = fNewsletter.save(recipient);
			tblTopic = null;
			selectRecipient();
		}
	}
	
	public void cancel()
	{
		recipient = null;
	}
	
	public void reloadRecipients()
	{
		recipients.clear();
		recipients.addAll(fNewsletter.all(fbNewsletter.getClassRecipient()).stream().filter(i->i.getCategory().equals(sbhCategory.getSelection())).collect(Collectors.toList()));
	}
	
	@Override
	public void selectSbSingle(SbSingleSelection handler, EjbWithId item)
	{
		if(fbNewsletter.getClassCategory().isAssignableFrom(item.getClass()))
		{
			reloadRecipients();
		}
	}
}
