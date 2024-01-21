package org.jeesl.model.ejb;

public interface SystemSecurityFlywayProvider extends JeeslFlywayMigrationVerifier
{
	public static final String flywayMigrationPath = "jeesl/system/io/db/migration/system/security";
	
	public static String baselineTables = flywayMigrationPath+"/0-Baseline Tables.sql";
	public static String baselineConstraints = flywayMigrationPath+"/0-Baseline Constraints.sql";
	
	void sinceSystemSecurity(int i);
//	public void markerSecurity2();
}