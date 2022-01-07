package org.jeesl.factory.ejb.io.mail.newsletter;

import org.jeesl.interfaces.model.io.mail.newsletter.JeeslNewsletterTopic;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoNewsletterTopicFactory<R extends JeeslTenantRealm<?,?,R,?>,
										TOPIC extends JeeslNewsletterTopic<?,?,R,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoNewsletterTopicFactory.class);
	
	private final Class<TOPIC> cTopic;
	
	public EjbIoNewsletterTopicFactory(final Class<TOPIC> cTopic)
	{
		this.cTopic = cTopic;
	}
	
	public <RREF extends EjbWithId> TOPIC build(R realm, RREF rref)
	{
		try
		{
			TOPIC ejb = cTopic.newInstance();
			ejb.setRealm(realm);
			ejb.setRref(rref.getId());
			
			return ejb;
		}
		catch(InstantiationException | IllegalAccessException e) {e.printStackTrace();}
		return null;
	}
	
	public TOPIC build()
	{
		try 
		{
			TOPIC ejb = cTopic.newInstance();
			
			return ejb;
		} 
		catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
		return null;
	}
}
