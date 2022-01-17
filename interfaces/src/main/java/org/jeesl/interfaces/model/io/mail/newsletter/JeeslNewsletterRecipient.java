package org.jeesl.interfaces.model.io.mail.newsletter;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.system.status.JeeslWithCategory;

public interface JeeslNewsletterRecipient <C extends JeeslNewsletterCategory<?,?,?,C,?>,
											RS extends JeeslNewsletterRegistration<?,?,RS,?>,
											TOPIC extends JeeslNewsletterTopic<?,?,?,C>>
			extends Serializable,EjbSaveable,EjbRemoveable,
					JeeslSimpleUser,JeeslWithCategory<C> 
{
	public enum Attributes{recordCreated,recordConfirmed}
	
	RS getRegistration();
	void setRegistration(RS registration);
	
	//Next step for recipient management
	List<TOPIC> getSubscriptions();
	void setSubscriptions(List<TOPIC> subscriptions);
	
	
	// Will be added later
//	LocalDateTime getRecordCreated();
//	void setRecordCreated(LocalDateTime recordCreated);
//
//	LocalDateTime getRecordConfirmed();
//	void setRecordConfirmed(LocalDateTime recordConfirmed);
}