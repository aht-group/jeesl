package net.sf.ahtutils.controller.factory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import net.sf.exlp.util.io.StringIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@SuppressWarnings("rawtypes")
public class AbstractFreemarkerFileFactory
{
	final static Logger logger = LoggerFactory.getLogger(AbstractFreemarkerFileFactory.class);
		
	protected File fTmpDir;
	
	protected Map freemarkerNodeModel;
	protected Configuration freemarkerConfiguration;
	
	public AbstractFreemarkerFileFactory(File fTmpDir)
	{
		this.fTmpDir=fTmpDir;
		
		freemarkerNodeModel = new HashMap();
		freemarkerConfiguration = new Configuration();
		freemarkerConfiguration.setClassForTemplateLoading(this.getClass(), "/");
	}
	
	protected void createFile(File f, String template) throws IOException, TemplateException
	{
		File fTmp = new File(fTmpDir,f.getName());
		logger.debug("Using tmp-Dir: "+fTmpDir);
		logger.debug("f.getName()="+f.getName());
		logger.debug("ftmp="+fTmp.getAbsolutePath());
		
		Template ftl = freemarkerConfiguration.getTemplate(template,"UTF-8");
		ftl.setEncoding("UTF-8");
		
		StringWriter sw = new StringWriter();
		ftl.process(freemarkerNodeModel, sw);
		sw.flush();
		
		StringIO.writeTxtIfDiffers(sw.toString(), f);
	}
}