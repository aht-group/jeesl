package net.sf.ahtutils.doc.latex.builder;

import java.io.File;

import org.jeesl.test.JeeslDocBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractUtilsDocTest;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;

@SuppressWarnings("unused")
public class TestLatexDevelopmentDocumentationBuilder extends AbstractUtilsDocTest
{
	final static Logger logger = LoggerFactory.getLogger(TestLatexDevelopmentDocumentationBuilder.class);
	
	private MultiResourceLoader mrl;
    private UtilsLatexDevelopmentDocumentationBuilder b;
	
	
	public void init()
	{	
		super.initOfx();
		mrl = MultiResourceLoader.instance();
        b = new UtilsLatexDevelopmentDocumentationBuilder(null,null,null,cp);
	}

	public static void main(String args[]) throws Exception
	{
		JeeslDocBootstrap.init();

        File f = new File("target","latex.tex");

        TestLatexDevelopmentDocumentationBuilder test = new TestLatexDevelopmentDocumentationBuilder();
        test.initOfx();
        test.init();
	}
}