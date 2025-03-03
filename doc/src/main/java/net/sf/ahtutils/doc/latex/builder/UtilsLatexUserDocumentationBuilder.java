package net.sf.ahtutils.doc.latex.builder;

import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.doc.latex.builder.AbstractLatexDocumentationBuilder;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.openfuxml.exception.OfxConfigurationException;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.UtilsDocumentation;

public class UtilsLatexUserDocumentationBuilder extends AbstractLatexDocumentationBuilder
{	
	final static Logger logger = LoggerFactory.getLogger(UtilsLatexUserDocumentationBuilder.class);
					
	public static final String cfgKeyErSvg = "doc.image.admin.development.er";
	
	
	public static enum Code {uiInterface,uiIcons,uiExport,uiRevision,uiApprovals,uiConstraints,uiWizard}
	
		
	public UtilsLatexUserDocumentationBuilder(Configuration config, Translations translations,String[] langs,ConfigurationProvider cp)
	{
		super(config,translations,langs,cp);
	}
	
	@Override protected void applyBaseLatexDir()
	{
		baseLatexDir=config.getString(UtilsDocumentation.keyBaseLatexDir);
	}
	
	@Override protected void applyConfigCodes()
	{		
		addConfig(Code.uiInterface.toString(),"jeesl/ofx/user/ui/interface.xml","user/ui/interface");
		addConfig(Code.uiIcons.toString(),"jeesl/ofx/user/ui/icons.xml","user/ui/icons");
		addConfig(Code.uiExport.toString(),"jeesl/ofx/user/ui/export.xml","user/ui/fileExport");
		addConfig(Code.uiRevision.toString(),"jeesl/ofx/user/ui/revisions.xml","user/ui/revisions");
		addConfig(Code.uiApprovals.toString(), "jeesl/ofx/user/components/approval.xml","user/components/approval");
		addConfig(Code.uiConstraints.toString(), "jeesl/ofx/user/components/constraints.xml","user/components/constraints");
		addConfig(Code.uiWizard.toString(),"jeesl/ofx/user/ui/wizard.xml","user/ui/wizard");
		
		
	}
	
	public void render(Code code) throws UtilsConfigurationException, OfxConfigurationException{render(1,code);}
	public void render(int lvl, Code code) throws UtilsConfigurationException, OfxConfigurationException{render(lvl,code.toString());}
}