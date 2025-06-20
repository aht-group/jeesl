package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.interfaces.controller.io.db.flyway.marker.FlywayModuleAomVersionMarker;

public class FlywayIoReportPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider, FlywayModuleAomVersionMarker
{	
	public static FlywayIoReportPathProvider instance() {return new FlywayIoReportPathProvider();}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/io/report";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		
		
		
		return list;
	}

	@Override public void sinceModuleAom(int i) {}
}