package org.jeesl.model.ejb;

public interface IoLabelFlywayProvider extends JeeslFlywayMigrationVerifier
{	
	public static String baselineTables = "jeesl/system/io/db/migration/io/label/0-Baseline Tables.sql";
	public static String baselineConstraints = "jeesl/system/io/db/migration/io/label/0-Baseline Constraints.sql";
}