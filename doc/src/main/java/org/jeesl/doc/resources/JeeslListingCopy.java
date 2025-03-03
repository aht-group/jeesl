package org.jeesl.doc.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Map;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.io.txt.ExlpTxtWriter;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;

public class JeeslListingCopy
{	
	final static Logger logger = LoggerFactory.getLogger(JeeslListingCopy.class);
		
	private String prefix = "listing.aht-utils";
	
	public static final String codeApp = "@@@APP@@@";
	public static final String patternJbossDS = "@@@JBOSS-DS@@@";
	
	public static final String dbPostgresPrepartionDebian = "admin/db/postgres/dump/debian/preparation.sh";
	public static final String dbPostgresCronDebian = "admin/db/postgres/dump/debian/cron.tab";
	public static final String dbPostgresDumpDebian = "admin/db/postgres/dump/debian/dump.sh";
	
	
	public static final String dbPostgresCreateDebianWheezy = "admin/db/postgres/create/debian/wheezy.sql";
	public static final String dbPostgresCreateDebianJessie = "admin/db/postgres/create/debian/jessie.sql";
	public static final String dbPostgresCreateDebianStretch = "admin/db/postgres/create/debian/stretch.sql";
	public static final String dbPostgresCreateOsxYosemite = "admin/db/postgres/create/osx/yosemite.sql";
	
	public static final String dbPostgresTemplateDebianWheezy = "admin/db/postgres/template/debian/wheezy.sql";
	
	public static final String dbPostgresTerminateDebianWheezy = "admin/db/postgres/terminate/debian/wheezy.sql";
	public static final String dbPostgresTerminateDebianJessie = "admin/db/postgres/terminate/debian/jessie.sql";
	
	public static final String dbPostgresTuneDebianWheezy = "admin/db/postgres/tuning/debian/wheezy.sql";
	public static final String dbPostgresTuneDebianJessie = "admin/db/postgres/tuning/debian/jessie.sql";
	public static final String dbPostgresTuneOsxYosemite = "admin/db/postgres/tuning/osx/yosemite.sql";
	
	public static final String jbServicePreparation = "admin/installation/jboss/service/preparation.txt";
	public static final String jbService = "admin/installation/jboss/service/etcInitD.txt";
	
	public static final String jbEap6StartLog = "admin/installation/jboss/deployment/eap6/log/start.txt";
	public static final String jbEap6DeployCli = "admin/installation/jboss/deployment/eap6/cli/deploy.sh";
	public static final String jbEap6DeployLog = "admin/installation/jboss/deployment/eap6/log/deploy.txt";
	public static final String jbEap6UndeployCli = "admin/installation/jboss/deployment/eap6/cli/undeploy.sh";
	public static final String jbEap6UndeployLog = "admin/installation/jboss/deployment/eap6/log/undeploy.txt";
	public static final String jbEap6UpdateCli = "admin/installation/jboss/deployment/eap6/cli/update.sh";
	public static final String jbEap6ConfigMem = "admin/installation/jboss/config/eap6/standalone.mem.txt";
	public static final String jbPostgresDriver = "admin/installation/jboss/config/ds-driver-postgres.xml";
	public static final String jbPostgresDs = "admin/installation/jboss/config/ds-postgres.xml";
	
	public static final String apacheProxy = "admin/installation/tools/apache/proxy.txt";
	
	public static final String apacheRedirectListing = "admin/installation/tools/apache/redirect.html";
	public static final String apacheRedirectPattern = "@@@APACHE.REDIRECT.URL@@@";
	public static final String apacheRedirectConfig = "doc.admin.apache.redirect.url";
	
	public static final String srcMavenVersions = "jeesl/maven/maven-versions.xml";
	public static final String mavenVersions = "development/maven-versions.xml";
	
	public static final String jeeslPrefix = "jeesl/listing";
	
	private Map<String,String> replace;
	private MultiResourceLoader mrl;
	private File dirListing;
	
	public JeeslListingCopy(Configuration config)
	{
		this("listing.aht-utils",new File(config.getString(UtilsDocumentation.keyBaseLatexDir)));
	}
	public JeeslListingCopy(Configuration config, String prefix)
	{
		this(prefix,new File(config.getString(UtilsDocumentation.keyBaseLatexDir)));
	}
	
	
	public JeeslListingCopy(String prefix,File baseDoc)
	{
		this.prefix=prefix;
		mrl = MultiResourceLoader.instance();
		dirListing = new File(baseDoc,"listing");
		logger.info("Using base.dir ("+UtilsDocumentation.keyBaseLatexDir+"): "+baseDoc.getAbsolutePath());
		logger.info("Using listing.dir (base.dir/listing): "+dirListing.getAbsolutePath());
		replace = new Hashtable<String,String>();
	}
	
	public void addReplacement(String key, String value)
	{
		replace.put(key,value);
	}
	
	public void copy(String fileRef) throws UtilsConfigurationException
	{
		copy(fileRef,fileRef);
	}
	
	public void copy(String src, String dst) throws UtilsConfigurationException
	{
		try
		{
			InputStream is = mrl.searchIs(prefix+"/"+src);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bris = new BufferedReader(isr);
			
			ExlpTxtWriter writer = new ExlpTxtWriter();
			String line; 
			while(null != (line = bris.readLine()))
			{
				for(String key : replace.keySet())
				{
					line = line.replace(key,replace.get(key));
				}
				
				writer.add(line);
				logger.trace("Line: "+line);
			}
			File target = new File(dirListing,dst);
			logger.debug("Writing to :"+target.getAbsolutePath());
			writer.writeFile(target);
		}
		catch (FileNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (IOException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
	
	public String jbossConfig(String product, String version, String file)
	{		
		StringBuffer sb = new StringBuffer();
		sb.append("admin/installation/jboss/config");
		sb.append("/").append(product);
		sb.append("/").append(version);
		sb.append("/").append(file.substring(file.lastIndexOf("/")+1));
		
		return sb.toString();
	}
}