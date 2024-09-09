package org.jeesl.model.ejb.io.db.flyway;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;

public class FlywayIoSsiPathProvider implements JeeslFlywayPathProvider
{
	public static FlywayIoSsiPathProvider instance() {return new FlywayIoSsiPathProvider();}
	private FlywayIoSsiPathProvider() {}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/io/ssi";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
}