package net.sf.ahtutils.test.controller.factory;

import net.sf.ahtutils.exception.ejb.UtilsIntegrityException;
import net.sf.ahtutils.factory.ejb.status.EjbStatusFactory;
import net.sf.ahtutils.model.ejb.status.AhtUtilsDescription;
import net.sf.ahtutils.model.ejb.status.AhtUtilsLang;
import net.sf.ahtutils.model.ejb.status.AhtUtilsStatus;
import net.sf.ahtutils.test.AbstractAhtUtilTest;
import net.sf.ahtutils.test.controller.util.TestRankComparator;
import net.sf.ahtutils.xml.status.Description;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Lang;
import net.sf.ahtutils.xml.status.Langs;
import net.sf.ahtutils.xml.status.Status;
import net.sf.exlp.util.io.LoggerInit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtilsStatusEjbFactory extends AbstractAhtUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestRankComparator.class);
	
	private EjbStatusFactory<AhtUtilsStatus,AhtUtilsLang,AhtUtilsDescription> facStatus;
	private Status status;
	
	@Before
	public void init()
	{
		facStatus = EjbStatusFactory.createFactory(AhtUtilsStatus.class, AhtUtilsLang.class,AhtUtilsDescription.class);
		status = createStatus();
	}
    
    @After
    public void close()
    {
    	facStatus = null;
    	status = null;
    }
 
    @Test
    public void testClass() throws InstantiationException, IllegalAccessException, UtilsIntegrityException
    {
    	Object o = facStatus.create(status);
    	Assert.assertTrue(o instanceof AhtUtilsStatus);
    }
    
    @Test
    public void testCode() throws InstantiationException, IllegalAccessException, UtilsIntegrityException
    {
    	AhtUtilsStatus ejb = (AhtUtilsStatus)facStatus.create(status);
    	Assert.assertEquals(status.getCode(), ejb.getCode());
    }
    
    @Test
    public void testMapSize() throws InstantiationException, IllegalAccessException, UtilsIntegrityException
    {
    	AhtUtilsStatus ejb = (AhtUtilsStatus)facStatus.create(status);
    	Assert.assertEquals(status.getLangs().getLang().size(), ejb.getName().size());
    	Assert.assertEquals(status.getDescriptions().getDescription().size(), ejb.getDescription().size());
    }
    
    @Test
    public void testTranslationValue() throws InstantiationException, IllegalAccessException, UtilsIntegrityException
    {
    	Lang lang = status.getLangs().getLang().get(0);
    	Description desc = status.getDescriptions().getDescription().get(0);
    	AhtUtilsStatus ejb = (AhtUtilsStatus)facStatus.create(status);
    	Assert.assertEquals(lang.getTranslation(), ejb.getName().get(lang.getKey()).getLang());
    	Assert.assertEquals(desc.getValue(), ejb.getDescription().get(lang.getKey()).getLang());
    }
    
    @Test(expected=UtilsIntegrityException.class)
    public void testMissingKeyLang() throws InstantiationException, IllegalAccessException, UtilsIntegrityException
    {
    	status.getLangs().getLang().get(0).setKey(null);
    	facStatus.create(status);
    }
    
    @Test(expected=UtilsIntegrityException.class)
    public void testMissingKeyDescription() throws InstantiationException, IllegalAccessException, UtilsIntegrityException
    {
    	status.getDescriptions().getDescription().get(0).setKey(null);
    	facStatus.create(status);
    }
    
    @Test(expected=UtilsIntegrityException.class)
    public void testMissingLangTranslation() throws InstantiationException, IllegalAccessException, UtilsIntegrityException
    {
    	status.getLangs().getLang().get(0).setTranslation(null);
    	facStatus.create(status);
    }
    
    @Test(expected=UtilsIntegrityException.class)
    public void testMissingDescriptionValue() throws InstantiationException, IllegalAccessException, UtilsIntegrityException
    {
    	status.getDescriptions().getDescription().get(0).setValue(null);
    	facStatus.create(status);
    }
    
    //**********************************************
    
    private Status createStatus()
    {
    	Status status = new Status();
    	status.setCode("testCode");
    	status.setLangs(getLangs());
    	status.setDescriptions(getDescriptions());
    	return status;
    }
    
    private Langs getLangs()
    {
    	Langs langs = new Langs();
    	Lang l1 = new Lang();l1.setKey("en");l1.setTranslation("t1");langs.getLang().add(l1);
    	Lang l2 = new Lang();l2.setKey("de");l2.setTranslation("t2");langs.getLang().add(l2);
    	return langs;
    }
    
    private Descriptions getDescriptions()
    {
    	Descriptions descriptions = new Descriptions();
    	Description d1 = new Description();d1.setKey("en");d1.setValue("v1");descriptions.getDescription().add(d1);
    	Description d2 = new Description();d2.setKey("de");d2.setValue("v2");descriptions.getDescription().add(d2);
    	return descriptions;
    }
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, UtilsIntegrityException
    {
		LoggerInit loggerInit = new LoggerInit("log4j.xml");	
			loggerInit.addAltPath("src/test/resources/config");
			loggerInit.init();		
			
		TestUtilsStatusEjbFactory test = new TestUtilsStatusEjbFactory();
		test.init();
		test.testClass();
		test.testCode();
		test.testMapSize();
//		test.testMissingKey();
//		test.testMissingTranslation();
		test.testMissingLangTranslation();
		test.close();
    }
}