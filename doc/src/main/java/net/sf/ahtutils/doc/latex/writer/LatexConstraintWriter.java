package net.sf.ahtutils.doc.latex.writer;

import java.io.File;
import java.io.IOException;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.doc.ofx.constraints.OfxConstraintScopeSectionFactory;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.constraint.ConstraintScope;
import org.jeesl.model.xml.system.constraint.Constraints;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.exception.OfxConfigurationException;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.renderer.latex.OfxMultiLangLatexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.UtilsDocumentation;

public class LatexConstraintWriter extends AbstractDocumentationLatexWriter
{	
	final static Logger logger = LoggerFactory.getLogger(LatexConstraintWriter.class);
	
	private OfxMultiLangLatexWriter ofxMlw;
		
	private OfxConstraintScopeSectionFactory ofConstraint;
	
	public LatexConstraintWriter(org.exlp.interfaces.system.property.Configuration config, Translations translations,String[] langs, ConfigurationProvider cp)
	{
		super(config,translations,langs,cp);
		
		File baseDir = new File(config.getString(UtilsDocumentation.keyBaseLatexDir));
		ofxMlw = new OfxMultiLangLatexWriter(baseDir,langs,cp);
		
		ofConstraint = new OfxConstraintScopeSectionFactory(config,langs,translations);
		logger.warn("Retrieve Constraint types");
	}
	
	public void constraints(String artifact) throws OfxAuthoringException, OfxConfigurationException, IOException, UtilsConfigurationException
	{
		Constraints index = JaxbUtil.loadJAXB("constraints."+artifact+"/index.xml", Constraints.class);
		for(ConstraintScope scope : index.getConstraintScope())
		{
			Constraints c = JaxbUtil.loadJAXB("constraints."+artifact+"/"+scope.getCategory()+".xml", Constraints.class);
			for(ConstraintScope s : c.getConstraintScope())
			{
				s.setCategory(scope.getCategory());
				scope(s);
			}
		}
	}
	
	public void scope(ConstraintScope scope) throws IOException, OfxAuthoringException, UtilsConfigurationException, OfxConfigurationException 
	{
		try
		{
			Section section = ofConstraint.build(scope);
			ofxMlw.section(2,"/system/constraints/"+scope.getCategory()+"/"+scope.getCode(),section);
		}
		catch (OfxAuthoringException e){throw new OfxAuthoringException(e.getMessage()+" -- "+ConstraintScope.class.getSimpleName()+" "+scope.getCategory()+"."+scope.getCode());}
	}
}