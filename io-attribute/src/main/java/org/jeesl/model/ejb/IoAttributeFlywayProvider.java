package org.jeesl.model.ejb;

import org.jeesl.model.ejb.JeeslFlywayMigrationVerifier;

public interface IoAttributeFlywayProvider extends JeeslFlywayMigrationVerifier
{	
	public static String baselineTables = "jeesl/system/io/db/migration/io/attribute/0-Baseline Tables.sql";
	public static String baselineConstraints = "jeesl/system/io/db/migration/io/attribute/0-Baseline Constraints.sql";
	
//	public void markerSecurity2();
}