package org.jeesl.controller.config.jboss;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.sf.ahtutils.controller.util.MavenArtifactResolver;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JbossModuleConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(JbossModuleConfigurator.class);
	
	private static final String srcBaseDir = "jeesl/listing/installation/jboss/modules";
	
	public static enum Product {eap}
	
	private final String versionPostgres = "42.3.4";
	
	private MultiResourceLoader mrl;
	
	private Product product;
	private String version;
	private File jbossBaseDir;
	
	public JbossModuleConfigurator(Product product, String version,String jbossDir)
	{
		this.product=product;
		this.version=version;
		jbossBaseDir = new File(jbossDir);
		mrl = new MultiResourceLoader();
	}
	
	public void postgres() throws IOException
	{
		File postgresBase = buildMobuleBase("org/postgresql");
		File moduleMain = new File(postgresBase,"main");
		File moduleXml = new File(moduleMain,"module.xml");
		
		if(!postgresBase.exists()){postgresBase.mkdir();}
		if(!moduleMain.exists()){moduleMain.mkdir();}
		if(!moduleXml.exists())
		{
			String src = srcBaseDir+"/"+product+"/"+version+"/postgres.xml";
			logger.info("Available?"+mrl.isAvailable(src)+" "+src);
			InputStream input = mrl.searchIs(src);
			FileUtils.copyInputStreamToFile(input, moduleXml);
		}
		
		if(version.equals("6.3"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.postgis:postgis-jdbc:1.5.3"),moduleMain);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.postgresql:postgresql:"+versionPostgres),moduleMain);
		}
		else if(version.equals("7.0"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.postgis:postgis-jdbc:1.5.3"),moduleMain);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.postgresql:postgresql:"+versionPostgres),moduleMain);
		}
		else if(version.equals("7.1"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.postgis:postgis-jdbc:1.5.3"),moduleMain);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.postgresql:postgresql:"+versionPostgres),moduleMain);
		}
		else if(version.equals("7.2"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.postgresql:postgresql:"+versionPostgres),moduleMain);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("net.postgis:postgis-jdbc:2.5.0"),moduleMain);

		}
		else if(version.equals("7.3"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.postgresql:postgresql:"+versionPostgres),moduleMain);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("net.postgis:postgis-jdbc:2.5.0"),moduleMain);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("net.postgis:postgis-geometry:2.5.0"),moduleMain);

		}
		else
		{
			logger.warn("NO PostGIS drivers defined in "+this.getClass().getSimpleName()+" for "+version);
		}
	}
	
	public void mysql() throws IOException
	{
		File dirCom = buildMobuleBase("com");
		File dirMysql = new File(dirCom,"mysql");
		File dirMain = new File(dirMysql,"main");
		File moduleXml = new File(dirMain,"module.xml");
		
		logger.debug(moduleXml.getAbsolutePath());
		
		if(!dirCom.exists()){dirCom.mkdir();}
		if(!dirMysql.exists()){dirMysql.mkdir();}
		if(!dirMain.exists()){dirMain.mkdir();}
		if(!moduleXml.exists())
		{
			String src = srcBaseDir+"/"+product+"/"+version+"/mysql.xml";
			logger.info("Available?"+mrl.isAvailable(src)+" "+src+" to "+moduleXml.getAbsolutePath());
			InputStream input = mrl.searchIs(src);
			FileUtils.copyInputStreamToFile(input, moduleXml);
		}
		
		if(version.equals("6.3"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("mysql:mysql-connector-java:5.1.29"),dirMain);
		}
		else if(version.equals("7.0"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("mysql:mysql-connector-java:5.1.43"),dirMain);
		}
		else if(version.equals("7.1"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("mysql:mysql-connector-java:5.1.46"),dirMain);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("mysql:mysql-connector-java:8.0.15"),dirMain);
		}
		else
		{
			logger.warn("NO MySQL drivers defined in "+this.getClass().getSimpleName()+" for "+version);
		}
	}
	
	public void mariaDB() throws IOException
	{
		File dirOrg = buildMobuleBase("org");
		File dirMaria = new File(dirOrg,"mariadb");
		File dirMain = new File(dirMaria,"main");
		File moduleXml = new File(dirMain,"module.xml");
		
		logger.debug(moduleXml.getAbsolutePath());
		
		if(!dirOrg.exists()){dirOrg.mkdir();}
		if(!dirMaria.exists()){dirMaria.mkdir();}
		if(!dirMain.exists()){dirMain.mkdir();}
		if(!moduleXml.exists())
		{
			String src = srcBaseDir+"/"+product+"/"+version+"/mariadb.xml";
			logger.info("Available?"+mrl.isAvailable(src)+" "+src+" to "+moduleXml.getAbsolutePath());
			InputStream input = mrl.searchIs(src);
			FileUtils.copyInputStreamToFile(input, moduleXml);
		}
		
		if(version.equals("7.1"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.mariadb.jdbc:mariadb-java-client:2.2.5"),dirMain);
		}
		else
		{
			logger.warn("NO MariaDB drivers defined in "+this.getClass().getSimpleName()+" for "+version);
		}
	}
	
	public void envers() throws IOException
	{
		File hibernateBase = buildMobuleBase("org"+File.separator+"hibernate"+File.separator+"envers");
		File moduleMain = new File(hibernateBase,"main");
		File moduleXml = new File(moduleMain,"module.xml");
		
		if(!hibernateBase.exists()){hibernateBase.mkdir();}
		if(!moduleMain.exists()){moduleMain.mkdir();}
		
		String src = srcBaseDir+"/"+product+"/"+version+"/envers.xml";
		InputStream input = mrl.searchIs(src);
		FileUtils.copyInputStreamToFile(input, moduleXml);
		
		if(version.equals("6.3"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.hibernate:hibernate-envers:4.2.0.Final"),moduleMain);
		}
	}
	
	public void hibernate() throws IOException
	{
		File hibernateBase = buildMobuleBase("org/hibernate");
		File moduleMain = new File(hibernateBase,"main");
		File moduleXml = new File(moduleMain,"module.xml");
		
		if(!hibernateBase.exists()){hibernateBase.mkdir();}
		if(!moduleMain.exists()){moduleMain.mkdir();}
		
		String src = srcBaseDir+"/"+product+"/"+version+"/hibernate.xml";
		InputStream input = mrl.searchIs(src);
		FileUtils.copyInputStreamToFile(input, moduleXml);
		System.out.println("Copy "+src+" to "+moduleXml.getAbsolutePath());
		
		if(version.equals("6.3"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("com.vividsolutions:jts:1.12"),moduleMain);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.hibernate:hibernate-spatial:4.0-M1"),moduleMain);
		}
		else if(version.equals("7.0"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("com.vividsolutions:jts:1.13"),moduleMain);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.geolatte:geolatte-geom:1.0.1"),moduleMain);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.hibernate:hibernate-spatial:5.0.9.Final"),moduleMain);
		}
		else if(version.equals("7.1"))
		{
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.hibernate:hibernate-spatial:5.1.12.Final"),moduleMain);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.geolatte:geolatte-geom:1.0.6"),moduleMain);
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("com.vividsolutions:jts:1.13"),moduleMain);
		}
		else if(version.equals("7.2"))
		{
			//Should match the hibernate version of EAP7.2.x
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.hibernate:hibernate-spatial:5.3.15.Final"),moduleMain);
			
			//Find the version in hibernate-spatial
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.geolatte:geolatte-geom:1.3.0"),moduleMain);
			
			//Find the version in geolatte-geom
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("com.vividsolutions:jts-core:1.14.0"),moduleMain);
		}
		else if(version.equals("7.3"))
		{
			//Should match the hibernate version of EAP7.2.x
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.hibernate:hibernate-spatial:5.3.18.Final"),moduleMain);
			
			
			//Find the version in hibernate-spatial
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.geolatte:geolatte-geom:1.3.0"),moduleMain);
			
			//Find the version in geolatte-geom
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("com.vividsolutions:jts-core:1.14.0"),moduleMain);
			
			FileUtils.copyFileToDirectory(MavenArtifactResolver.resolve("org.hibernate.common:hibernate-commons-annotations:5.1.0.Final"),moduleMain);
		}
	}
	
	private File buildMobuleBase(String packageId)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("modules");
		sb.append(File.separator).append("system");
		sb.append(File.separator).append("layers");
		sb.append(File.separator).append("base");
		sb.append(File.separator).append(packageId);
		return new File(jbossBaseDir,sb.toString());
	}
}