package net.sf.ahtutils.test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

import org.exlp.util.io.log.LoggerInit;
import org.exlp.util.jx.JaxbUtil;
import org.exlp.util.system.DateUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AbstractJeeslTest
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslTest.class);
	
	protected static File fTarget;
	protected static Random rnd;
	
	public static void setTargetDirectory(File fTarget){AbstractJeeslTest.fTarget=fTarget;}
	
	@BeforeClass
	public static void initTargetDirectory()
	{
		if(fTarget==null)
		{
			String dirTarget = System.getProperty("targetDir");
			if(dirTarget==null){dirTarget="target";}
			setfTarget(new File(dirTarget));
			if(LoggerInit.isLog4jInited())
			{
				logger.debug("Using targeDir "+fTarget.getAbsolutePath());
			}
		}
	}
	
	@BeforeClass
	public static void initRnd()
	{
		rnd = new Random();
	}
	
	protected static void initLogger()
	{
		System.err.println("Must be overridden!!");
		System.exit(-1);
	}
	
	protected long rndL()
	{
		if(rnd==null){initRnd();}
		return rnd.nextLong();
	}
	protected int rndI()
	{
		if(rnd==null){initRnd();}
		return rnd.nextInt();
	}
	protected int rndI(int max)
	{
		if(rnd==null){initRnd();}
		return rnd.nextInt(max);
	}
	
	protected String getSizedString(int size)
    {
    	StringBuffer sb = new StringBuffer();
    	for(int i=0;i<size;i++)
    	{
    		sb.append("a");
    	}
    	return sb.toString();
    }
	
	protected void assertJaxbEquals(Object ref, Object test)
	{
		Assert.assertEquals(JaxbUtil.toString(ref),JaxbUtil.toString(test));
	}

	protected static Date getDefaultDate()
	{
		LocalDateTime ldt = LocalDateTime.of(2011,11,11,11,11,11);
		return DateUtil.toDate(ldt);
	}
	
	protected static void setfTarget(File myTarget) {fTarget=myTarget;}
}