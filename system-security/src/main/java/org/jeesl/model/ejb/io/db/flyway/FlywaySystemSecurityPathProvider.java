package org.jeesl.model.ejb.io.db.flyway;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;

public class FlywaySystemSecurityPathProvider implements JeeslFlywayPathProvider
{	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/system/security";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
}