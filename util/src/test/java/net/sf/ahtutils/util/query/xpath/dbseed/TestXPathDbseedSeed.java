package net.sf.ahtutils.util.query.xpath.dbseed;

import org.jeesl.factory.xml.system.io.db.XmlSeedFactory;
import org.jeesl.model.xml.io.db.Db;
import org.jeesl.model.xml.io.db.Seed;
import org.jeesl.model.xml.system.io.db.TestDb;
import org.jeesl.util.query.xpath.DbseedXpath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TestXPathDbseedSeed
{
	final static Logger logger = LoggerFactory.getLogger(TestXPathDbseedSeed.class);
    
	private Db dbSeed;
	private final String codeOk = "code";
	private final String codeMulti = "multi";
	
	@BeforeEach
	public void iniDbseed()
	{
		dbSeed = TestDb.create(false);

		Seed s1 = XmlSeedFactory.build(codeOk);s1.setCode(codeOk);dbSeed.getSeed().add(s1);
		Seed s2 = XmlSeedFactory.build(codeMulti);s2.setCode(codeMulti);dbSeed.getSeed().add(s2);
		Seed s3 = XmlSeedFactory.build(codeMulti);s3.setCode(codeMulti);dbSeed.getSeed().add(s3);
	}
	
	@Test
	public void find() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Seed test = DbseedXpath.getSeed(dbSeed, codeOk);
	    Assertions.assertEquals(codeOk,test.getCode());
	}

	@Test
	public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Assertions.assertThrows(ExlpXpathNotFoundException.class,() -> {
		DbseedXpath.getSeed(dbSeed, "-1");
	});
	}
	
	 @Test
	 public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	 {
		Assertions.assertThrows(ExlpXpathNotUniqueException.class,() -> {
		 DbseedXpath.getSeed(dbSeed, codeMulti);
	 });
	}
}