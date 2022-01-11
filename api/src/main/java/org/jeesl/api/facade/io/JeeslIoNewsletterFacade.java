package org.jeesl.api.facade.io;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterCategory;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterRecipient;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterRegistration;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterTopic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;

public interface JeeslIoNewsletterFacade <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>,
								CAT extends JeeslNewsletterCategory<L,D,R,CAT,?>,
								TOPIC extends JeeslNewsletterTopic<L,D,R,CAT>,
								RCP extends JeeslNewsletterRecipient<CAT,REGS,TOPIC>,
								REGS extends JeeslNewsletterRegistration<L,D,REGS,?>>
			extends JeeslFacade
{
	
}
