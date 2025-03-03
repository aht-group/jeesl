package net.sf.ahtutils.doc.latex.builder;

import java.util.NoSuchElementException;

import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.doc.latex.builder.AbstractLatexDocumentationBuilder;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.openfuxml.exception.OfxConfigurationException;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.openfuxml.model.xml.core.media.Image;
import org.openfuxml.model.xml.core.ofx.Listing;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.util.query.XmlSectionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class UtilsLatexDevelopmentDocumentationBuilder extends AbstractLatexDocumentationBuilder
{	
	final static Logger logger = LoggerFactory.getLogger(UtilsLatexDevelopmentDocumentationBuilder.class);
					
	public static final String cfgKeyErSvg = "doc.image.admin.development.er";
	public static final String cfgKeyVcsUrl = "doc.admin.development.vcs";
	
	
	public static enum ErCode {erIntroduction}
	public static enum EclipseClassifier {luna,svn,git,texlipse}
	public static enum Code {latex,doc,svn}
	public static enum Maven {mvnIntroduction,mvnShortcuts,mvnStructure}
		
	public UtilsLatexDevelopmentDocumentationBuilder(Configuration config, Translations translations,String[] langs, ConfigurationProvider cp)
	{
		super(config,translations,langs,cp);
	}
	
	@Override protected void applyBaseLatexDir()
	{
		baseLatexDir=config.getString(UtilsDocumentation.keyBaseLatexDir);
	}
	
	@Override protected void applyConfigCodes()
	{	
		addConfig(ErCode.erIntroduction.toString(),"ofx.aht-utils/development/er.xml","admin/development/er");
		addConfig("eclipse","jeesl/ofx/development/ide/eclipse/eclipse.xml","admin/development/environment/eclipse");
		
		addConfig(Code.latex.toString(),"ofx.aht-utils/development/environment/latex.xml","admin/development/environment/latex");
		addConfig(Code.doc.toString(),"ofx.aht-utils/development/documentation.xml","admin/development/documentation");
		addConfig(Code.svn.toString(),"ofx.aht-utils/development/svn.xml","admin/development/svn");
		
		addConfig(Maven.mvnIntroduction.toString(),"ofx.aht-utils/development/maven/introduction.xml","admin/development/maven/introduction");
		addConfig(Maven.mvnShortcuts.toString(),"ofx.aht-utils/development/maven/shortcuts.xml","admin/development/maven/shortcuts");
		addConfig(Maven.mvnStructure.toString(),"ofx.aht-utils/development/maven/structure.xml","admin/development/maven/structure");
	}

	public void render(Code code) throws UtilsConfigurationException, OfxConfigurationException{render(2,code);}
	public void render(int lvl, Code code) throws UtilsConfigurationException, OfxConfigurationException{render(lvl,code.toString());}
	
	public void render(int lvl, Maven code) throws UtilsConfigurationException, OfxConfigurationException{render(lvl,code.toString());}
	
	public void render(ErCode code) throws UtilsConfigurationException, OfxConfigurationException{render(code.toString());}
	
	public void renderEclipse(EclipseClassifier... versions) throws UtilsConfigurationException, OfxConfigurationException
	{
		String[] classifier = new String[versions.length];
		for(int i=0;i<versions.length;i++){classifier[i]=versions[i].toString();}
		render(2,"eclipse",classifier);
	}
	
	protected void applySectionSettings(String code, Section section)
	{
		logger.info("TEST "+code);
		if(code.equals("eclipse")){applyEclipseSettings(section);}
		for(Object s : section.getContent())
		{
			if (s instanceof Image)
			{
				Image image = (Image)s;
				if(image.getId().equals("image.admin.development.er"))
				{
					image.getMedia().setSrc(config.getString(cfgKeyErSvg));
				}
			}
		}
	}
	
	private void applyEclipseSettings(Section section)
	{
		logger.info("Eclipse code");
		try
		{	// VCS.URL
			String url = config.getString(cfgKeyVcsUrl);
			Listing listing = XmlSectionQuery.getSeed(section,"listing.admin.installation.eclipse.vcs.url");
			listing.getRaw().setValue(url);
		}
		catch (NoSuchElementException e){logger.warn(e.toString());}
		catch (ExlpXpathNotFoundException e) {logger.warn(e.toString());}
		catch (ExlpXpathNotUniqueException e) {logger.warn(e.toString());}
	}
}