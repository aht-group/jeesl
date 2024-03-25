package net.sf.ahtutils.doc.latex.writer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sf.ahtutils.doc.ofx.status.OfxLangStatisticTableFactory;
import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.io.RelativePathFactory;
import net.sf.exlp.util.io.RelativePathFactory.PathSeparator;
import net.sf.exlp.util.io.dir.DirChecker;
import net.sf.exlp.util.io.dir.RecursiveFileFinder;
import net.sf.exlp.util.xml.JDomUtil;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.pojo.io.locale.TranslationStatistic;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LatexTranslationStatisticWriter
{	
	final static Logger logger = LoggerFactory.getLogger(LatexTranslationStatisticWriter.class);
	
	private final static String dirViewTabs = "tab/status";
	
	private String baseLatexDir;
	private RelativePathFactory rpf;
	
	private Translations translations;
	private String[] headerKeys = {"langStatTableHeaderFile","langStatTableHeaderCount","langStatTableHeaderVersion","langStatTableHeaderMissing"};
	private String[] langs;
	
	public LatexTranslationStatisticWriter(Translations translations,String baseLatexDir,String[] langs)
	{
		this.translations=translations;
		this.baseLatexDir=baseLatexDir;
		this.langs=langs;
	}
	
	public void langStatistic(String texName, String searchDir) throws UtilsConfigurationException
	{
		try
		{
			File fDir = new File(searchDir);
			DirChecker.checkFileIsDirectory(fDir);
			rpf = new RelativePathFactory(fDir,PathSeparator.UNIX);
			logger.info("Creating statistic for baseDir "+fDir.getAbsolutePath());
			
			RecursiveFileFinder finder = new RecursiveFileFinder(FileFilterUtils.suffixFileFilter(".xml"));
	    	List<File> list = finder.find(fDir);
	    	logger.info("Found "+list.size()+" potential files");
	    	
	    	List<TranslationStatistic> stats = new ArrayList<TranslationStatistic>();
	    	for(File f : list)
	    	{
	    		try
	    		{
	    			stats.add(process(f));
				}
	    		catch (JeeslNotFoundException e) {}
	    	}
	    	
	    	File f = new File(baseLatexDir,"de/"+dirViewTabs+"/"+texName);
	    	OfxLangStatisticTableFactory fOfx = new OfxLangStatisticTableFactory("de", translations);
	    	fOfx.saveDescription(f, stats, headerKeys);	
		}
		catch (ExlpConfigurationException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (IOException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
	
	private TranslationStatistic process(File f) throws JeeslNotFoundException
	{
		Document doc = JDomUtil.load(f);
		if(!hasLangs(doc)){throw new JeeslNotFoundException();}
		logger.debug("Processing "+f);
		
		List<Langs> langs = getLangs(doc);
		TranslationStatistic stat = createStatistic(langs);
		stat.setFile(rpf.relativate(f));
		return stat;
	}

	protected boolean hasLangs(Document doc)
	{
		if(getLangsElements(doc).size()>0){return true;}
		return false;
	}
	
	protected List<Langs> getLangs(Document doc)
	{
		List<Langs> langs = new ArrayList<Langs>();
		for(Element e : getLangsElements(doc))
		{
			langs.add(JDomUtil.toJaxb(e, Langs.class));
		}
		return langs;
	}
	protected List<Element> getLangsElements(Document doc)
	{
		List<Element> result = new ArrayList<Element>();

		XPathExpression<Element> xpath = XPathFactory.instance().compile("//s:langs", Filters.element());
		List<Element> elements = xpath.evaluate(doc);
		result.addAll(elements);
		return result;
	}
	
	public TranslationStatistic createStatistic(List<Langs> listLangs)
	{
		TranslationStatistic row = new TranslationStatistic();
		row.setFile("dummy");
		row.setAllTranslations(listLangs.size());
		row.setVersionOutdated(0);
		row.setMissing(0);
		
		for(Langs xmlLangs : listLangs)
		{
			boolean versionOutdated=false;
			Integer version = null;
			Set<String> sLangs = new HashSet<String>();
			for(String s : langs){sLangs.add(s);}
			for(Lang lang : xmlLangs.getLang())
			{
				if(Objects.isNull(lang.getVersion())) {versionOutdated=true;}
				if(version!=null && Objects.nonNull(lang.getVersion())) {versionOutdated=true;}
				
				if(sLangs.contains(lang.getKey())){sLangs.remove(lang.getKey());}
			}
			
			if(versionOutdated){row.setVersionOutdated(1+row.getVersionOutdated());}
			if(sLangs.size()>0){row.setMissing(1+row.getMissing());}
		}
		
		return row;
	}
}