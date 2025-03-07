package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.model.ejb.module.cal.unit.CalMonth;
import org.jeesl.model.ejb.module.cal.unit.CalWeekOfYear;
import org.jeesl.model.ejb.module.cal.unit.CalYear;

public class FlywayModuleCalendarPathProvider implements JeesDdlClassProvider,JeeslFlywayPathProvider
{	
	public static FlywayModuleCalendarPathProvider instance() {return new FlywayModuleCalendarPathProvider();}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/module/cal";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		list.add(CalYear.class);
		list.add(CalMonth.class);
		list.add(CalWeekOfYear.class);
		return list;
	}
}