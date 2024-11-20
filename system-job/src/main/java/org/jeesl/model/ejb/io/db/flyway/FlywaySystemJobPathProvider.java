package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.interfaces.controller.io.db.flyway.marker.FlywaySystemJobVersionMarker;
import org.jeesl.model.ejb.system.job.cache.SystemJobCache;
import org.jeesl.model.ejb.system.job.cache.SystemJobExpiration;
import org.jeesl.model.ejb.system.job.core.SystemJob;
import org.jeesl.model.ejb.system.job.core.SystemJobPriority;
import org.jeesl.model.ejb.system.job.core.SystemJobStatus;
import org.jeesl.model.ejb.system.job.maintenance.SystemJobRobot;
import org.jeesl.model.ejb.system.job.template.SystemJobCategory;
import org.jeesl.model.ejb.system.job.template.SystemJobTemplate;
import org.jeesl.model.ejb.system.job.template.SystemJobType;

public class FlywaySystemJobPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider, FlywaySystemJobVersionMarker
{
	public static FlywaySystemJobPathProvider instance() {return new FlywaySystemJobPathProvider();}
	private FlywaySystemJobPathProvider() {}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/system/job";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		
		list.add(SystemJobCache.class);
		list.add(SystemJobTemplate.class);
		list.add(SystemJobCategory.class);
		
		list.add(SystemJobType.class);
		list.add(SystemJobPriority.class);
		list.add(SystemJobExpiration.class);
		list.add(SystemJob.class);
		list.add(SystemJobStatus.class);
		
		list.add(SystemJobCache.class);
		list.add(SystemJobRobot.class);
		
		return list;
	}

	@Override public void sinceSystemJob(int i) {}
//	@Override public void markerSystemJob() {}
}