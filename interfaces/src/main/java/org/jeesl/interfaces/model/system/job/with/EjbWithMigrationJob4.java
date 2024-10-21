package org.jeesl.interfaces.model.system.job.with;

import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;

public interface EjbWithMigrationJob4 <STATUS extends JeeslJobStatus<?,?,?,?>>
{
	public enum Attributes{job4}
	
	STATUS getJob4();
	void setJob4(STATUS job1);
}
