package org.jeesl.model.ejb;

public interface SystemSecurityFlywayProvider extends JeeslFlywayMigrationVerifier
{	
	public static String baselineTables = "jeesl/system/io/db/migration/system/security/0-Baseline Tables.sql";
	public static String baselineConstraints = "jeesl/system/io/db/migration/system/security/0-Baseline Constraints.sql";
	
//	public void markerSecurity2();
}