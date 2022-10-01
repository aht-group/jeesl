package net.sf.ahtutils.db.xml;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.exlp.util.io.compression.JarExtractor;
import net.sf.exlp.util.io.compression.JarStream;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.system.io.db.Db;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAhtDbXmlExtract extends UtilsDbXmlSeedUtil
{
	final static Logger logger = LoggerFactory.getLogger(AbstractAhtDbXmlExtract.class);

	protected String templateDir;
	protected JarStream jarStream;
	private Set<String> extractedIds;
	protected boolean xmlOut;
	
	public AbstractAhtDbXmlExtract(Db dbSeed, DataSource datasource, JarStream jarStream)
	{
		super(dbSeed, datasource);
		this.jarStream=jarStream;
		logger.warn("NYI: TemplateDir");
//		templateDir=config.getString("db/extract/@dirTemplate");
		extractedIds = new HashSet<String>();
		xmlOut = false;
	}
	
	protected String getTemplate(String extractId)
	{
		logger.warn("NYI: getTemplate");
		return null;//templateDir+"/"+config.getString("db/extract/file[@id='"+extractId+"']/@template");
	}
	
	protected void addExtractId(String id) throws UtilsConfigurationException
	{
		logger.debug(id+" "+getTemplate(id)+" -> "+getExtractName(id));
		if(extractedIds.contains(id)){logger.warn("extractedIds already containes "+id);}
		extractedIds.add(id);
	}
	
	public void ideUpdate() throws UtilsConfigurationException
	{
		Iterator<String> iterator = extractedIds.iterator();
		while(iterator.hasNext())
		{
			singleJarExtract(iterator.next());
		}
	}
	
	@SuppressWarnings("unused")
	public void singleJarExtract(String extractId) throws UtilsConfigurationException
	{
		String from = getExtractName(extractId);
		String to = null;//= config.getString("db/prefix[@type='ide']")+"/"+getContentName(extractId);
		
//		singleJarExtract(from, to);
	}
	
	public void singleJarExtract(String from, String to)
	{
		String jarName = null;//config.getString("db/prefix[@type='jar']/@file");
		StringBuffer sb = new StringBuffer();
		sb.append("Extracting "+jarName);
		sb.append(" from jar to "+to);
		logger.info(sb.toString());
		JarExtractor.extract(jarName, from,to);
	}
}