package net.sf.ahtutils.factory.xml.cloud.facebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

import org.exlp.util.io.StringUtil;
import org.jeesl.JeeslUtilTestBootstrap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.controller.factory.xml.cloud.facebook.AccessTokenFactory;
import net.sf.ahtutils.test.AbstractFileProcessingTest;
import net.sf.ahtutils.xml.cloud.facebook.Token;
import net.sf.exlp.util.xml.JaxbUtil;

@RunWith(Parameterized.class)
public class TestAccessTokenFactory extends AbstractFileProcessingTest
{
	final static Logger logger = LoggerFactory.getLogger(TestAccessTokenFactory.class);
	
	private AccessTokenFactory atf;
	
	private static final String srcDirCode  = "src/test/resources/data/xml/cloud/facebook/accessToken/http";
	private static final String dstDirToken = "src/test/resources/data/xml/cloud/facebook/accessToken/token";

	public TestAccessTokenFactory(File fTest)
	{
		this.fTest = fTest;
	}
	
	private void setRefFile(String suffix, String dir)
	{
		String name = fTest.getName().substring(0, fTest.getName().length()-4);
		fRef = new File(dir,name+"."+suffix);
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> initFileNames() {return initFileNames(srcDirCode, ".txt");}
	
	@Before
	public void init()
	{	
		atf = new AccessTokenFactory(null,null,null);
	}
	
	@After
	public void close()
	{
		atf = null;
	}
    
    @Test
    public void decode() throws FileNotFoundException{decode(false);}
	
	private void decode(boolean saveReference) throws FileNotFoundException
	{
		setRefFile("xml",dstDirToken);
		logger.debug(fTest.getAbsoluteFile().getAbsolutePath());
		String inRaw = StringUtil.readFile(fTest);
		Token testToken = atf.toXml(inRaw);
		if(saveReference)
		{
			JaxbUtil.save(fRef, testToken, true);
		}
		else
		{
			Token refToken = (Token)JaxbUtil.loadJAXB(fRef.getAbsolutePath(), Token.class);
			testToken.setExpires(null);refToken.setExpires(null);
			Assert.assertEquals(JaxbUtil.toString(refToken).trim(),JaxbUtil.toString(testToken).trim());
		}	
	}
	
	public static void main(String[] args) throws FileNotFoundException
    {
		JeeslUtilTestBootstrap.init();
			
		boolean saveReference = true;
		int id = -1;
		int index = 0;
		
		for(Object[] o : TestAccessTokenFactory.initFileNames())
		{
			boolean testThis = (id<0 | id==index);
			logger.trace(id+" "+index+" test?"+testThis);
			if(testThis)
			{
				File fTest = (File)o[0];
				TestAccessTokenFactory test = new TestAccessTokenFactory(fTest);
				test.init();
				test.decode(saveReference);
				test.close();
			}			
			index++;
		}
    }
}