package org.jeesl.doc.latex.builder;


import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.openfuxml.exception.OfxConfigurationException;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.UtilsDocumentation;

public class JeeslLatexAdminDocumentationBuilder extends AbstractLatexDocumentationBuilder
{	
	final static Logger logger = LoggerFactory.getLogger(JeeslLatexAdminDocumentationBuilder.class);
	
	private enum Code {accessIntroduction};
							 
	public enum MaintenanceCode {mArchitectureIntroductionJgPgGeoMg,mLoggingIntroduction,mJboss,mDisasterRecovery,mScp}
	
	public enum BackupCode {bPostgres}
	
	public enum SecurityCode {sConceptIntroduction,secViews,secUsecases,secRoles,sActualUsecases,sActualRoles,sActualViews}
							 
	public enum InstallationCode {instIntroduction,instDebian,instJava,instJboss,instPostGis,instMySql,instMaven}
	public enum InstallationArchitecture {debian,debianSqueeze,debianWheezy,debianJessie,debianRaspberry,devJava7FX}
	
	public enum JBossClassifier {as7,eap6,eap6Source,mysql,postgis}
	
	public enum RequirementsCode {reqIntroduction,reqHardware,reqAdmin,reqDeveloper}
	public enum RequirementsClassifier {reqHardware,reqAdmin,reqDeveloper,reqNetwork}
	
	public enum ToolsCode {toolsOsticket,toolsApache}
	public enum SystemCode {systemWs,systemTsStructure}
	
	public enum Survey {surveyQuestion,surveySection}
	
	public JeeslLatexAdminDocumentationBuilder(org.exlp.interfaces.system.property.Configuration config, Translations translations,String[] langs, ConfigurationProvider cp)
	{
		super(config,translations,langs,cp);
	}
	public JeeslLatexAdminDocumentationBuilder(org.apache.commons.configuration2.Configuration config, Translations translations,String[] langs, ConfigurationProvider cp)
	{
		super(config,translations,langs,cp);
	}
	
	@Override protected void applyBaseLatexDir()
	{
		baseLatexDir=config.getString(UtilsDocumentation.keyBaseLatexDir);
	}
	
	@Override protected void applyConfigCodes()
	{
		//Security
		addConfig(SecurityCode.sConceptIntroduction.toString(),"ofx.aht-utils/administration/security/concept/introduction.xml","admin/security/concept/introduction");
		addConfig(SecurityCode.secViews.toString(),"ofx.aht-utils/administration/security/settings/views.xml","admin/security/settings/views");
		addConfig(SecurityCode.secUsecases.toString(),"ofx.aht-utils/administration/security/settings/usecases.xml","admin/security/settings/usecases");
		addConfig(SecurityCode.secRoles.toString(),"ofx.aht-utils/administration/security/settings/roles.xml","admin/security/settings/roles");
		addConfig(SecurityCode.sActualViews.toString(),"ofx.aht-utils/administration/security/actual/views.xml");
		addConfig(SecurityCode.sActualRoles.toString(),"ofx.aht-utils/administration/security/actual/roles.xml");
		addConfig(SecurityCode.sActualUsecases.toString(),"ofx.aht-utils/administration/security/actual/usecases.xml");
				
		//Maintenance
		addConfig(MaintenanceCode.mArchitectureIntroductionJgPgGeoMg.toString(),"jeesl/ofx/administration/architecture/jb-pg-geo-mg.xml","admin/system/architecture/introduction");
		addConfig(MaintenanceCode.mLoggingIntroduction.toString(),"jeesl/ofx/administration/logging/introduction.xml","admin/system/logging/introduction");
		addConfig(MaintenanceCode.mJboss.toString(),"ofx.aht-utils/administration/as/jboss.xml","admin/as/jboss");
		addConfig(MaintenanceCode.mDisasterRecovery.toString(),"ofx.aht-utils/administration/system/disaster.xml","admin/system/disasterRecovery");
		addConfig(MaintenanceCode.mScp.toString(),"ofx.aht-utils/maintenance/scp.xml","admin/maintenance/scp");

		//Backup
		addConfig(BackupCode.bPostgres.toString(),"jeesl/ofx/administration/db/postgres.xml","admin/db/postgres");
		
		//Installation
		addConfig(InstallationCode.instIntroduction.toString(),"ofx.aht-utils/installation/introduction.xml","admin/installation/introduction");
		addConfig(InstallationCode.instDebian.toString(),"jeesl/ofx/installation/debian/debian.xml","admin/installation/debian");
		addConfig(InstallationCode.instJava.toString(),"jeesl/ofx/installation/java/java.xml","admin/installation/java");
		addConfig(InstallationCode.instJboss.toString(),"jeesl/ofx/installation/jboss/jboss.xml","admin/installation/jboss");
		addConfig(InstallationCode.instPostGis.toString(),"jeesl/ofx/installation/db/postgres/postgres.xml","admin/installation/postgres");
		addConfig(InstallationCode.instMySql.toString(),"jeesl/ofx/installation/db/mysql/mysql.xml","admin/installation/mysql");
		addConfig(InstallationCode.instMaven.toString(),"jeesl/ofx/installation/tools/maven.xml","admin/installation/maven");
//		addConfig(InstallationCode.instGeoserver.toString(),"ofx.aht-utils/installation/geoserver.xml","admin/installation/geoserver");
		
		//Requirements
		addConfig(RequirementsCode.reqIntroduction.toString(),"ofx.aht-utils/requirements/introduction.xml","admin/requirements/introduction");
		addConfig(RequirementsCode.reqHardware.toString(),"ofx.aht-utils/requirements/hardware.xml","admin/requirements/hardware");
		addConfig(RequirementsCode.reqAdmin.toString(),"ofx.aht-utils/requirements/administrator.xml","admin/requirements/administrator");
		addConfig(RequirementsCode.reqDeveloper.toString(),"ofx.aht-utils/requirements/developer.xml","admin/requirements/developer");
		
		//Tools
		addConfig(ToolsCode.toolsOsticket.toString(),"jeesl/ofx/installation/tools/osticket.xml","admin/installation/tools/osticket");
		addConfig(ToolsCode.toolsApache.toString(),"jeesl/ofx/installation/tools/apache.xml","admin/installation/tools/apache");
		
		//System
		addConfig(SystemCode.systemWs.toString(),"ofx.aht-utils/administration/system/webservice.xml","admin/system/webservice/introduction");
		
		//TimeSeries
		addConfig(SystemCode.systemTsStructure.toString(),"aht-utils/ofx/system/ts/structure.xml","system/ts/structure");
		
		//Survey
		addConfig(Survey.surveySection.toString(),"jeesl/ofx/administration/utils/survey/section.xml","system/survey/section");
		addConfig(Survey.surveyQuestion.toString(),"jeesl/ofx/administration/utils/survey/question.xml","system/survey/question");
	}
	
	@Deprecated
	public void buildDoc() throws UtilsConfigurationException, OfxConfigurationException
	{
		logger.info("buildDoc");
		render(Code.accessIntroduction.toString());
	}
	
	public void render(SecurityCode code) throws UtilsConfigurationException, OfxConfigurationException{render(code.toString());}
	public void render(int lvl,SecurityCode code) throws UtilsConfigurationException, OfxConfigurationException{render(lvl,code.toString());}

	public void render(MaintenanceCode code) throws UtilsConfigurationException, OfxConfigurationException{render(code.toString());}
	public void render(BackupCode code) throws UtilsConfigurationException, OfxConfigurationException{render(code.toString());}
	
	public void render(SystemCode code) throws UtilsConfigurationException, OfxConfigurationException{render(code.toString());}
	public void render(int lvl,SystemCode code) throws UtilsConfigurationException, OfxConfigurationException{render(lvl,code.toString());}
	
	public void render(Survey code) throws UtilsConfigurationException, OfxConfigurationException{render(code.toString());}
	public void render(int lvl,Survey code) throws UtilsConfigurationException, OfxConfigurationException{render(lvl,code.toString());}
	
	public void render(ToolsCode code) throws UtilsConfigurationException, OfxConfigurationException{render(code.toString());}
	public void render(int lvl,ToolsCode code) throws UtilsConfigurationException, OfxConfigurationException{render(lvl,code.toString());}
	
	public void render(InstallationCode code) throws UtilsConfigurationException, OfxConfigurationException{render(code.toString());}
	public void render(InstallationCode code, InstallationArchitecture... architectures) throws UtilsConfigurationException, OfxConfigurationException
	{
		String[] classifier = new String[architectures.length];
		for(int i=0;i<architectures.length;i++){classifier[i]=architectures[i].toString();}
		render(code.toString(),classifier);
	}
	public void render(InstallationCode code, JBossClassifier... versions) throws UtilsConfigurationException, OfxConfigurationException
	{
		String[] classifier = new String[versions.length];
		for(int i=0;i<versions.length;i++){classifier[i]=versions[i].toString();}
		render(code.toString(),classifier);
	}
	
	public void render(RequirementsCode code) throws UtilsConfigurationException, OfxConfigurationException{render(code.toString());}
	public void render(RequirementsCode code, RequirementsClassifier... reqClassifier) throws UtilsConfigurationException, OfxConfigurationException
	{
		String[] classifier = new String[reqClassifier.length];
		for(int i=0;i<reqClassifier.length;i++){classifier[i]=reqClassifier[i].toString();}
		render(code.toString(),classifier);
	}
	
}