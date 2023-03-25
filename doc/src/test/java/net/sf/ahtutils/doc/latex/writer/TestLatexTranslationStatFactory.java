package net.sf.ahtutils.doc.latex.writer;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.model.pojo.status.TranslationStatistic;
import net.sf.ahtutils.test.AbstractUtilsDocTest;
import net.sf.ahtutils.xml.status.Langs;

public class TestLatexTranslationStatFactory extends AbstractUtilsDocTest
{
	final static Logger logger = LoggerFactory.getLogger(TestLatexTranslationStatFactory.class);
	
	private LatexTranslationStatisticWriter factory;
	private Document doc;

	private Namespace nsLang;
	private Element langs;
	
	public static TestLatexTranslationStatFactory factory()
	{
		TestLatexTranslationStatFactory factory = new TestLatexTranslationStatFactory();
		factory.init();
		
		return factory;
	}
	
	@Before
	public void init()
	{	
		factory = new LatexTranslationStatisticWriter(null,null,null);
		
		nsLang = Namespace.getNamespace("s", "http://ahtutils.aht-group.com/status");
		
		langs = createLangs();

		doc = new Document();
		doc.setRootElement(new Element("root"));
	}
	
	public List<TranslationStatistic> createStatistic()
	{
		List<Langs> listLangs = new ArrayList<Langs>();
		
		List<TranslationStatistic> stats = new ArrayList<TranslationStatistic>();
		stats.add(factory.createStatistic(listLangs));
		return stats;
	}
	
	private Element createLangs()
	{
		return new Element("langs",nsLang);
	}
	
	@Test @Ignore
	public void noLangs()
	{
		Assert.assertFalse(factory.hasLangs(doc));
	}
	
	@Test @Ignore
	public void hasOneLangs()
	{
		doc.getRootElement().addContent(langs);
		Assert.assertTrue(factory.hasLangs(doc));
	}
	
	@Test @Ignore
	public void hasTwoLangs()
	{
		doc.getRootElement().addContent(createLangs());
		doc.getRootElement().addContent(createLangs());
		Assert.assertTrue(factory.hasLangs(doc));
		Assert.assertEquals(2, factory.getLangsElements(doc).size());
	}
	
	@Test @Ignore
	public void jdomLangs()
	{
		doc.getRootElement().addContent(langs);
		List<Element> actual = factory.getLangsElements(doc);
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals(langs, actual.get(0));
	}
	
	@Test @Ignore
	public void xmlLangs()
	{
		doc.getRootElement().addContent(langs);
		List<Langs> actual = factory.getLangs(doc);
		Assert.assertEquals(1, actual.size());
	}
}