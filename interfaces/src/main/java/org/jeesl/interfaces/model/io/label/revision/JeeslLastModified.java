package org.jeesl.interfaces.model.io.label.revision;

import java.time.LocalDateTime;

import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslLastModified <USER extends JeeslSimpleUser> extends EjbWithId
{
	USER getLastModifiedBy();
	void setLastModifiedBy(USER lastModifiedBy);
	
	LocalDateTime getLastModifiedAt();
	void setLastModifiedAt(LocalDateTime lastModifiedAt);
}
