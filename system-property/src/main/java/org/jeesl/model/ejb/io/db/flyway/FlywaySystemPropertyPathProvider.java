package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.model.ejb.system.property.SystemFeature;
import org.jeesl.model.ejb.system.property.SystemProperty;
import org.jeesl.model.ejb.system.property.SystemPropertyCategory;
import org.jeesl.model.ejb.system.property.SystemVersion;

public class FlywaySystemPropertyPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider, FlywaySystemPropertyVersionMarker
{
	public static FlywaySystemPropertyPathProvider instance() {return new FlywaySystemPropertyPathProvider();}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/system/property";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		
		list.add(SystemVersion.class);
		list.add(SystemProperty.class);
		list.add(SystemPropertyCategory.class);
		list.add(SystemFeature.class);
		
		
		return list;
	}

//	@Override public void sinceSystemJob(int i) {}
//	@Override public void markerSystemJob() {}
}