package org.jeesl.model.ejb;

public interface IoSsiFlywayProvider extends JeeslFlywayMigrationVerifier //extends IoLocaleFlywayProvider
{	
	public static final String flywayMigrationPath = "jeesl/system/io/db/migration/io/ssi";
	public static String baselineTables = "jeesl/system/io/db/migration/io/ssi/0-Baseline Tables.sql";
	public static String baselineConstraints = "jeesl/system/io/db/migration/io/ssi/0-Baseline Constraints.sql";
	
//	public void markerIoSsi();
	
	void implementsOfIoSsiSince(int i);
}