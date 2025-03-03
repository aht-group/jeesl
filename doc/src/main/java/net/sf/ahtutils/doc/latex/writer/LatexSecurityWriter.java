package net.sf.ahtutils.doc.latex.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.api.rest.rs.system.security.JeeslSecurityRestExport;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.navigation.Menu;
import org.jeesl.model.xml.system.security.Security;
import org.jeesl.model.xml.system.security.View;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.exception.OfxConfigurationException;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.openfuxml.model.xml.addon.graph.Node;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.renderer.latex.OfxMultiLangLatexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.menu.OfxMenuTreeFactory;
import net.sf.ahtutils.doc.ofx.security.list.OfxSecurityCategoryListFactory;
import net.sf.ahtutils.doc.ofx.security.section.OfxSecurityPagesSectionFactory;
import net.sf.ahtutils.doc.ofx.security.section.OfxSecurityRolesSectionFactory;
import net.sf.ahtutils.doc.ofx.security.section.OfxSecurityUsecasesSectionFactory;
import net.sf.ahtutils.doc.ofx.security.section.OfxSecurityViewsSectionFactory;
import net.sf.exlp.util.io.StringIO;

public class LatexSecurityWriter extends AbstractDocumentationLatexWriter
{	
	final static Logger logger = LoggerFactory.getLogger(LatexSecurityWriter.class);
	
	private OfxMultiLangLatexWriter ofxMlw;
	
	private final static String dirDescriptions = "description/security";
		
	private OfxSecurityCategoryListFactory ofSecurityCategoryList;
	private OfxSecurityUsecasesSectionFactory ofUsecases;
	private OfxSecurityRolesSectionFactory ofRoles;
	private OfxSecurityViewsSectionFactory ofViews;
	private OfxSecurityPagesSectionFactory ofPages;
	
	private List<String> headerKeysViews;
	
	public LatexSecurityWriter(org.exlp.interfaces.system.property.Configuration config, Translations translations, String[] langs, OfxMultiLangLatexWriter ofxMlw, ConfigurationProvider cp)
	{
		super(config,translations,langs,cp);
		this.ofxMlw=ofxMlw;
		buildFactories();
	}
	
	private void buildFactories()
	{
		ofSecurityCategoryList = new OfxSecurityCategoryListFactory(config,langs,translations,cp);
		ofUsecases = new OfxSecurityUsecasesSectionFactory(config,langs,translations);
		ofRoles = new OfxSecurityRolesSectionFactory(config,langs,translations);
		ofViews = new OfxSecurityViewsSectionFactory(config,langs,translations);
		ofPages = new OfxSecurityPagesSectionFactory(config,langs,translations);
		
		headerKeysViews = new ArrayList<String>();
		headerKeysViews.add("auSecurityTableViewName");
		headerKeysViews.add("auSecurityTableViewDescription");
	}
		
	public void writeMenuTrees(String xmlMenu, String xmlViews) throws UtilsConfigurationException
	{
		try
		{
			logger.info("Creating menu trees from "+xmlMenu+" to LaTex");
			Menu menu = JaxbUtil.loadJAXB(xmlMenu,Menu.class);
			Security access = JaxbUtil.loadJAXB(xmlViews, Security.class);
			for(String lang : langs)
			{
				OfxMenuTreeFactory f = new OfxMenuTreeFactory(config,lang,translations);
				org.openfuxml.model.xml.addon.graph.Tree tree = f.build(menu,access);
				JaxbUtil.trace(tree);
				for(Node node : tree.getNode().getNode())
				{
					logger.info(node.getCode());
				}
			}
		}
		catch (FileNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (OfxAuthoringException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
		
	@Deprecated
	public void writeCategoryDescriptionsOld(String xmlFile, String extractId) throws UtilsConfigurationException
	{
		logger.info("Creating descriptions from "+xmlFile+" to LaTex");
		for(String lang : langs)
		{
			File f = new File(baseLatexDir,lang+"/"+dirDescriptions+"/"+extractId+".tex");
			
			try
			{
				logger.debug("Converting "+xmlFile+" to LaTex ("+f.getAbsolutePath());
				Security access = JaxbUtil.loadJAXB(xmlFile, Security.class);
				OfxSecurityCategoryListFactory latexFactory = new OfxSecurityCategoryListFactory(config,lang,translations,cp);
				String content = latexFactory.saveDescription(access.getCategory());
				StringIO.writeTxt(f, content);
			}
			catch (FileNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
			catch (OfxAuthoringException e) {throw new UtilsConfigurationException(e.getMessage());}
		}
	}
	
	public void categoryList(String xmlFile, String extractId) throws UtilsConfigurationException, FileNotFoundException
	{
		logger.info("Creating descriptions from "+xmlFile+" to LaTex");
		
		Security security = JaxbUtil.loadJAXB(xmlFile, Security.class);
		org.openfuxml.model.xml.core.list.List list;
		try {list = ofSecurityCategoryList.descriptionList(security.getCategory());}
		catch (OfxAuthoringException e) {throw new UtilsConfigurationException(e.getMessage());}
		JaxbUtil.info(list);
		
		for(String lang : langs)
		{
			File f = new File(baseLatexDir,lang+"/"+dirDescriptions+"/"+extractId+".tex");
			
			try
			{
				logger.info("Converting "+xmlFile+" to LaTex ("+f.getAbsolutePath());
				Security access = JaxbUtil.loadJAXB(xmlFile, Security.class);
				OfxSecurityCategoryListFactory latexFactory = new OfxSecurityCategoryListFactory(config,lang,translations,cp);
				String content = latexFactory.saveDescriptionSec(access.getCategory());
				StringIO.writeTxt(f, content);
			}
			catch (FileNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
			catch (OfxAuthoringException e) {throw new UtilsConfigurationException(e.getMessage());}
		}
	}
	
	public void usecases(int lvl, JeeslSecurityRestExport rest) throws OfxAuthoringException, OfxConfigurationException, IOException, UtilsConfigurationException {usecases(lvl,rest.documentationSecurityUsecases());}
	public void usecases(Security security) throws UtilsConfigurationException, OfxAuthoringException, OfxConfigurationException, IOException{usecases(2,security);}
	public void usecases(int lvl, Security security) throws UtilsConfigurationException, OfxAuthoringException, OfxConfigurationException, IOException
	{
		Section section = ofUsecases.build(security);
		JaxbUtil.trace(security);
		ofxMlw.section(lvl,"/admin/security/actual/usecases",section);
	}
	
	public void roles(int lvl, JeeslSecurityRestExport rest) throws OfxAuthoringException, OfxConfigurationException, IOException, UtilsConfigurationException{roles(lvl,rest.exportSecurityRoles());}
	public void roles(Security security) throws UtilsConfigurationException, OfxAuthoringException, OfxConfigurationException, IOException{roles(2,security);}
	public void roles(int lvl, Security security) throws OfxAuthoringException, OfxConfigurationException, IOException, UtilsConfigurationException
	{
		Section section = ofRoles.build(security);
		JaxbUtil.trace(security);
		ofxMlw.section(lvl,"/admin/security/actual/roles",section);
	}
	
	public void views(int lvl, JeeslSecurityRestExport rest) throws OfxAuthoringException, OfxConfigurationException, IOException, UtilsConfigurationException{views(lvl,rest.documentationSecurityViews());}
	public void views(Security security) throws UtilsConfigurationException, OfxAuthoringException, OfxConfigurationException, IOException{views(2,security);}
	public void views(int lvl, Security security) throws UtilsConfigurationException, OfxAuthoringException, OfxConfigurationException, IOException
	{
		JaxbUtil.trace(security);
		Section section = ofViews.build(security);
		ofxMlw.section(lvl,"/admin/security/actual/views",section);
	}
	
	public void pageActions(JeeslSecurityRestExport rest) throws OfxAuthoringException, OfxConfigurationException, IOException
	{
		Security security = rest.documentationSecurityPageActions();
		
		for(org.jeesl.model.xml.system.security.Category category : security.getCategory())
		{
			for(View view : category.getViews().getView())
			{
				StringBuffer sb = new StringBuffer();
				sb.append("/admin/security/actual/pages");
				
				for (String w : category.getCode().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"))
				{
					sb.append("/").append(w.toLowerCase());
			    }

				sb.append("/").append(view.getCode());
				
				Section section = ofPages.build(view);
				ofxMlw.section(2,sb.toString(),section);
			}
		}
	}
}