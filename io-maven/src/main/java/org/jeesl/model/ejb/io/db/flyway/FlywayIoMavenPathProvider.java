package org.jeesl.model.ejb.io.db.flyway;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;

public class FlywayIoMavenPathProvider implements JeeslFlywayPathProvider
{
	public static FlywayIoMavenPathProvider instance() {return new FlywayIoMavenPathProvider();}
	private FlywayIoMavenPathProvider() {}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/io/maven";}

	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
}