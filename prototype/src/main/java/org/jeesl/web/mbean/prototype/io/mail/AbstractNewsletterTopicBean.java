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

public abstract class AbstractNewsletterTopicBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
								CAT extends JeeslNewsletterCategory<L,D,R,CAT,?>,
								TOPIC extends JeeslNewsletterTopic<L,D,R,CAT>
								>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractNewsletterTopicBean.class);
	
	protected final IoNewsletterFactoryBuilder<L,D,R,CAT,TOPIC,?,?> fbNewsletter;

	protected JeeslIoNewsletterFacade<L,D,LOC,R,CAT,TOPIC,?,?> fNewsletter;
	

	protected final SbSingleHandler<CAT> sbhCategory; public SbSingleHandler<CAT> getSbhCategory() {return sbhCategory;}
	
	protected final List<TOPIC> topics;  public List<TOPIC> getTopics() {return topics;}
	
	protected R realm;
	protected RREF rref;
	protected TOPIC topic; public TOPIC getTopic() {return topic;} public void setTopic(TOPIC topic) {this.topic = topic;}
	
	public AbstractNewsletterTopicBean(IoNewsletterFactoryBuilder<L,D,R,CAT,TOPIC,?,?> fbNewsletter)
	{
		super(fbNewsletter.getClassL(),fbNewsletter.getClassD());
		this.fbNewsletter=fbNewsletter;

		sbhCategory = new SbSingleHandler<>(fbNewsletter.getClassCategory(),this);
		topics = new ArrayList<>();
	}

	protected void postConstructNewsletter(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslIoNewsletterFacade<L,D,LOC,R,CAT,TOPIC,?,?> fNewsletter,
									R realm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fNewsletter=fNewsletter;
		this.realm=realm;
	}
	
	protected void updateRealmReference(RREF rref)
	{
		this.rref=rref;

		sbhCategory.setList(fNewsletter.all(fbNewsletter.getClassCategory(), realm, rref));
		sbhCategory.setDefault();
		
		updatedRealmReference();
	}
	protected void updatedRealmReference()
	{
		reloadTopics();
	}
	
	public void selectTopic() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.selectEntity(topic));
		topic = fNewsletter.find(fbNewsletter.getClassTopic(), topic);
		topic = efLang.persistMissingLangs(fNewsletter, lp, topic);
		topic = efDescription.persistMissingLangs(fNewsletter, lp.getLocales(), topic);
	}
	
	public void addTopic()
	{
		topic = fbNewsletter.ejbTopic().build(realm, rref);
		topic.setName(efLang.buildEmpty(lp.getLocales()));
		topic.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	
	public void saveTopic() throws JeeslConstraintViolationException, JeeslLockingException
	{
		topic = fNewsletter.save(topic);
		reloadTopics();
	}
	
	public void rmTopic() throws JeeslConstraintViolationException
	{
		fNewsletter.rm(topic);
		cancel();
		reloadTopics();
	}
	
	public void cancel()
	{
		topic = null;
	}
	
	public void reloadTopics()
	{
		topics.clear();
		// TODO: filter method in facade
		topics.addAll(fNewsletter.all(fbNewsletter.getClassTopic()).stream().filter(i->i.getCategory().equals(sbhCategory.getSelection())).collect(Collectors.toList()));
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item)
	{
		if(fbNewsletter.getClassCategory().isAssignableFrom(item.getClass()))
		{
			reloadTopics();
		}
	}
}