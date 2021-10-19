package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.mail.newsletter.EjbIoNewsletterRecipientFactory;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterCategory;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterRecipient;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterRegistration;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoNewsletterFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								CAT extends JeeslNewsletterCategory<L,D,R,CAT,?>,
								RCP extends JeeslNewsletterRecipient<CAT,REG>,
								REG extends JeeslNewsletterRegistration<L,D,REG,?>>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoNewsletterFactoryBuilder.class);
	
	private final Class<RCP> cRecipient;  public Class<RCP> getClassRecipient(){return cRecipient;}
	
	public IoNewsletterFactoryBuilder(final Class<L> cL, final Class<D> cD,
										final Class<RCP> cRecipient
										
									)
	{
		super(cL,cD);
		this.cRecipient=cRecipient;
	}
	
	public EjbIoNewsletterRecipientFactory<CAT,RCP,REG> mail()
	{
		return new EjbIoNewsletterRecipientFactory<>(cRecipient);
	}

}