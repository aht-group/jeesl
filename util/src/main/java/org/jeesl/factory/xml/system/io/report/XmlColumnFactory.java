package org.jeesl.factory.xml.system.io.report;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.factory.xml.system.status.XmlDataTypeFactory;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.report.Queries;
import net.sf.ahtutils.xml.report.XlsColumn;

public class XmlColumnFactory <L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
								ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,TEMPLATE>,
								STYLE extends JeeslReportStyle<L,D>,CDT extends JeeslStatus<L,D,CDT>,
								CW extends JeeslStatus<L,D,CW>,
								RT extends JeeslStatus<L,D,RT>,
								ENTITY extends EjbWithId,
								ATTRIBUTE extends EjbWithId,
								TL extends JeeslTrafficLight<L,D,TLS>,
								TLS extends JeeslStatus<L,D,TLS>,
								FILLING extends JeeslStatus<L,D,FILLING>,
								TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>
								>
{
	final static Logger logger = LoggerFactory.getLogger(XmlColumnFactory.class);
	
	private XlsColumn q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	private XmlDataTypeFactory<CDT,L,D> xfDataType;
	private XmlLayoutFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS> xfLayout;
	
	public XmlColumnFactory(String localeCode, XlsColumn q)
	{
		this.q=q;
		if(q.isSetLangs()){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(q.isSetDescriptions()){xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
		if(q.isSetDataType()){xfDataType = new XmlDataTypeFactory<CDT,L,D>(localeCode,q.getDataType());}
		if(q.isSetLayout()){xfLayout = new XmlLayoutFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>(localeCode,q.getLayout());}
	}
	
	public XlsColumn build(COLUMN column)
	{
		XlsColumn xml = new XlsColumn();
		
		if(q.isSetCode()){xml.setCode(column.getCode());}
		if(q.isSetVisible()){xml.setVisible(column.isVisible());}
		if(q.isSetPosition()){xml.setPosition(column.getPosition());}
		
		if(q.isSetShowLabel()){xml.setShowLabel(column.getShowLabel());}
		if(q.isSetShowWeb()){xml.setShowWeb(column.getShowWeb());}
		
		if(q.isSetDataType() && column.getDataType()!=null){xml.setDataType(xfDataType.build(column.getDataType()));}
		
		if(q.isSetLangs()){xml.setLangs(xfLangs.getUtilsLangs(column.getName()));}
		if(q.isSetDescriptions()){xml.setDescriptions(xfDescriptions.create(column.getDescription()));}
		
		if(q.isSetQueries()){xml.setQueries(queries(column));}
		if(q.isSetLayout()){xml.setLayout(xfLayout.build(column));}
						
		return xml;
	}
	
	private Queries queries(COLUMN column)
	{
		Queries xml = XmlQueriesFactory.build();
		if(column.getQueryHeader()!=null){xml.getQuery().add(XmlQueryFactory.build(JeeslReportQueryType.Column.header, column.getQueryHeader()));}
		if(column.getQueryCell()!=null){xml.getQuery().add(XmlQueryFactory.build(JeeslReportQueryType.Column.cell, column.getQueryCell()));}
		if(column.getQueryFooter()!=null){xml.getQuery().add(XmlQueryFactory.build(JeeslReportQueryType.Column.footer, column.getQueryFooter()));}
		return xml;
	}
}