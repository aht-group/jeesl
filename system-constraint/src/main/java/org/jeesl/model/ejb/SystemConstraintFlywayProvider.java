package org.jeesl.model.ejb;

public interface SystemConstraintFlywayProvider extends JeeslFlywayMigrationVerifier
{	
	public static String baselineTables = "jeesl/system/io/db/migration/system/constraint/0-Baseline Tables.sql";
	public static String baselineConstraints = "jeesl/system/io/db/migration/system/constraint/0-Baseline Constraints.sql";
	
//	public void markerSecurity2();
}