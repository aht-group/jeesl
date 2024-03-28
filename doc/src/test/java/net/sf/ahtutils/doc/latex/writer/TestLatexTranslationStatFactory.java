package net.sf.ahtutils.doc.latex.writer;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jeesl.model.pojo.io.locale.TranslationStatistic;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractUtilsDocTest;

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
	
	@BeforeEach
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
	
	@Test @Disabled
	public void noLangs()
	{
		Assertions.assertFalse(factory.hasLangs(doc));
	}
	
	@Test @Disabled
	public void hasOneLangs()
	{
		doc.getRootElement().addContent(langs);
		Assertions.assertTrue(factory.hasLangs(doc));
	}
	
	@Test @Disabled
	public void hasTwoLangs()
	{
		doc.getRootElement().addContent(createLangs());
		doc.getRootElement().addContent(createLangs());
		Assertions.assertTrue(factory.hasLangs(doc));
		Assertions.assertEquals(2, factory.getLangsElements(doc).size());
	}
	
	@Test @Disabled
	public void jdomLangs()
	{
		doc.getRootElement().addContent(langs);
		List<Element> actual = factory.getLangsElements(doc);
		Assertions.assertEquals(1, actual.size());
		Assertions.assertEquals(langs, actual.get(0));
	}
	
	@Test @Disabled
	public void xmlLangs()
	{
		doc.getRootElement().addContent(langs);
		List<Langs> actual = factory.getLangs(doc);
		Assertions.assertEquals(1, actual.size());
	}
}