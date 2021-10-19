package org.jeesl.interfaces.model.io.mail.newsletter;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.system.status.JeeslWithCategory;

public interface JeeslNewsletterRecipient <C extends JeeslNewsletterCategory<?,?,?,C,?>,
											R extends JeeslNewsletterRegistration<?,?,R,?>>
			extends Serializable,EjbSaveable,EjbRemoveable,
					JeeslSimpleUser,JeeslWithCategory<C> 
{
	public enum Attributes{recordCreated,recordConfirmed}
	
	R getRegistration();
	void setRegistration(R registration);
	
//	LocalDateTime getRecordCreated();
//	void setRecordCreated(LocalDateTime recordCreated);
//
//	LocalDateTime getRecordConfirmed();
//	void setRecordConfirmed(LocalDateTime recordConfirmed);
	
}