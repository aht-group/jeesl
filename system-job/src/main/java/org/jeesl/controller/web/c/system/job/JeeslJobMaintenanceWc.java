package org.jeesl.controller.web.c.system.job;

import org.jeesl.controller.web.system.job.JeeslJobMaintenanceController;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.interfaces.controller.processor.system.job.JeeslJobMaitenanceTupler;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.system.job.cache.SystemJobCache;
import org.jeesl.model.ejb.system.job.cache.SystemJobExpiration;
import org.jeesl.model.ejb.system.job.core.SystemJob;
import org.jeesl.model.ejb.system.job.core.SystemJobPriority;
import org.jeesl.model.ejb.system.job.core.SystemJobStatus;
import org.jeesl.model.ejb.system.job.maintenance.SystemJobMaintenance;
import org.jeesl.model.ejb.system.job.maintenance.SystemJobMaintenanceInfo;
import org.jeesl.model.ejb.system.job.maintenance.SystemJobRobot;
import org.jeesl.model.ejb.system.job.template.SystemJobCategory;
import org.jeesl.model.ejb.system.job.template.SystemJobTemplate;
import org.jeesl.model.ejb.system.job.template.SystemJobType;

public class JeeslJobMaintenanceWc extends JeeslJobMaintenanceController<IoLang,IoDescription,IoLocale,SystemJobStatus,SystemJobMaintenance,SystemJobMaintenanceInfo>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslJobMaintenanceWc(JeeslJobMaitenanceTupler callback)
	{
		super(callback,new JobFactoryBuilder<>(IoLang.class,IoDescription.class,SystemJobTemplate.class,SystemJobCategory.class,SystemJobType.class,SystemJobExpiration.class,SystemJob.class,SystemJobPriority.class,SystemJobStatus.class,SystemJobRobot.class,SystemJobCache.class,SystemJobMaintenance.class,SystemJobMaintenanceInfo.class));	
	}
}