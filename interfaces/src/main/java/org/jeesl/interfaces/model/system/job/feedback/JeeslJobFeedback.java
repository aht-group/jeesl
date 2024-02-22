package org.jeesl.interfaces.model.system.job.feedback;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslJobFeedback<FT extends JeeslJobFeedbackType<?,?,FT,?>,
								USER extends JeeslSimpleUser
							>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable
{
	FT getType();
	void setType(FT type);
	
	USER getUser();
	void setUser(USER user);
}