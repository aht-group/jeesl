package org.jeesl.doc.latex.builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import org.exlp.controller.handler.system.property.ConfigLoader;
import org.jeesl.controller.handler.system.property.ConfigBootstrap;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.exception.OfxConfigurationException;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.processor.pre.ExternalContentEagerLoader;
import org.openfuxml.util.OfxCommentBuilder;
import org.openfuxml.util.filter.OfxClassifierFilter;
import org.openfuxml.util.filter.OfxLangFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.DocumentationCommentBuilder;
import net.sf.ahtutils.doc.latex.writer.AbstractDocumentationLatexWriter;

public class AbstractLatexDocumentationBuilder extends AbstractDocumentationLatexWriter
{	
	final static Logger logger = LoggerFactory.getLogger(AbstractLatexDocumentationBuilder.class);
	
	protected Map<String,String> dstFiles;

	public AbstractLatexDocumentationBuilder(org.apache.commons.configuration2.Configuration config, Translations translations,String[] langs, ConfigurationProvider cp)
	{
		this(ConfigBootstrap.wrap(config),translations,langs,cp);
	}
	public AbstractLatexDocumentationBuilder(org.exlp.interfaces.system.property.Configuration config, Translations translations,String[] langs, ConfigurationProvider cp)
	{
		super(config,translations,langs,cp);
		
		dstFiles = new Hashtable<String,String>();
		
		applyConfigCodes();
		applyBaseLatexDir();
		
		logger.info("Using baseLatexDir: "+baseLatexDir);		
	}
	
	
	
	protected void applyBaseLatexDir()
	{
		logger.error("Method applyBaseLatexDir() needs to be @Override");
	}
	
	protected void applyConfigCodes()
	{
		logger.error("Method applyConfigCodes() needs to be @Override");
	}
	
	protected void addConfig(String code, String source){addConfig(code,source,null);}
	protected void addConfig(String code, String source, String destination)
	{
		logger.warn("NYI");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		config.addProperty(code, source);
		if(destination!=null){dstFiles.put(code, destination);}
	}
	
	public <E extends Enum<E>> String getOfxResource(E code){return config.getString(code.toString());}
	
	protected void render(String code) throws UtilsConfigurationException, OfxConfigurationException{render(1,code);}
	protected void render(int lvl, String code) throws UtilsConfigurationException, OfxConfigurationException{render(lvl,code,null);}
	protected void render(String code,String classifier[]) throws UtilsConfigurationException, OfxConfigurationException{render(1,code,classifier);}
	
	public <E extends Enum<E>> void renderE(int lvl, E code) throws UtilsConfigurationException, OfxConfigurationException{render(lvl,code.toString(),null);}
	public <E extends Enum<E>> void renderE(int lvl, E code, String classifier[]) throws UtilsConfigurationException, OfxConfigurationException{render(lvl,code.toString(),classifier);}
	protected void render(int lvl, String code,String classifier[]) throws UtilsConfigurationException, OfxConfigurationException
	{
		try
		{
			renderSection(lvl,code,classifier);
		}
		catch (FileNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (OfxAuthoringException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (IOException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
	
	protected void renderSection(int lvl,String code, String classifier[]) throws OfxAuthoringException, IOException, OfxConfigurationException, UtilsConfigurationException
	{
		logger.trace("Rendering "+Section.class.getSimpleName()+": "+code);
		
		ExternalContentEagerLoader ecel = new ExternalContentEagerLoader();
		Section section = ecel.load(config.getString(code),Section.class);
		
		applySectionSettings(code, section);
		
		Comment comment = XmlCommentFactory.build();
		DocumentationCommentBuilder.configKeyReference(comment, config, code, "Source file");
		
		if(classifier!=null && classifier.length>0)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("Using classifier: ");
			for(String c : classifier){sb.append(c).append(" ");}
			logger.trace(sb.toString());
			
			OfxClassifierFilter mlf = new OfxClassifierFilter(classifier);
			section = mlf.filterLang(section);
			DocumentationCommentBuilder.ofxClassifier(comment,classifier);
		}
		
		OfxCommentBuilder.doNotModify(comment);
		section.getContent().add(comment);
		
		for(String lang : langs)
		{
			OfxLangFilter omf = new OfxLangFilter(lang);
			Section sectionlang = omf.filterLang(section);
			
			File f = new File(baseLatexDir,lang+"/"+"section"+"/"+dstFiles.get(code)+".tex");
			this.writeSection(lvl,sectionlang, f);
		}
	}
	
	protected void applySectionSettings(String code, Section section) {}
}