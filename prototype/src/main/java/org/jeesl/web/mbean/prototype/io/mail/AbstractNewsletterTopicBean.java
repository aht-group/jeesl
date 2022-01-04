package org.jeesl.web.mbean.prototype.io.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslHdFacade;
import org.jeesl.controller.handler.sb.SbSingleHandler;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.HdFactoryBuilder;
import org.jeesl.interfaces.bean.sb.SbSingleBean;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterCategory;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterTopic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.primefaces.push.Status.STATUS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

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
	
//	protected final HdFactoryBuilder<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,FRC,USER> fbHd;

//	protected JeeslHdFacade<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,USER> fHd;
	
	//TODO final
	protected SbSingleHandler<CAT> sbhCategory; public SbSingleHandler<CAT> getSbhCategory() {return sbhCategory;}
	
	protected final List<TOPIC> topics;  public List<TOPIC> getTopics() {return topics;}
	
	protected R realm;
	protected RREF rref;
	protected TOPIC topic; public TOPIC getTopic() {return topic;} public void setTopic(TOPIC topic) {this.topic = topic;}

	public AbstractNewsletterTopicBean(
//			HdFactoryBuilder<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,FRC,USER> fbHd
			)
	{
		super(null,null); //TOOD Create own NewsletterFactoryBuilder
//		super(fbHd.getClassL(),fbHd.getClassD());
//		this.fbHd=fbHd;
		

//		sbhCategory = new SbSingleHandler<>(fbHd.getClassCategory(),this);
		topics = new ArrayList<>();
	}

	protected void postConstructNewsletter(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
//									JeeslHdFacade<L,D,LOC,R,TICKET,CAT,STATUS,EVENT,TYPE,LEVEL,PRIORITY,MSG,M,MT,FAQ,SCOPE,FGA,DOC,SEC,USER> fHd,
									R realm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
//		this.fHd=fHd;
		//Create own JeeslNewsletterFacade
		this.realm=realm;
	}
	
	protected void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		
//		sbhCategory.setList(fHd.all(fbHd.getClassCategory(),realm,rref)); /later
//		sbhCategory.setList(fHd.all(fbHd.getClassCategory()));
		sbhCategory.setDefault();
		
		updatedRealmReference();
	}
	protected abstract void updatedRealmReference();
	
	public void selectTopic() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.selectEntity(topic));
//		ticket = fHd.find(fbHd.getClassTicket(),ticket);
	}
}