package net.sf.ahtutils.doc.latex.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.openfuxml.content.table.Table;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.openfuxml.renderer.latex.OfxMultiLangLatexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.db.xml.UtilsDbXmlSeedUtil;
import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.ahtutils.doc.ofx.status.OfxStatusTableFactory;
import net.sf.ahtutils.doc.ofx.status.OfxStatusTableFactory.Code;
import net.sf.ahtutils.exception.processing.UtilsConfigurationException;
import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.dbseed.Db;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.exlp.util.io.StringUtil;
import net.sf.exlp.util.xml.JaxbUtil;

public class LatexStatusWriter extends AbstractDocumentationLatexWriter
{	
	final static Logger logger = LoggerFactory.getLogger(LatexStatusWriter.class);
	
	public static String systemConstraintsType = "db.aht-utils/system/constraints/type.xml";
	
	private OfxMultiLangLatexWriter ofxMlw;
	private UtilsDbXmlSeedUtil seedUtil;
	
	private String seedKey,seedKeyParent;
	private boolean withIcon;
	
	public LatexStatusWriter(Configuration config, Translations translations,String[] langs, ConfigurationProvider cp, String dirTable) throws UtilsConfigurationException
	{
		super(config,translations,langs,cp);
		File baseDir = new File(config.getString(UtilsDocumentation.keyBaseLatexDir));
		ofxMlw = new OfxMultiLangLatexWriter(baseDir,langs,cp);
		ofxMlw.setDirTable(dirTable);
		
		withIcon = false;
		
		String dbSeedFile = config.getString(UtilsDbXmlSeedUtil.configKeySeed);
		logger.debug("Using seed: "+dbSeedFile);
		try
		{
			Db dbSeed = (Db)JaxbUtil.loadJAXB(dbSeedFile, Db.class);
			seedUtil = new UtilsDbXmlSeedUtil(dbSeed);
		}
		catch (FileNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
	
	
	@Deprecated public void buildStatusTable(String seedKeyStatus) throws UtilsConfigurationException
	{
		buildStatusTable(seedKeyStatus, 10,30,40);
	}
		
	@Deprecated public void table(boolean withIcon,Aht ahtStatus,String texName) throws UtilsConfigurationException
	{
		this.withIcon=withIcon;
		if(withIcon){table(ahtStatus,null,texName,15,10,30,40);}
		else{table(ahtStatus,null,texName,10,30,40);}
	}
	@Deprecated public void table(boolean withIcon,String seedKey) throws UtilsConfigurationException
	{
		this.withIcon=withIcon;
		this.seedKey=seedKey;
		this.seedKeyParent=null;
		table(15,10,30,40);
	}
	
	@Deprecated public void buildStatusTable(String seedKey, String seedKeyParent) throws UtilsConfigurationException
	{
		this.withIcon=false;
		this.seedKey=seedKey;
		this.seedKeyParent=seedKeyParent;
		table(15,10,30,40);
	}
	
	@Deprecated public void buildStatusTable(String seedKey, int... colWidths) throws UtilsConfigurationException
	{
		this.withIcon=false;
		this.seedKey=seedKey;
		this.seedKeyParent=null;
		table(colWidths);
	}
	
	@Deprecated public void buildStatusTable(String seedKey, String seedKeyParent, int... colWidths) throws UtilsConfigurationException
	{
		this.withIcon=false;
		this.seedKey=seedKey;
		this.seedKeyParent=seedKeyParent;
		table(colWidths);
	}
	
	@Deprecated private void table(int... colWidths) throws UtilsConfigurationException
	{	
		Aht athStatus;
		Aht ahtParents = null;
		
		try
		{
			athStatus = JaxbUtil.loadJAXB(seedUtil.getExtractName(seedKey), Aht.class);
			if(seedKeyParent!=null){ahtParents = JaxbUtil.loadJAXB(seedUtil.getExtractName(seedKeyParent), Aht.class);}
			
			String texName = seedUtil.getContentName(seedKey);
			texName = texName.substring(0, texName.indexOf(".xml"));
			
			table(athStatus, ahtParents, texName, colWidths);
		}
		catch (FileNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
	
	@Deprecated private void table(Aht athStatus, Aht ahtParents, String texName, int... colWidths) throws UtilsConfigurationException
	{	
		try
		{
			logger.info(texName);
			OfxStatusTableFactory fOfx = new OfxStatusTableFactory(config,langs,translations);
			fOfx.setColWidths(colWidths);
			if(ahtParents!=null){fOfx.activateParents(ahtParents);}
			fOfx.renderColumn(Code.icon, withIcon);
			
			Table table = fOfx.buildLatexTable(StringUtil.dash2Dot(texName),athStatus);
			ofxMlw.table("/status/"+texName, table);
		}
		catch (OfxAuthoringException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (IOException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
	
	//**************************************************************************************************
	
	private Aht statusList,statusParentList;
	private String texName;
	
	public void table2(String keyStatus) throws UtilsConfigurationException
	{
		loadFile(keyStatus,null);
		table3(OfxStatusTableFactory.Code.name,OfxStatusTableFactory.Code.description);
	}
	public void icon(String keyStatus) throws UtilsConfigurationException
	{
		loadFile(keyStatus,null);
		table3(OfxStatusTableFactory.Code.icon,OfxStatusTableFactory.Code.name,OfxStatusTableFactory.Code.description);
	}
	public void table2(String keyStatus, OfxStatusTableFactory.Code... columns) throws UtilsConfigurationException
	{
		loadFile(keyStatus,null);
		table3(columns);
	}
	
	public void table2(String keyStatus,String keyParent) throws UtilsConfigurationException
	{
		loadFile(keyStatus,keyParent);
		table3(OfxStatusTableFactory.Code.parent,OfxStatusTableFactory.Code.name,OfxStatusTableFactory.Code.description);
	}
	public void table2(String keyStatus,String keyParent, OfxStatusTableFactory.Code... columns) throws UtilsConfigurationException
	{
		loadFile(keyStatus,keyParent);
		table3(columns);
	}
	
	private void table3(OfxStatusTableFactory.Code... columns) throws UtilsConfigurationException
	{
		try
		{
			logger.info(texName);
			OfxStatusTableFactory fOfx = new OfxStatusTableFactory(config,langs,translations);
			fOfx.setParentKey(texName);
			fOfx.setColumns(columns);
			fOfx.setListStatus(statusList);
			fOfx.setListParent(statusParentList);
			
			Table table = fOfx.build(StringUtil.dash2Dot(texName));
		
			JaxbUtil.trace(table);
			ofxMlw.table("/status/"+texName, table);
			
		}
		catch (OfxAuthoringException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadFile(String keyStatus, String keyParent) throws UtilsConfigurationException
	{
		try
		{
			statusList = JaxbUtil.loadJAXB(seedUtil.getExtractName(keyStatus), Aht.class);
			if(keyParent!=null){statusParentList = JaxbUtil.loadJAXB(seedUtil.getExtractName(keyParent), Aht.class);}
			else{statusParentList=null;}
			texName = seedUtil.getContentName(keyStatus);
			texName = texName.substring(0, texName.indexOf(".xml"));
		}
		catch (FileNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (UtilsConfigurationException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
}