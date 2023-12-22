package org.jeesl.doc.ofx.cms;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.exception.OfxConfigurationException;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionsFactory;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.model.xml.core.ofx.Sections;
import org.openfuxml.renderer.latex.content.structure.LatexSectionRenderer;
import org.openfuxml.renderer.latex.preamble.LatexSrcBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslCmsWriter<CMS extends JeeslIoCms<?,?,?,?,?>>
{	
	final static Logger logger = LoggerFactory.getLogger(JeeslCmsWriter.class);
	
	private final File latexBaseDir;
	private ConfigurationProvider cp;
	
	public JeeslCmsWriter(ConfigurationProvider cp, File latexBaseDir)
	{
		this.latexBaseDir=latexBaseDir;
		this.cp=cp;
	}
	
	public void render(Sections sections) throws OfxConfigurationException, IOException, OfxAuthoringException, UtilsConfigurationException
	{
		Writer w = new PrintWriter(new File(latexBaseDir,"srs.tex"));
		
		LatexSrcBook srcBook = new LatexSrcBook(cp);
		srcBook.reportPackages();
		srcBook.graphicsPath("pdf","png");
		srcBook.chapterSectionMarks();
//		logger.info("CMS: "+cms.getName().get("en").getLang());
//		srcBook.fancyHeader("AHT", cms.getName().get("en").getLang());
		srcBook.fancyHeader("AHT", "");
		srcBook.fancyFooter("");
//		srcBook.draft(false);
		srcBook.hyphenation();
		
		srcBook.beginDocument();
//		if(cms.getToc()) {srcBook.toc();}
		
		srcBook.toc();
		
		for(Section section : XmlSectionsFactory.toList(sections))
		{
			LatexSectionRenderer rSection = new LatexSectionRenderer(cp,0,srcBook);
			rSection.render(section);
			srcBook.addSectionRenderer(rSection);
		}
		
		srcBook.endDocument();
		srcBook.write(w);
//		jlr.copyPackages();
	}
}