package net.sf.ahtutils.msgbundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.xml.status.Lang;
import net.sf.ahtutils.xml.status.Translation;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.exlp.util.io.RelativePathFactory;
import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.exlp.xml.io.Dir;

import org.apache.commons.io.DirectoryWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslationFactory
{
	final static Logger logger = LoggerFactory.getLogger(TranslationFactory.class);
	
	public static final String msgBundleSuffix = "properties";
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>Fields<<<<<<<<<<<<<<<<<<<<<<<<<<<	
	
	private TranslationMap tMap;

	private String outEncoding;
	private int processedFiles;

	public TranslationFactory()
	{
		tMap = new TranslationMap();
		outEncoding = "UTF-8";
		processedFiles = 0;
	}
	
	public void writeMessageResourceBundles(String bundleName, String bundlePackage, String targetDirectory) throws FileNotFoundException, UtilsNotFoundException
	{
		File baseDir = new File(targetDirectory);
		if(!baseDir.exists() || !baseDir.isDirectory())
		{
			throw new FileNotFoundException("Directory "+baseDir.getAbsolutePath()+" does not exist!");
		}
		
		bundlePackage=bundlePackage.replaceAll("\\.", "/");
		File bundleDir = new File(baseDir,bundlePackage);
		if(!bundleDir.exists())
		{
			bundleDir.mkdirs();
		}
		
		for(String langKey : tMap.getLangKeys())
		{
			logger.info("Processing "+langKey);
			String fName = bundleName+"_"+langKey+"."+msgBundleSuffix;
					
			try
			{
				File f = new File(bundleDir,fName);
				OutputStream os = new FileOutputStream(f);
				OutputStreamWriter osw = new OutputStreamWriter(os, outEncoding);
				PrintWriter pw = new PrintWriter(osw, true); 
				
				for(String key : tMap.getTranslationKeys(langKey))
				{
					pw.println(key+"="+tMap.translate(langKey, key));
				}
				
				pw.close();osw.close();os.close();
			}
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public Dir rekursiveDirectory(String directory) throws FileNotFoundException
	{
		File startDirectory = new File(directory);
		RelativePathFactory rpf = new RelativePathFactory(startDirectory,RelativePathFactory.PathSeparator.CURRENT);
		TranslationFileFinder tff = new TranslationFileFinder();
		Dir translationRepo = new Dir();
		try
		{
			for(File f : tff.find(startDirectory))
			{
				net.sf.exlp.xml.io.File xmlF = new net.sf.exlp.xml.io.File();
				xmlF.setName(rpf.relativate(f));
				translationRepo.getFile().add(xmlF);
				
				add(f.getAbsolutePath());
			}
		}
		catch (IOException e) {logger.error(e.getMessage());}
		return translationRepo;
	}
	
	public void add(ClassLoader cl, String xmlFile) throws FileNotFoundException
	{
		Translations translations = (Translations)JaxbUtil.loadJAXB(cl, xmlFile, Translations.class);
		logger.debug("Loaded "+translations.getTranslation().size()+" Elements from "+xmlFile);
		add(translations);
	}
	public void add(String xmlFile) throws FileNotFoundException
	{
		Translations translations = (Translations)JaxbUtil.loadJAXB(xmlFile, Translations.class);
		logger.debug("Loaded "+translations.getTranslation().size()+" Elements from "+xmlFile);
		add(translations);
	}
	
	private void add(Translations translations)
	{	
		for(Translation translation : translations.getTranslation())
		{
			for(Lang lang : translation.getLangs().getLang())
			{
				tMap.add(lang.getKey(), translation.getKey(), lang.getTranslation());
			}
		}
		processedFiles++;
	}
	
	public List<String> getStats()
	{
		List<String> result = new ArrayList<String>();
		result.add("Created Message Bundle (output ecoding: "+outEncoding+", processed files: "+processedFiles+")");
		for(String langKey : tMap.getLangKeys())
		{
			StringBuffer sb = new StringBuffer();
			sb.append("    ").append(langKey).append(": ");
			int number = 0;
			try {number = tMap.getTranslationKeys(langKey).size();}
			catch (UtilsNotFoundException e) {logger.error("",e);}
			sb.append(number).append(" translations");
			result.add(sb.toString());
		}
		return result;
	}
	
	public void debug()
	{
		for(String s : getStats())
		{
			logger.debug(s);
		}
	}
	
	public void setOutEncoding(String outEncoding) {this.outEncoding = outEncoding;}
	public TranslationMap gettMap(){return tMap;}
	
	protected class TranslationFileFinder extends DirectoryWalker<File>
	{
		public TranslationFileFinder()
	    {
			super();
	    }

	    public List<File> find(File startDirectory) throws IOException
	    {
	    	List<File> results = new ArrayList<File>();
	    	walk(startDirectory, results);
	    	return results;
	    }

	    protected boolean handleDirectory(File directory, int depth, Collection<File> results)
	    {
	    	if(directory.getName().equals(".svn")){return false;}
	    	else {return true;}
	    }

	    protected void handleFile(File file, int depth, Collection<File> results)
	    {
		    results.add(file);
	    }
	}
}