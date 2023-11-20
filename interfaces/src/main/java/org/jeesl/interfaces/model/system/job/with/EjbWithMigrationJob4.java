package org.jeesl.interfaces.model.system.job.with;

import org.jeesl.interfaces.model.system.job.JeeslJobStatus;

public interface EjbWithMigrationJob4 <STATUS extends JeeslJobStatus<?,?,?,?>>
{
	public enum Attributes{job4}
	
	STATUS getJob3();
	void setJob3(STATUS job1);
}
