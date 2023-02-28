package net.sf.ahtutils.doc.latex.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.jeesl.doc.ofx.constraints.OfxConstraintScopeSectionFactory;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintType;
import org.jeesl.model.xml.jeesl.Container;
import org.openfuxml.content.ofx.Section;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.exception.OfxConfigurationException;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.openfuxml.renderer.latex.OfxMultiLangLatexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.ahtutils.xml.system.ConstraintScope;
import net.sf.ahtutils.xml.system.Constraints;
import net.sf.exlp.util.xml.JaxbUtil;

public class LatexConstraintWriter extends AbstractDocumentationLatexWriter
{	
	final static Logger logger = LoggerFactory.getLogger(LatexConstraintWriter.class);
	
	private OfxMultiLangLatexWriter ofxMlw;
		
	private OfxConstraintScopeSectionFactory ofConstraint;
	
	public LatexConstraintWriter(Configuration config, Translations translations,String[] langs, ConfigurationProvider cp)
	{
		super(config,translations,langs,cp);
		
		File baseDir = new File(config.getString(UtilsDocumentation.keyBaseLatexDir));
		ofxMlw = new OfxMultiLangLatexWriter(baseDir,langs,cp);
		
		ofConstraint = new OfxConstraintScopeSectionFactory(config,langs,translations);
		logger.warn("Retrieve Constraint types");
		
//		try{ofConstraint.setConstraintTypes(JaxbUtil.loadJAXB(JeeslConstraintType.xmlResourceContainer, Container.class));}
//		catch (FileNotFoundException e) {e.printStackTrace();}
		
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