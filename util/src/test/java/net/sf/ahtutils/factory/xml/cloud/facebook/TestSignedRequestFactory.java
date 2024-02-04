package net.sf.ahtutils.factory.xml.cloud.facebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

import org.exlp.util.io.StringUtil;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.JeeslBootstrap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.controller.factory.xml.cloud.facebook.SignedRequestFactory;
import net.sf.ahtutils.test.AbstractFileProcessingTest;
import net.sf.ahtutils.xml.cloud.facebook.SignedRequest;
import net.sf.exlp.util.io.StringIO;

@RunWith(Parameterized.class)
public class TestSignedRequestFactory extends AbstractFileProcessingTest
{
	final static Logger logger = LoggerFactory.getLogger(TestSignedRequestFactory.class);
	
	private SignedRequestFactory srf;
	
	private static final String srcDirName    = "src/test/resources/data/xml/cloud/facebook/request/raw";
	private static final String dstDirNameDec = "src/test/resources/data/xml/cloud/facebook/request/decode";
	private static final String dstDirNameReq = "src/test/resources/data/xml/cloud/facebook/request/xml";

	@BeforeClass
	public static void initStatic()
	{
		
	}
	
	public TestSignedRequestFactory(File fTest)
	{
		this.fTest = fTest;
	}
	
	private void setRefFile(String suffix, String dir)
	{
		String name = fTest.getName().substring(0, fTest.getName().length()-4);
		fRef = new File(dir,name+"."+suffix);
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> initFileNames() {return initFileNames(srcDirName, ".txt");}
	
	@Before
	public void init()
	{	
		srf = new SignedRequestFactory();
	}
	
	@After
	public void close()
	{
		srf = null;
	}
    
    @Test
    public void decode(){decode(false);}
	
	private void decode(boolean saveReference)
	{
		setRefFile("txt",dstDirNameDec);
		logger.debug(fTest.getAbsolutePath());
		String inRaw = StringUtil.readFile(fTest);
		srf.decode(inRaw);
		String testPayload = srf.getTxtPayload();
		if(saveReference)
		{
			StringIO.writeTxt(fRef, testPayload.trim());
		}
		else
		{
			String refPayload = StringUtil.readFile(fRef);
			Assert.assertEquals(refPayload.trim(),testPayload.trim());
		}	
	}
	
	@Test
	public void xml() throws FileNotFoundException{xml(false);}
	
	private void xml(boolean saveReference) throws FileNotFoundException
	{
		setRefFile("xml",dstDirNameReq);
		logger.debug(fTest.getAbsolutePath());
		String inRaw = StringUtil.readFile(fTest);
		SignedRequest actual = srf.decode(inRaw);
		if(saveReference)
		{
			JaxbUtil.save(fRef, actual, true);
		}
		else
		{
			SignedRequest expected = JaxbUtil.loadJAXB(fRef.getAbsolutePath(), SignedRequest.class);
			assertJaxbEquals(expected,actual);
			
		}	
	}
	
	public static void main(String[] args) throws FileNotFoundException
    {
		JeeslBootstrap.init();
		TestSignedRequestFactory.initStatic();	
		
		boolean saveReference = true;
		int id = -1;
		int index = 0;
		
		for(Object[] o : TestSignedRequestFactory.initFileNames())
		{
			boolean testThis = (id<0 | id==index);
			logger.trace(id+" "+index+" test?"+testThis);
			if(testThis)
			{
				File fTest = (File)o[0];
				TestSignedRequestFactory test = new TestSignedRequestFactory(fTest);
				test.init();
//				test.decode(saveReference);
				test.xml(saveReference);
				test.close();
			}			
			index++;
		}
    }
}