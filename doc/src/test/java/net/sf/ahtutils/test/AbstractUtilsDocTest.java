package net.sf.ahtutils.test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openfuxml.factory.ConfigurationProviderFacotry;
import org.openfuxml.interfaces.configuration.ConfigurationProvider;
import org.openfuxml.interfaces.configuration.DefaultSettingsManager;
import org.openfuxml.interfaces.media.CrossMediaManager;
import org.openfuxml.interfaces.renderer.latex.OfxLatexRenderer;
import org.openfuxml.media.cross.NoOpCrossMediaManager;
import org.openfuxml.util.configuration.settings.OfxDefaultSettingsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.util.io.RelativePathFactory;
import net.sf.exlp.util.io.StringIO;
import net.sf.exlp.util.xml.JaxbUtil;

public class AbstractUtilsDocTest
{
	final static Logger logger = LoggerFactory.getLogger(AbstractUtilsDocTest.class);
	
	protected CrossMediaManager cmm;
	protected DefaultSettingsManager dsm;
	protected ConfigurationProvider cp;
	
	protected File f;
	protected boolean saveReference=false;

	protected static File fTarget;
	protected static void setfTarget(File myTarget) {fTarget=myTarget;}	
	
	@BeforeAll
	public static void initFile()
	{
		if(!LoggerInit.isLog4jInited()){initLogger();}
		String dirTarget = System.getProperty("targetDir");
		if(dirTarget==null){dirTarget="target";}
		setfTarget(new File(dirTarget));
		logger.debug("Using targeDir "+fTarget.getAbsolutePath());
	}
	
	@BeforeAll
    public static void initLogger()
	{
		if(!LoggerInit.isLog4jInited())
		{
			LoggerInit loggerInit = new LoggerInit("log4junit.xml");	
			loggerInit.addAltPath("config.ahtutils-doc.test");
			loggerInit.init();
		}
    }
	
	@BeforeAll
	public static void initPrefixMapper()
	{
//		JaxbUtil.setNsPrefixMapper(new AhtUtilsNsPrefixMapper());
	}
	
	protected void initOfx()
	{
		cmm = new NoOpCrossMediaManager();
		dsm = new OfxDefaultSettingsManager();
		
		cp = ConfigurationProviderFacotry.build(cmm, dsm);
	}
	
	protected void assertJaxbEquals(Object expected, Object actual)
	{
		Assertions.assertEquals("actual XML differes from expected XML",JaxbUtil.toString(expected),JaxbUtil.toString(actual));
	}
	
	protected void saveXml(Object xml, File f, boolean formatted)
	{
		if(saveReference)
		{
			logger.debug("Saving Reference XML");
			JaxbUtil.debug(xml);
			JaxbUtil.save(f, xml, formatted);
		}
	}
	
	protected void debug(OfxLatexRenderer renderer)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Debugging "+renderer.getClass().getSimpleName());
			System.out.println("************************************");
			for(String s : renderer.getContent())
			{
				System.out.println(s);
			}
			System.out.println("************************************");
		}
	}
	
	protected void save(OfxLatexRenderer renderer, File f) throws IOException
	{
		if(saveReference)
		{
			RelativePathFactory rpf = new RelativePathFactory(new File("src/test/resources"),RelativePathFactory.PathSeparator.CURRENT);
			logger.debug("Saving Reference to "+rpf.relativate(f));
			StringWriter actual = new StringWriter();
			renderer.write(actual);
			StringIO.writeTxt(f, actual.toString());
		}
	}
	
	protected void assertText(OfxLatexRenderer renderer, File fExpected) throws IOException
	{
		StringWriter actual = new StringWriter();
		renderer.write(actual);
		assertText(fExpected, actual.toString());
	}
	
	private void assertText(File fExpected, String actual)
	{
		String expected = StringIO.loadTxt(fExpected);		
		Assertions.assertEquals("Texts are different",expected, actual);
	}
	
	protected void assertText(File fExpected, File fActual) throws IOException
	{
		if(saveReference)
		{
			FileUtils.copyFile(fActual, fExpected);
			System.out.println(StringIO.loadTxt(fActual));
		}
		
		String expected = StringIO.loadTxt(fExpected);
		String actual = StringIO.loadTxt(fActual);
		Assertions.assertEquals("Texts are different",expected, actual);
	}
	
	public void setSaveReference(boolean saveReference) {this.saveReference = saveReference;}
	
	@Test public void dummy(){}
}