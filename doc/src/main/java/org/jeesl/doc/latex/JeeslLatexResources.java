package org.jeesl.doc.latex;

import java.io.File;

import org.openfuxml.exception.OfxConfigurationException;
import org.openfuxml.renderer.latex.util.OfxLatexResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.UtilsDocumentation;

public class JeeslLatexResources extends OfxLatexResources
{	
	final static Logger logger = LoggerFactory.getLogger(JeeslLatexResources.class);
		
	public static enum Code {colors}
	
	public static String glossaryWeb = "ofx.aht-utils/editorial/glossary/web.xml";
	public static String glossaryFile = "ofx.aht-utils/editorial/glossary/file.xml";
	public static String glossaryDb = "ofx.aht-utils/editorial/glossary/db.xml";
	public static String glossaryOs = "ofx.aht-utils/editorial/glossary/os.xml";
	public static String glossaryJava = "ofx.aht-utils/editorial/glossary/java.xml";
	public static String glossaryDev = "ofx.aht-utils/editorial/glossary/development.xml";
	public static String glossaryUi = "ofx.aht-utils/editorial/glossary/ui.xml";
	
	public static String acronymServer = "ofx.aht-utils/editorial/acronym/server.xml";
	public static String acronymDonor = "ofx.aht-utils/editorial/acronym/donor.xml";
	public static String acronymFile = "ofx.aht-utils/editorial/acronym/file.xml";
	public static String acronymOs = "ofx.aht-utils/editorial/acronym/os.xml";
	public static String acronymInternet = "ofx.aht-utils/editorial/acronym/internet.xml";
	public static String acronymDev = "ofx.aht-utils/editorial/acronym/development.xml";
	public static String acronymJava = "ofx.aht-utils/editorial/acronym/java.xml";
	
	public JeeslLatexResources(org.exlp.interfaces.system.property.Configuration config)
	{
		super(config.getString(UtilsDocumentation.keyBaseLatexDir));
	}
	
	public JeeslLatexResources(File fLatexBase)
	{
		super(fLatexBase);
	}
	
	public void copyColors() throws OfxConfigurationException {copyResource("tex.aht-utils","colors");}
}