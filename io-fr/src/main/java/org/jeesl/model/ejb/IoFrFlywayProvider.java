package org.jeesl.model.ejb;

public interface IoFrFlywayProvider extends JeeslFlywayMigrationVerifier,
											IoSsiFlywayProvider
{	
	public static String baselineTables = "jeesl/io/db/migration/io/fr/0-Baseline Tables.sql";
	public static String baselineConstraints = "jeesl/io/db/migration/io/fr/0-Baseline Constraints.sql";
	
//	public void makerIoFr();
}