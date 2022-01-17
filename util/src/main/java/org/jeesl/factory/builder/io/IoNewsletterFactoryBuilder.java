package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.mail.newsletter.EjbIoNewsletterRecipientFactory;
import org.jeesl.factory.ejb.io.mail.newsletter.EjbIoNewsletterTopicFactory;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterCategory;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterRecipient;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterRegistration;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterTopic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoNewsletterFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								CAT extends JeeslNewsletterCategory<L,D,R,CAT,?>,
								TOPIC extends JeeslNewsletterTopic<L,D,R,CAT>,
								RCP extends JeeslNewsletterRecipient<CAT,REG,TOPIC>,
								REG extends JeeslNewsletterRegistration<L,D,REG,?>>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoNewsletterFactoryBuilder.class);
	
	private final Class<RCP> cRecipient;  public Class<RCP> getClassRecipient(){return cRecipient;}
	private final Class<CAT> cCategory; public Class<CAT> getClassCategory(){return cCategory;}
	private final Class<TOPIC> cTopic; public Class<TOPIC> getClassTopic(){return cTopic;}
	private final Class<REG> cRegistration; public Class<REG> getClassRegistration(){return cRegistration;}
	
	public IoNewsletterFactoryBuilder(final Class<L> cL, final Class<D> cD,
										final Class<RCP> cRecipient, 
										final Class<CAT> cCategory, 
										final Class<TOPIC> cTopic,
										final Class<REG> cRegistration
										)
	{
		super(cL,cD);
		this.cRecipient=cRecipient;
		this.cCategory=cCategory;
		this.cTopic=cTopic;
		this.cRegistration=cRegistration;
	}
	
	public EjbIoNewsletterRecipientFactory<CAT,RCP,REG,TOPIC> mail()
	{
		return new EjbIoNewsletterRecipientFactory<>(cRecipient);
	}
	
	public EjbIoNewsletterTopicFactory<R,TOPIC> ejbTopic(){return new EjbIoNewsletterTopicFactory<>(cTopic);}

}