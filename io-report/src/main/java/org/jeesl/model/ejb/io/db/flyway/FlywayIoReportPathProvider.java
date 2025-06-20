package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.interfaces.controller.io.db.flyway.marker.FlywayIoReportVersionMarker;
import org.jeesl.model.ejb.io.report.style.IoReportAlignment;
import org.jeesl.model.ejb.io.report.style.IoReportStyle;

public class FlywayIoReportPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider, FlywayIoReportVersionMarker
{	
	public static FlywayIoReportPathProvider instance() {return new FlywayIoReportPathProvider();}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/io/report";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		
		list.add(IoReportStyle.class);
		list.add(IoReportAlignment.class);
		
		return list;
	}

	@Override public void sinceIoReport(int i) {}
}