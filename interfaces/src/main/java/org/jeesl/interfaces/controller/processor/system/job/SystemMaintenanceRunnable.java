package org.jeesl.interfaces.controller.processor.system.job;

import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenance;

public interface SystemMaintenanceRunnable <M extends JeeslJobMaintenance<?,?,?,?>> extends Runnable
{
	void shutdown();
	
	void task(M task);
}