package org.jeesl.controller.facade.io;

import javax.persistence.EntityManager;

import org.jeesl.api.facade.io.JeeslIoNewsletterFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.factory.builder.io.IoNewsletterFactoryBuilder;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterCategory;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterRecipient;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterRegistration;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterTopic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoNewsletterFacadeBean<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										R extends JeeslTenantRealm<L,D,R,?>,
										CAT extends JeeslNewsletterCategory<L,D,R,CAT,?>,
										TOPIC extends JeeslNewsletterTopic<L,D,R,CAT>,
										RCP extends JeeslNewsletterRecipient<CAT,REGS,TOPIC>,
										REGS extends JeeslNewsletterRegistration<L,D,REGS,?>> 
				extends JeeslFacadeBean 
				implements JeeslIoNewsletterFacade<L,D,LOC,R,CAT,TOPIC,RCP,REGS>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(JeeslIoNewsletterFacadeBean.class);
	
	private final IoNewsletterFactoryBuilder<L,D,R,CAT,TOPIC,RCP,REGS> fbNewsletter;
	
	public JeeslIoNewsletterFacadeBean(EntityManager em, final IoNewsletterFactoryBuilder<L,D,R,CAT,TOPIC,RCP,REGS> fbNewsletter)
	{
		super(em);
		this.fbNewsletter=fbNewsletter;
	}
}
