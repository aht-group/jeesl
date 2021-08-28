package org.jeesl.factory.builder.system;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.controller.report.JeeslReportUpdater;
import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.report.EjbIoReportCellFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnGroupFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportRowFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportSheetFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportStyleFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportTemplateFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportWorkbookFactory;
import org.jeesl.factory.txt.system.io.report.TxtIoColumnFactory;
import org.jeesl.factory.txt.system.io.report.TxtIoGroupFactory;
import org.jeesl.factory.xls.system.io.report.XlsCellFactory;
import org.jeesl.factory.xls.system.io.report.XlsColumnFactory;
import org.jeesl.factory.xls.system.io.report.XlsRowFactory;
import org.jeesl.factory.xls.system.io.report.XlsStyleFactory;
import org.jeesl.factory.xml.system.io.report.XmlReportFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
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

import net.sf.ahtutils.xml.aht.Query;

public class ReportFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
										CATEGORY extends JeeslStatus<L,D,CATEGORY>,
										REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
										IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
										WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
										SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
										GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
										COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
										ROW extends JeeslReportRow<L,D,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
										TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
										CELL extends JeeslReportCell<L,D,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
										STYLE extends JeeslReportStyle<L,D>,	
										CDT extends JeeslStatus<L,D,CDT>,
										CW extends JeeslStatus<L,D,CW>,
										RT extends JeeslStatus<L,D,RT>,
										RCAT extends JeeslStatus<L,D,RCAT>,
										ENTITY extends EjbWithId,
										ATTRIBUTE extends EjbWithId,
										TL extends JeeslTrafficLight<L,D,TLS>,
										TLS extends JeeslStatus<L,D,TLS>,
										FILLING extends JeeslStatus<L,D,FILLING>,
										TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>
>
	extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(ReportFactoryBuilder.class);
	
	private final Class<CATEGORY> cCategory; public Class<CATEGORY> getClassCategory(){return cCategory;}
	private final Class<REPORT> cReport; public Class<REPORT> getClassReport(){return cReport;}
	private final Class<IMPLEMENTATION> cImplementation; public Class<IMPLEMENTATION> getClassImplementation(){return cImplementation;}
	private final Class<WORKBOOK> cWorkbook; public Class<WORKBOOK> getClasWorkbook(){return cWorkbook;}
	private final Class<SHEET> cSheet; public Class<SHEET> getClassSheet(){return cSheet;}
	private final Class<GROUP> cGroup; public Class<GROUP> getClassGroup(){return cGroup;}
	private final Class<COLUMN> cColumn; public Class<COLUMN> getClassColumn(){return cColumn;}
	private final Class<ROW> cRow; public Class<ROW> getClassRow(){return cRow;}
	private final Class<TEMPLATE> cTemplate; public Class<TEMPLATE> getClassTemplate(){return cTemplate;}
	private final Class<CELL> cCell; public Class<CELL> getClassCell(){return cCell;}
	private final Class<STYLE> cStyle; public Class<STYLE> getClassStyle(){return cStyle;}
	private final Class<CDT> cDataType; public Class<CDT> getClassCellDataType(){return cDataType;}
	private final Class<CW> cColumnWidth; public Class<CW> getClassColumnWidth(){return cColumnWidth;}
	private final Class<RT> cRt; public Class<RT> getClassRowType(){return cRt;}
	private final Class<RCAT> cRevisionCategory; public Class<RCAT> getClassRevisionCategory(){return cRevisionCategory;}
	private final Class<TLS> cTls; public Class<TLS> getClassTrafficLightScope(){return cTls;}
	private final Class<TRANSFORMATION> cTransformation; public Class<TRANSFORMATION> getClassTransformation(){return cTransformation;}
//	
	
	public ReportFactoryBuilder(final Class<L> cL,final Class<D> cD,
								final Class<CATEGORY> cCategory, final Class<REPORT> cReport, 
								final Class<IMPLEMENTATION> cImplementation, final Class<WORKBOOK> cWorkbook, final Class<SHEET> cSheet, 
								final Class<GROUP> cGroup, final Class<COLUMN> cColumn, final Class<ROW> cRow, 
								final Class<TEMPLATE> cTemplate, final Class<CELL> cCell, final Class<STYLE> cStyle, 
								final Class<CDT> cDataType, final Class<CW> cColumnWidth, final Class<RT> cRt, 
								final Class<RCAT> cRevisionCategory, final Class<TLS> cTls,
								final Class<TRANSFORMATION> cTransformation)
	{       
		super(cL,cD);
        this.cCategory = cCategory;
        this.cReport = cReport;
        this.cImplementation=cImplementation;
        this.cWorkbook = cWorkbook;
        this.cSheet = cSheet;
        this.cGroup = cGroup;
        this.cColumn = cColumn;
        this.cRow = cRow;
        this.cTemplate = cTemplate;
        this.cCell = cCell;
        this.cStyle=cStyle;
        this.cDataType=cDataType;
        this.cColumnWidth = cColumnWidth;
        this.cRt = cRt;
        this.cRevisionCategory=cRevisionCategory;
        this.cTls = cTls;
        this.cTransformation=cTransformation;
	}
	
	public JeeslReportUpdater<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> ejbUpdater(JeeslIoReportFacade<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fReport)
	{
		return new JeeslReportUpdater<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(fReport,this);
	}

	
	public EjbIoReportFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> report()
	{
		return new EjbIoReportFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(cL,cD,cCategory,cReport);
	}
	
	public EjbIoReportWorkbookFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> workbook()
	{
		return new EjbIoReportWorkbookFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(cL,cD,cWorkbook);
	}
	
	public EjbIoReportSheetFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> sheet()
	{
		return new EjbIoReportSheetFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(this,cL,cD,cImplementation,cSheet);
	}
	
	public EjbIoReportRowFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> row()
	{
		return new EjbIoReportRowFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(cL,cD,cRow,cTemplate,cDataType,cRt);
	}
	
	public EjbIoReportColumnGroupFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> group()
	{
		return new EjbIoReportColumnGroupFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(cL,cD,cGroup,cStyle);
	}
	
	public EjbIoReportColumnFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> column()
	{
		return new EjbIoReportColumnFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(cL,cD,cColumn,cDataType,cColumnWidth);
	}
	
	public EjbIoReportTemplateFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> template()
	{
		return new EjbIoReportTemplateFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(cL,cD,cTemplate);
	}
	
	public EjbIoReportCellFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> cell()
	{
		return new EjbIoReportCellFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(cL,cD,cCell);
	}
	
	public EjbIoReportStyleFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> style()
	{
		return new EjbIoReportStyleFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(cL,cD,cStyle);
	}
	
	public XlsColumnFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS> xlsColumn()
	{
		return new XlsColumnFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>();
	}
	
	public XlsRowFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS> xlsRow(String localeCode, XlsCellFactory<REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS> xfCell)
	{
		return new XlsRowFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>(localeCode,this,xfCell);
	}
	
	public XlsCellFactory<REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS> xlsCell(String localeCode, XlsStyleFactory<SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS> xfStyle)
	{
		return new XlsCellFactory<REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>(localeCode,xfStyle);
	}
	
	public XlsStyleFactory<SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS> xlsStyle(Workbook xlsWorkbook, List<GROUP> ioGroups, List<COLUMN> ioColumns, List<ROW> ioRows)
	{
		return new XlsStyleFactory<SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>(this,xlsWorkbook,ioGroups,ioColumns,ioRows);
	}
	
	public XmlReportFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> xmlReport(Query q)
	{
		return new XmlReportFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(q);
	}

	
	public TxtIoColumnFactory<COLUMN> tfColumn(String localeCode)
	{
		return new TxtIoColumnFactory<COLUMN>(localeCode);
	}
	
	public TxtIoGroupFactory<SHEET,GROUP> tfGroup(String localeCode)
	{
		return new TxtIoGroupFactory<SHEET,GROUP>(localeCode);
	}
}