package org.jeesl.model.ejb;

public interface FlywayIoDbProvider extends JeeslFlywayMigrationVerifier
{
//	public static String baselineTables = "jeesl/system/io/db/migration/io/db/0-Baseline Tables.sql";
//	public static String baselineConstraints = "jeesl/system/io/db/migration/io/db/0-Baseline Constraints.sql";
	
	void implementsOfIoDbSince(int i);
	
//	public void y();
}