package net.sf.ahtutils.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.exlp.util.io.log.LoggerInit;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFileProcessingTest extends AbstractAhtUtilsXmlTest
{
	final static Logger logger = LoggerFactory.getLogger(AbstractFileProcessingTest.class);
	
	protected File fTest;
	protected File fRef;
	
	@BeforeClass
    public static void initLogger()
	{
		LoggerInit loggerInit = new LoggerInit("log4junit.xml");	
		loggerInit.path("src/test/resources/config.ahtutils-util.test");
		loggerInit.init();
    }
	
	protected static Collection<Object[]> initFileNames(String srcDir, String fileSuffix)
	{
		Collection<Object[]> c = new ArrayList<Object[]>();
		File dirSrc = new File(srcDir);
		for(File f : dirSrc.listFiles())
		{
			if(f.getName().endsWith(fileSuffix))
			{
				Object[] o = new Object[] {f};
				c.add(o);
			}
		}
		if(c.size()==0){logger.warn("No test files found in "+dirSrc.getAbsolutePath());}
		return c;
	}

}