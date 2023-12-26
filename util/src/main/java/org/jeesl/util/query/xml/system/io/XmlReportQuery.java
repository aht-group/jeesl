package org.jeesl.util.query.xml.system.io;

import java.util.Hashtable;
import java.util.Map;

import org.jeesl.factory.xml.system.io.report.XmlFontFactory;
import org.jeesl.factory.xml.system.io.report.XmlLayoutFactory;
import org.jeesl.factory.xml.system.io.report.XmlOffsetFactory;
import org.jeesl.factory.xml.system.io.report.XmlQueriesFactory;
import org.jeesl.factory.xml.system.io.report.XmlRowsFactory;
import org.jeesl.factory.xml.system.io.report.XmlSheetsFactory;
import org.jeesl.factory.xml.system.io.report.XmlSizeFactory;
import org.jeesl.factory.xml.system.io.report.XmlStyleFactory;
import org.jeesl.factory.xml.system.io.report.XmlStylesFactory;
import org.jeesl.factory.xml.system.io.report.XmlTemplateFactory;
import org.jeesl.factory.xml.system.status.XmlCategoryFactory;
import org.jeesl.factory.xml.system.status.XmlDataTypeFactory;
import org.jeesl.factory.xml.system.status.XmlImplementationFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.model.io.report.style.JeeslReportColumnWidth;
import org.jeesl.model.xml.io.report.Cell;
import org.jeesl.model.xml.io.report.ColumnGroup;
import org.jeesl.model.xml.io.report.Font;
import org.jeesl.model.xml.io.report.Layout;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Row;
import org.jeesl.model.xml.io.report.Style;
import org.jeesl.model.xml.io.report.Template;
import org.jeesl.model.xml.io.report.XlsColumn;
import org.jeesl.model.xml.io.report.XlsSheet;
import org.jeesl.model.xml.io.report.XlsWorkbook;
import org.jeesl.util.query.xml.XmlStatusQuery;

import net.sf.ahtutils.xml.aht.Query;

public class XmlReportQuery
{
	public static enum Key {exReport}
	
	private static Map<Key,Query> mQueries;
	
	public static Query get(Key key){return get(key,null);}
	public static Query get(Key key,String lang)
	{
		if(mQueries==null){mQueries = new Hashtable<Key,Query>();}
		if(!mQueries.containsKey(key))
		{
			Query q = new Query();
			switch(key)
			{
				case exReport: q.setReport(exReport());break;
			}
			mQueries.put(key, q);
		}
		Query q = mQueries.get(key);
		q.setLang(lang);
		return q;
	}
	
	private static Report exReport()
	{
		Report xml = new Report();
		xml.setCode("");
		xml.setPosition(0);
		xml.setVisible(true);
		
		xml.setXmlExample("");
		xml.setLangs(XmlStatusQuery.langs());
		xml.setDescriptions(XmlStatusQuery.descriptions());
		
		xml.setCategory(XmlCategoryFactory.create(""));
		
		xml.setXlsWorkbook(exportWorkbook());
		
		return xml;
	}
	
	public static Template exTemplate()
	{
		Template xml = new Template();
		xml.setCode("");
		xml.setPosition(0);
		xml.setVisible(true);
		
		xml.setLangs(XmlStatusQuery.langs());
		xml.setDescriptions(XmlStatusQuery.descriptions());
		
		xml.getCell().add(exCell());
		
		return xml;
	}
	
	public static Style exStyle()
	{
		Style xml = new Style();
		xml.setCode("");
		xml.setPosition(0);
		xml.setVisible(true);
		
		xml.setLangs(XmlStatusQuery.langs());
		xml.setDescriptions(XmlStatusQuery.descriptions());
		
		xml.setLayout(XmlLayoutFactory.build());
		xml.getLayout().setFont(exFont());
		
		return xml;
	}
	
	private static Font exFont()
	{
		Font xml = XmlFontFactory.build();
		xml.setBold(true);
		xml.setItalic(true);
		return xml;
	}
	
	private static Cell exCell()
	{
		Cell xml = new Cell();
		xml.setCode("");
		xml.setVisible(true);
		
		xml.setColNr(1);
		xml.setRowNr(1);
		
		xml.setLangs(XmlStatusQuery.langs());
		xml.setDescriptions(XmlStatusQuery.descriptions());
		
		return xml;
	}
	
	private static XlsWorkbook exportWorkbook()
	{
		XlsWorkbook xml = new XlsWorkbook();
		xml.setXlsSheets(XmlSheetsFactory.build());
		xml.getXlsSheets().getXlsSheet().add(exportSheet());
		return xml;
	}
	
	private static XlsSheet exportSheet()
	{
		XlsSheet xml = new XlsSheet();
		xml.setCode("");
		xml.setPosition(0);
		xml.setVisible(true);
		
		xml.getContent().add(XmlStatusQuery.langs());
		xml.getContent().add(XmlStatusQuery.descriptions());
		xml.getContent().add(exportColumnGroup());
		xml.getContent().add(XmlQueriesFactory.build());
		xml.getContent().add(XmlRowsFactory.build(exportRow()));
		xml.getContent().add(XmlImplementationFactory.build(""));
		
		return xml;
	}
	
	private static ColumnGroup exportColumnGroup()
	{
		ColumnGroup xml = new ColumnGroup();
		xml.setCode("");
		xml.setPosition(0);
		xml.setVisible(true);
		
		xml.setShowLabel(true);
		xml.setShowWeb(true);
		xml.setQuery("");
		
		xml.setLangs(XmlStatusQuery.langs());
		xml.setDescriptions(XmlStatusQuery.descriptions());
		xml.setLayout(layout());
		xml.getXlsColumn().add(exportColumn());
		return xml;
	}
	
	private static Row exportRow()
	{
		Row xml = new Row();
		xml.setCode("");
		xml.setPosition(0);
		xml.setVisible(true);
		
		xml.setLangs(XmlStatusQuery.langs());
		xml.setDescriptions(XmlStatusQuery.descriptions());
		xml.setType(XmlTypeFactory.create(""));
		xml.setDataType(XmlDataTypeFactory.build(""));
		xml.setTemplate(XmlTemplateFactory.build(""));
		
		xml.setQueries(XmlQueriesFactory.build());
		xml.setLayout(layout());

		return xml;
	}
	
	private static Layout layout()
	{
		Layout xml = XmlLayoutFactory.build();
		xml.setOffset(XmlOffsetFactory.build());
		xml.setStyles(XmlStylesFactory.build(XmlStyleFactory.build("")));
		xml.getSize().add(XmlSizeFactory.build(JeeslReportColumnWidth.Code.none, XmlTypeFactory.create(""), 1));
		return xml;
	}
	
	private static XlsColumn exportColumn()
	{
		XlsColumn xml = new XlsColumn();
		xml.setCode("");
		xml.setPosition(0);
		xml.setVisible(true);
		
		xml.setShowLabel(true);
		xml.setShowWeb(true);
		
		xml.setDataType(XmlDataTypeFactory.build(""));
		xml.setQueries(XmlQueriesFactory.build());
		xml.setLayout(layout());
		
		xml.setLangs(XmlStatusQuery.langs());
		xml.setDescriptions(XmlStatusQuery.descriptions());
		return xml;
	}
}