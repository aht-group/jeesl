package org.jeesl.model.ejb;

public interface IoCmsFlywayProvider extends JeeslFlywayMigrationVerifier
{	
	public static String baselineTables = "jeesl/system/io/db/migration/io/cms/0-Baseline Tables.sql";
	public static String baselineConstraints = "jeesl/system/io/db/migration/io/cms/0-Baseline Constraints.sql";
	
//	public void markerIoCms3();
}