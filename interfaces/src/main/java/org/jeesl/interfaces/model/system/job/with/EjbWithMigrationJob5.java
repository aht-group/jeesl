package org.jeesl.interfaces.model.system.job.with;

import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;

public interface EjbWithMigrationJob5 <STATUS extends JeeslJobStatus<?,?,?,?>>
{
	public enum Attributes{job5}
	
	STATUS getJob5();
	void setJob5(STATUS job5);
}
