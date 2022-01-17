package org.jeesl.factory.ejb.io.mail.newsletter;

import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterCategory;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterRecipient;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterRegistration;
import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoNewsletterRecipientFactory <CAT extends JeeslNewsletterCategory<?,?,?,CAT,?>,
												RCP extends JeeslNewsletterRecipient<CAT,REG,TOPIC>,
												REG extends JeeslNewsletterRegistration<?,?,REG,?>,
												TOPIC extends JeeslNewsletterTopic<?,?,?,CAT>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoNewsletterRecipientFactory.class);
	
	private final Class<RCP> cRecipient;

	public EjbIoNewsletterRecipientFactory(final Class<RCP> cRecipient)
	{
        this.cRecipient = cRecipient;
	}
 
	public RCP build(CAT category, REG registration)
	{
		RCP ejb = null;
		try
		{
			ejb = cRecipient.newInstance();
			ejb.setCategory(category);
			ejb.setRegistration(registration);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public RCP build()
	{
		RCP ejb = null;
		
		try 
		{
			ejb = cRecipient.newInstance();
		} 
		catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}