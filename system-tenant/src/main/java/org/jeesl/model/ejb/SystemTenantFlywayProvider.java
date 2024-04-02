package org.jeesl.model.ejb;

public interface SystemTenantFlywayProvider extends JeeslFlywayMigrationVerifier
{	
	public static String baselineTables = "jeesl/system/io/db/migration/system/tenant/0-Baseline Tables.sql";
	public static String baselineConstraints = "jeesl/system/io/db/migration/system/tenant/0-Baseline Constraints.sql";
	
//	public void markerSecurity2();
}