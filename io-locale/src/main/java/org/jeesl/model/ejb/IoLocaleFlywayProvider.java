package org.jeesl.model.ejb;

public interface IoLocaleFlywayProvider extends JeeslFlywayMigrationVerifier
{	
	public static String baselineTables = "jeesl/system/io/db/migration/io/locale/0-Baseline Tables.sql";
	public static String baselineConstraints = "jeesl/system/io/db/migration/io/locale/0-Baseline Constraints.sql";
}