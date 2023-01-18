package org.jeesl.interfaces.model.system.job.with;

import org.jeesl.interfaces.model.system.job.JeeslJobStatus;

public interface EjbWithMigrationJob2 <STATUS extends JeeslJobStatus<?,?,?,?>>
{
	public enum Tmp{job2}
	
	STATUS getJob2();
	void setJob2(STATUS job1);
}
