package net.sf.ahtutils.doc.ofx.qa.section;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.content.ofx.Comment;
import org.openfuxml.content.ofx.Highlight;
import org.openfuxml.content.ofx.Marginalia;
import org.openfuxml.content.ofx.Paragraph;
import org.openfuxml.content.ofx.Section;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.ofx.XmlHighlightFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlParagraphFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.ofx.editorial.XmlMarginaliaFactory;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.doc.ofx.qa.table.OfxQaFrResultTableFactory;
import net.sf.ahtutils.doc.ofx.qa.table.OfxQaFrTableFactory;
import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.qa.Category;
import net.sf.ahtutils.xml.qa.Expected;
import net.sf.ahtutils.xml.qa.Info;
import net.sf.ahtutils.xml.qa.Test;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxQaFrSectionFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaFrSectionFactory.class);

	private OfxQaFrTableFactory fOfxTableTest;
	private OfxQaFrResultTableFactory fOfxTableTestResult;
	
	private Aht conditions;public void setTestConditions(Aht conditions){this.conditions=conditions;}
	
	public OfxQaFrSectionFactory(Configuration config, String lang, Translations translations)
	{
		this(config,new String[] {lang},translations);
	}
	public OfxQaFrSectionFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		fOfxTableTest = new OfxQaFrTableFactory(config,langs,translations);
		fOfxTableTestResult = new OfxQaFrResultTableFactory(config,langs,translations);
	}
	
	public Section build(Category category, boolean withResults) throws OfxAuthoringException
	{
		Section section = XmlSectionFactory.build();

		section.getContent().add(XmlTitleFactory.build(category.getName()));
		
		Comment comment = XmlCommentFactory.build();
		OfxCommentBuilder.doNotModify(comment);
		section.getContent().add(comment);
		
		for(Test test : category.getTest())
		{
			if(test.isVisible())
			{
				section.getContent().add(buildFrTestSection(test,withResults));
			}
		}
		return section;
	}
	
	private Section buildFrTestSection(Test test, boolean withResults) throws OfxAuthoringException
	{
		Section section = XmlSectionFactory.build();
		section.getContent().add(XmlTitleFactory.build(test.getName()));
		
		Paragraph p = XmlParagraphFactory.build();
		if(test.isSetInfo() && test.getInfo().isSetStatus()){p.getContent().add(marginalia(test.getInfo()));}
		if(test.isSetDescription() && test.getDescription().isSetValue())
		{
			p.getContent().add(test.getDescription().getValue());	
		}
		if(p.getContent().size()>0){section.getContent().add(p);}
		
		section.getContent().add(fOfxTableTest.buildTableTestDetails(test));
		if(test.isSetExpected()){section.getContent().addAll(expectedParagraph(test.getExpected()));}
		if(test.isSetInfo())
		{
			Info info = test.getInfo();
			if(info.isSetComment() && info.getComment().isSetValue() && info.getComment().getValue().length()>0)
			{
				section.getContent().add(testInfo(test.getInfo()));
			}
		}
		
		if(withResults && test.isSetResults() && test.getResults().isSetResult())
		{
			section.getContent().add(fOfxTableTestResult.build(test));
		}
		
		return section;
	}
	
	private List<Paragraph> expectedParagraph(Expected expected)
	{
		List<Paragraph> list = new ArrayList<Paragraph>();
		Paragraph p1 = XmlParagraphFactory.build();
		p1.getContent().add("The expected result is: "+expected.getValue());
		list.add(p1);
		return list;
	}
	
	private Marginalia marginalia(Info info)
	{
		String condition = null;
		if(conditions!=null)
		try
		{
			Status status = StatusXpath.getStatus(conditions.getStatus(), info.getStatus().getCode());
			Lang lCondition = StatusXpath.getLang(status.getLangs(),"en");
			condition = lCondition.getTranslation();
		}
		catch (ExlpXpathNotFoundException e) {}
		catch (ExlpXpathNotUniqueException e) {}
		if(conditions==null){condition = info.getStatus().getCode();}
		
		Paragraph p1 = XmlParagraphFactory.text("Test Status: ");
		Paragraph p2 = XmlParagraphFactory.text(condition);
		
		return XmlMarginaliaFactory.build(p1,p2);
	}
	
	private Highlight testInfo(Info info)
	{
		Highlight highlight = XmlHighlightFactory.build();
		highlight.getContent().add(XmlParagraphFactory.text(info.getComment().getValue()));
		return highlight;
	}
}