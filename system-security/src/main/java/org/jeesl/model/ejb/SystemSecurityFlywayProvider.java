package org.jeesl.model.ejb;

public interface SystemSecurityFlywayProvider extends JeeslFlywayMigrationVerifier
{	
	public static String baseline = "jeesl/system/io/db/migration/system/security/0-Baseline.sql";
	
//	public void markerSecurity();
}