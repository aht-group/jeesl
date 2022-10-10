package org.jeesl.interfaces.model.system.job.feedback;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.job.JeeslJob;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;

public interface JeeslJobFeedback<JOB extends JeeslJob<?,?,?,?,USER>,
								FT extends JeeslJobFeedbackType<?,?,FT,?>,
							USER extends EjbWithEmail
							>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable
{
	JOB getJob();
	void setJob(JOB job);
	
	FT getType();
	void setType(FT type);
	
	USER getUser();
	void setUser(USER user);
}