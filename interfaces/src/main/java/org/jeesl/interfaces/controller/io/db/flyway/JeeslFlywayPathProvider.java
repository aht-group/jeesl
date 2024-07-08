package org.jeesl.interfaces.controller.io.db.flyway;

public interface JeeslFlywayPathProvider
{
	public static String sqlTables = "0-Baseline Tables.sql";
	public static String sqlConstraints = "0-Baseline Constraints.sql";
	
	String getRootDirectory();
	String getBaselineTables();
	String getBaselineConstraints();
}