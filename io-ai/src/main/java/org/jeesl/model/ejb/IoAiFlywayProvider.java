package org.jeesl.model.ejb;

public interface IoAiFlywayProvider extends JeeslFlywayMigrationVerifier
{
	public static final String flywayMigrationPath = "jeesl/system/io/db/migration/io/ai";
	
	public static String baselineTables = "jeesl/system/io/db/migration/io/ai/0-Baseline Tables.sql";
	public static String baselineConstraints = "jeesl/system/io/db/migration/io/ai/0-Baseline Constraints.sql";
	
//	public void markerIoCms3();
	void sinceIoAi(int i);
}