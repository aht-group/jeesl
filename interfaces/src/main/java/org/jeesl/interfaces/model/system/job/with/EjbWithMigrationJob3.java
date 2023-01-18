package org.jeesl.interfaces.model.system.job.with;

import org.jeesl.interfaces.model.system.job.JeeslJobStatus;

public interface EjbWithMigrationJob3 <STATUS extends JeeslJobStatus<?,?,?,?>>
{
	public enum Tmp{job3}
	
	STATUS getJob3();
	void setJob3(STATUS job1);
}
