package org.jeesl.client.test.factory.ejb.io.locale;

import org.jeesl.client.app.JeeslBootstrap;
import org.jeesl.client.model.ejb.system.locale.Description;
import org.jeesl.client.model.ejb.system.locale.Lang;
import org.jeesl.client.model.ejb.system.locale.Status;
import org.jeesl.client.test.AbstractJeeslClientTest;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.model.xml.io.locale.status.Descriptions;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEjbLocaleStatusFactory extends AbstractJeeslClientTest
{
	final static Logger logger = LoggerFactory.getLogger(TestEjbLocaleStatusFactory.class);
	
	private EjbStatusFactory<Lang,Description,Status> efStatus;
	private org.jeesl.model.xml.io.locale.status.Status status;
	
	@BeforeEach
	public void init()
	{
		efStatus = EjbStatusFactory.instance(Status.class, Lang.class,Description.class);
		status = createStatus();
	}
    
    @AfterEach
    public void close()
    {
    	efStatus = null;
    	status = null;
    }
    
    @Test
    public void testBuildEjb() throws InstantiationException, IllegalAccessException, JeeslConstraintViolationException
    {
    	Object o = efStatus.create("test");
    	Assertions.assertTrue(o instanceof Status);
    }
 
    @Test
    public void testBuildXml() throws InstantiationException, IllegalAccessException, JeeslConstraintViolationException
    {
    	Object o = efStatus.create(status);
    	Assertions.assertTrue(o instanceof Status);
    }
    
    @Test
    public void testCode() throws InstantiationException, IllegalAccessException, JeeslConstraintViolationException
    {
    	Status ejb = (Status)efStatus.create(status);
    	Assertions.assertEquals(status.getCode(), ejb.getCode());
    }
    
    @Test
    public void testMapSize() throws InstantiationException, IllegalAccessException, JeeslConstraintViolationException
    {
    	Status ejb = (Status)efStatus.create(status);
    	Assertions.assertEquals(status.getLangs().getLang().size(), ejb.getName().size());
    	Assertions.assertEquals(status.getDescriptions().getDescription().size(), ejb.getDescription().size());
    }
    
    @Test
    public void testTranslationValue() throws InstantiationException, IllegalAccessException, JeeslConstraintViolationException
    {
    	org.jeesl.model.xml.io.locale.status.Lang lang = status.getLangs().getLang().get(0);
    	org.jeesl.model.xml.io.locale.status.Description desc = status.getDescriptions().getDescription().get(0);
    	Status ejb = (Status)efStatus.create(status);
    	Assertions.assertEquals(lang.getTranslation(), ejb.getName().get(lang.getKey()).getLang());
    	Assertions.assertEquals(desc.getValue(), ejb.getDescription().get(lang.getKey()).getLang());
    }
    
    @Test @Disabled //(expected=JeeslConstraintViolationException.class)
    public void testMissingKeyLang() throws InstantiationException, IllegalAccessException, JeeslConstraintViolationException
    {
    	status.getLangs().getLang().get(0).setKey(null);
    	efStatus.create(status);
    }
    
    @Test @Disabled //(expected=JeeslConstraintViolationException.class)
    public void testMissingKeyDescription() throws InstantiationException, IllegalAccessException, JeeslConstraintViolationException
    {
    	status.getDescriptions().getDescription().get(0).setKey(null);
    	efStatus.create(status);
    }
    
    @Test @Disabled //(expected=JeeslConstraintViolationException.class)
    public void testMissingLangTranslation() throws InstantiationException, IllegalAccessException, JeeslConstraintViolationException
    {
    	status.getLangs().getLang().get(0).setTranslation(null);
    	efStatus.create(status);
    }
    
    @Test @Disabled //(expected=JeeslConstraintViolationException.class)
    public void testMissingDescriptionValue() throws InstantiationException, IllegalAccessException, JeeslConstraintViolationException
    {
    	status.getDescriptions().getDescription().get(0).setValue(null);
    	efStatus.create(status);
    }

    
    private org.jeesl.model.xml.io.locale.status.Status createStatus()
    {
    	org.jeesl.model.xml.io.locale.status.Status status = new org.jeesl.model.xml.io.locale.status.Status();
    	status.setCode("testCode");
    	status.setLangs(getLangs());
    	status.setDescriptions(getDescriptions());
    	return status;
    }
    
    private Langs getLangs()
    {
    	Langs langs = new Langs();
    	org.jeesl.model.xml.io.locale.status.Lang l1 = new org.jeesl.model.xml.io.locale.status.Lang();l1.setKey("en");l1.setTranslation("t1");langs.getLang().add(l1);
    	org.jeesl.model.xml.io.locale.status.Lang l2 = new org.jeesl.model.xml.io.locale.status.Lang();l2.setKey("de");l2.setTranslation("t2");langs.getLang().add(l2);
    	return langs;
    }
    
    private Descriptions getDescriptions()
    {
    	Descriptions descriptions = new Descriptions();
    	org.jeesl.model.xml.io.locale.status.Description d1 = new org.jeesl.model.xml.io.locale.status.Description();d1.setKey("en");d1.setValue("v1");descriptions.getDescription().add(d1);
    	org.jeesl.model.xml.io.locale.status.Description d2 = new org.jeesl.model.xml.io.locale.status.Description();d2.setKey("de");d2.setValue("v2");descriptions.getDescription().add(d2);
    	return descriptions;
    }
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, JeeslConstraintViolationException
    {
		JeeslBootstrap.init();
			
		TestEjbLocaleStatusFactory test = new TestEjbLocaleStatusFactory();
		test.init();
		
		test.testBuildEjb();
//		test.testBuildXml();
//		test.testCode();
//		test.testMapSize();
//		test.testMissingKey();
//		test.testMissingTranslation();
//		test.testMissingLangTranslation();
//		test.close();
    }
}