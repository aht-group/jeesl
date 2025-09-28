package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.interfaces.controller.io.db.flyway.marker.FlywayModuleTsVersionMarker;
import org.jeesl.model.ejb.module.ts.config.TsCategory;

public class FlywayModuleTsPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider, FlywayModuleTsVersionMarker
{
	public static FlywayModuleTsPathProvider instance() {return new FlywayModuleTsPathProvider();}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/module/ts";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		list.add(TsCategory.class);
		
		
		return list;
	}
	
	@Override public void sinceModuleTs(int i) {}
}