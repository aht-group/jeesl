package org.jeesl.factory.xml.system.io.report;

import java.util.Collections;
import java.util.Comparator;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.factory.xml.system.status.XmlImplementationFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.data.JeeslReportQueryType;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportGroupComparator;
import org.jeesl.util.query.xpath.ReportXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.report.Queries;
import net.sf.ahtutils.xml.report.XlsSheet;
import net.sf.exlp.exception.ExlpXpathNotFoundException;

public class XmlSheetFactory <L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
								ROW extends JeeslReportRow<L,D,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
								STYLE extends JeeslReportStyle<L,D>,CDT extends JeeslStatus<L,D,CDT>,CW extends JeeslStatus<L,D,CW>,
								RT extends JeeslStatus<L,D,RT>,
								ENTITY extends EjbWithId,
								ATTRIBUTE extends EjbWithId,
								TL extends JeeslTrafficLight<L,D,TLS>,
								TLS extends JeeslStatus<L,D,TLS>,
								FILLING extends JeeslStatus<L,D,FILLING>,
								TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>
								>
{
	final static Logger logger = LoggerFactory.getLogger(XmlSheetFactory.class);
	
	private XlsSheet q;
	
	private Comparator<GROUP> cGroup;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlImplementationFactory<IMPLEMENTATION,L,D> xfImplementation;
	private XmlColumnGroupFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> xfGroup;
	private XmlRowsFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> xfRows;

	public XmlSheetFactory(String localeCode, XlsSheet q)
	{
		this.q=q;
		cGroup = new IoReportGroupComparator<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>().factory(IoReportGroupComparator.Type.position);
	
		try {xfLangs = new XmlLangsFactory<L>(ReportXpath.getLangs(q));} catch (ExlpXpathNotFoundException e) {}
		try {xfDescriptions = new XmlDescriptionsFactory<D>(ReportXpath.getDescriptions(q));} catch (ExlpXpathNotFoundException e) {}
		try {xfImplementation = new XmlImplementationFactory<IMPLEMENTATION,L,D>(localeCode,ReportXpath.getImplementation(q));} catch (ExlpXpathNotFoundException e) {}
		try {xfGroup = new XmlColumnGroupFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(localeCode,ReportXpath.getColumnGroup(q));}catch (ExlpXpathNotFoundException e) {}
		try {xfRows = new XmlRowsFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(localeCode,ReportXpath.getRows(q));}catch (ExlpXpathNotFoundException e) {}
	}
	
	public XlsSheet build(SHEET sheet)
	{
		XlsSheet xml = new XlsSheet();
		
		if(q.isSetCode()){xml.setCode(sheet.getCode());}
		if(q.isSetVisible()){xml.setVisible(sheet.isVisible());}
		if(q.isSetPosition()){xml.setPosition(sheet.getPosition());}
		
		try {ReportXpath.getLangs(q);xml.getContent().add(xfLangs.getUtilsLangs(sheet.getName()));} catch (ExlpXpathNotFoundException e) {}
		try {ReportXpath.getDescriptions(q);xml.getContent().add(xfDescriptions.create(sheet.getDescription()));} catch (ExlpXpathNotFoundException e) {}
		
		try {ReportXpath.getQueries(q);xml.getContent().add(queries(sheet));}catch (ExlpXpathNotFoundException e) {}
		try
		{
			if(sheet.getImplementation()!=null && ReportXpath.getImplementation(q)!=null)
			{
				xml.getContent().add(xfImplementation.build(sheet.getImplementation()));
			}
		}
		catch (ExlpXpathNotFoundException e) {}
		
		try
		{
			ReportXpath.getColumnGroup(q);
			Collections.sort(sheet.getGroups(),cGroup);
			for(GROUP g : sheet.getGroups())
			{
				xml.getContent().add(xfGroup.build(g));
			}
		}
		catch (ExlpXpathNotFoundException e) {}
		
		try
		{
			ReportXpath.getRows(q);
			xml.getContent().add(xfRows.build(sheet));
		}
		catch (ExlpXpathNotFoundException e) {}
		
		return xml;
	}
	
	private Queries queries(SHEET sheet)
	{
		Queries xml = XmlQueriesFactory.build();
		if(sheet.getQueryTable()!=null){xml.getQuery().add(XmlQueryFactory.build(JeeslReportQueryType.Sheet.table, sheet.getQueryTable()));}
		return xml;
	}
}