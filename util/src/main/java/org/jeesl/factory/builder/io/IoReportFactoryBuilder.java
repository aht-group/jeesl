package org.jeesl.factory.builder.io;

import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.controller.report.JeeslReportUpdater;
import org.jeesl.controller.util.comparator.ejb.io.report.EjbIoReportComparator;
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
import org.jeesl.factory.xlsx.io.report.XlsCellFactory;
import org.jeesl.factory.xlsx.io.report.XlsColumnFactory;
import org.jeesl.factory.xlsx.io.report.XlsRowFactory;
import org.jeesl.factory.xlsx.io.report.XlsStyleFactory;
import org.jeesl.factory.xml.system.io.report.XmlReportFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportAlignment;
import org.jeesl.interfaces.model.io.report.style.JeeslReportColumnWidth;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Query;

public class IoReportFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
										CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
										REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
										IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
										WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
										SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
										GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
										COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
										ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
										TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
										CELL extends JeeslReportCell<L,D,TEMPLATE>,
										STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
										ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
										CDT extends JeeslReportCellType<L,D,CDT,?>,
										CW extends JeeslReportColumnWidth<L,D,CW,?>,
										RT extends JeeslReportRowType<L,D,RT,?>,
										RCAT extends JeeslStatus<L,D,RCAT>,
										ENTITY extends EjbWithId,
										ATTRIBUTE extends EjbWithId,
										TL extends JeeslTrafficLight<L,D,TLS>,
										TLS extends JeeslTrafficLightScope<L,D,TLS,?>,
										FILLING extends JeeslStatus<L,D,FILLING>,
										TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>
>
	extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoReportFactoryBuilder.class);
	
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
	
	public IoReportFactoryBuilder(final Class<L> cL,final Class<D> cD,
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
	
	public JeeslReportUpdater<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT> ejbUpdater(JeeslIoReportFacade<REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL> fReport)
	{
		return new JeeslReportUpdater<>(fReport,this);
	}
	
	public EjbIoReportFactory<L,D,CATEGORY,REPORT> report()
	{
		return new EjbIoReportFactory<>(cL,cD,cCategory,cReport);
	}
	
	public EjbIoReportWorkbookFactory<REPORT,WORKBOOK> workbook()
	{
		return new EjbIoReportWorkbookFactory<>(cWorkbook);
	}
	
	public EjbIoReportSheetFactory<L,D,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,STYLE> sheet()
	{
		return new EjbIoReportSheetFactory<>(this,cL,cD,cImplementation,cSheet);
	}
	
	public EjbIoReportRowFactory<L,D,SHEET,GROUP,ROW,TEMPLATE,STYLE,CDT,RT> row()
	{
		return new EjbIoReportRowFactory<>(cL,cD,cRow,cTemplate,cDataType,cRt);
	}
	
	public EjbIoReportColumnGroupFactory<L,D,SHEET,GROUP,COLUMN,STYLE> group()
	{
		return new EjbIoReportColumnGroupFactory<>(cL,cD,cGroup,cStyle);
	}
	
	public EjbIoReportColumnFactory<L,D,SHEET,GROUP,COLUMN,ROW,CELL,STYLE,CDT,CW,RT> column()
	{
		return new EjbIoReportColumnFactory<>(cL,cD,cColumn,cDataType,cColumnWidth);
	}
	
	public EjbIoReportTemplateFactory<L,D,TEMPLATE,CELL,STYLE,CDT,CW> template()
	{
		return new EjbIoReportTemplateFactory<>(cL,cD,cTemplate);
	}
	
	public EjbIoReportCellFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT> cell()
	{
		return new EjbIoReportCellFactory<>(cL,cD,cCell);
	}
	
	public EjbIoReportStyleFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT> style()
	{
		return new EjbIoReportStyleFactory<>(cL,cD,cStyle);
	}
	
	public XlsColumnFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS> xlsColumn()
	{
		return new XlsColumnFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>();
	}
	
	public XlsRowFactory<WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT> xlsRow(String localeCode, XlsCellFactory<GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW> xfCell)
	{
		return new XlsRowFactory<>(localeCode,this,xfCell);
	}
	
	public XlsCellFactory<GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW> xlsCell(String localeCode, XlsStyleFactory<GROUP,COLUMN,ROW,STYLE,ALIGNMENT,CDT> xfStyle)
	{
		return new XlsCellFactory<>(localeCode,xfStyle);
	}
	
	public XlsStyleFactory<GROUP,COLUMN,ROW,STYLE,ALIGNMENT,CDT> xlsStyle(Workbook xlsWorkbook, List<GROUP> ioGroups, List<COLUMN> ioColumns, List<ROW> ioRows)
	{
		return new XlsStyleFactory<>(this,xlsWorkbook,ioGroups,ioColumns,ioRows);
	}
	
	public XmlReportFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT> xmlReport(Query q)
	{
		return new XmlReportFactory<>(q);
	}

	
	public TxtIoColumnFactory<COLUMN> tfColumn(String localeCode){return new TxtIoColumnFactory<COLUMN>(localeCode);}
	public TxtIoGroupFactory<SHEET,GROUP> tfGroup(String localeCode) {return new TxtIoGroupFactory<SHEET,GROUP>(localeCode);}
	
	public Comparator<REPORT> cpReport(EjbIoReportComparator.Type type) {return (new EjbIoReportComparator<CATEGORY,REPORT>()).instance(type);}
}