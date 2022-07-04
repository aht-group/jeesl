package org.jeesl.interfaces.model.io.crypto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslIoCryptoKey<USER extends JeeslSimpleUser>
						extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable
								
{	
	public enum Attributes{user}
		
	USER getUser();
	void setUser(USER user);
	
	LocalDateTime getRecord();
	void setRecord(LocalDateTime record);
}