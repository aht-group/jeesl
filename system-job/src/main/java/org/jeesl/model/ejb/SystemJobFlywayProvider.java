package org.jeesl.model.ejb;

public interface SystemJobFlywayProvider extends JeeslFlywayMigrationVerifier,
													IoSsiFlywayProvider
{	
	public static final String flywayMigrationPath = "jeesl/system/io/db/migration/system/job";
	
	public static String baselineTables = flywayMigrationPath+"/0-Baseline Tables.sql";
	public static String baselineConstraints = flywayMigrationPath+"/0-Baseline Constraints.sql";
	
	void sinceSystemJob(int i);
	
	void markerSystemJob();
}