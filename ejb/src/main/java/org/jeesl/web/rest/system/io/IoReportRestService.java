package org.jeesl.web.rest.system.io;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.api.rest.system.io.report.JeeslIoReportRestExport;
import org.jeesl.api.rest.system.io.report.JeeslIoReportRestImport;
import org.jeesl.controller.db.updater.JeeslDbCodeEjbUpdater;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.controller.report.JeeslReportUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.factory.builder.system.ReportFactoryBuilder;
import org.jeesl.factory.ejb.io.report.EjbIoReportCellFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportStyleFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportTemplateFactory;
import org.jeesl.factory.xml.system.io.report.XmlReportFactory;
import org.jeesl.factory.xml.system.io.report.XmlReportsFactory;
import org.jeesl.factory.xml.system.io.report.XmlStyleFactory;
import org.jeesl.factory.xml.system.io.report.XmlStylesFactory;
import org.jeesl.factory.xml.system.io.report.XmlTemplateFactory;
import org.jeesl.factory.xml.system.io.report.XmlTemplatesFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
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
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportStyleComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportTemplateComparator;
import org.jeesl.util.query.xml.system.io.XmlReportQuery;
import org.jeesl.web.rest.AbstractJeeslRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.report.Cell;
import net.sf.ahtutils.xml.report.Report;
import net.sf.ahtutils.xml.report.Reports;
import net.sf.ahtutils.xml.report.Style;
import net.sf.ahtutils.xml.report.Styles;
import net.sf.ahtutils.xml.report.Template;
import net.sf.ahtutils.xml.report.Templates;
import net.sf.ahtutils.xml.sync.DataUpdate;

public class IoReportRestService <L extends JeeslLang, D extends JeeslDescription,
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
									STYLE extends JeeslReportStyle<L,D>,
									CDT extends JeeslStatus<L,D,CDT>,
									CW extends JeeslStatus<L,D,CW>,
									RT extends JeeslReportRowType<L,D,RT,?>,
									RCAT extends JeeslStatus<L,D,RCAT>,
									ENTITY extends EjbWithId,
									ATTRIBUTE extends EjbWithId,
									TL extends JeeslTrafficLight<L,D,TLS>,
									TLS extends JeeslTrafficLightScope<L,D,TLS,?>,
									FILLING extends JeeslStatus<L,D,FILLING>,
									TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>,
									AGGREGATION extends JeeslStatus<L,D,AGGREGATION>>
					extends AbstractJeeslRestService<L,D>
					implements JeeslIoReportRestExport,JeeslIoReportRestImport
{
	final static Logger logger = LoggerFactory.getLogger(IoReportRestService.class);
	
	private JeeslIoReportFacade<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fReport;
	
	private final Class<CATEGORY> cCategory;
	private final Class<REPORT> cReport;
	private final Class<IMPLEMENTATION> cImplementation;
	private final Class<TEMPLATE> cTemplate;
	private final Class<CELL> cCell;
	private final Class<STYLE> cStyle;
	private final Class<CW> cColumWidth;
	private final Class<RT> cRt;
	private final Class<FILLING> cFilling;
	private final Class<TRANSFORMATION> cTransformation;
	private final Class<AGGREGATION> cAggregation;

	private XmlReportFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> xfReport;
	private XmlTemplateFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS> xfTemplate;
	private XmlStyleFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE> xfStyle;
		
	private final JeeslReportUpdater<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> reportUpdater;
	

	private EjbIoReportTemplateFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> efTemplate;
	private EjbIoReportCellFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> efCell;
	private EjbIoReportStyleFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> efStyle;
		
	private Comparator<REPORT> comparatorReport;
	private Comparator<TEMPLATE> comparatorTemplate;
	private Comparator<STYLE> comparatorStyle;
	
	private IoReportRestService(JeeslIoReportFacade<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fReport,
			final ReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fbReport,
			final Class<L> cL, final Class<D> cD, Class<CATEGORY> cCategory, final Class<REPORT> cReport, final Class<WORKBOOK> cWorkbook, final Class<SHEET> cSheet, final Class<GROUP> cGroup, final Class<COLUMN> cColumn, final Class<ROW> cRow, final Class<TEMPLATE> cTemplate, final Class<CELL> cCell, final Class<STYLE> cStyle, final Class<CDT> cDataType, final Class<CW> cColumWidth, final Class<RT> cRt, final Class<FILLING> cFilling, final Class<TRANSFORMATION> cTransformation,final Class<IMPLEMENTATION> cImplementation,final Class<AGGREGATION> cAggregation)
	{
		super(fReport,cL,cD);
		this.fReport=fReport;
		
		this.cCategory=cCategory;
		this.cReport=cReport;
		this.cImplementation=cImplementation;
		this.cTemplate=cTemplate;
		this.cCell=cCell;
		this.cStyle=cStyle;
		this.cColumWidth=cColumWidth;
		this.cRt=cRt;
		
		this.cFilling=cFilling;
		this.cTransformation=cTransformation;
		this.cAggregation=cAggregation;
		
		reportUpdater = new JeeslReportUpdater<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(fReport,fbReport);
		
		xfReport = new XmlReportFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(XmlReportQuery.get(XmlReportQuery.Key.exReport));
		xfTemplate = new XmlTemplateFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>(XmlReportQuery.exTemplate());
		xfStyle = new XmlStyleFactory<>(XmlReportQuery.exStyle());
		
		efTemplate = fbReport.template();
		efCell = fbReport.cell();
		efStyle = fbReport.style();
		
		comparatorReport = new IoReportComparator<L,D,CATEGORY,REPORT>().factory(IoReportComparator.Type.position);
		comparatorTemplate = new IoReportTemplateComparator<TEMPLATE>().factory(IoReportTemplateComparator.Type.position);
		comparatorStyle = new IoReportStyleComparator<STYLE>().factory(IoReportStyleComparator.Type.position);
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
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
					STYLE extends JeeslReportStyle<L,D>,
					CDT extends JeeslStatus<L,D,CDT>,CW extends JeeslStatus<L,D,CW>,
					RT extends JeeslReportRowType<L,D,RT,?>,
					RCAT extends JeeslStatus<L,D,RCAT>,
					ENTITY extends EjbWithId,
					ATTRIBUTE extends EjbWithId,
					TL extends JeeslTrafficLight<L,D,TLS>,
					TLS extends JeeslTrafficLightScope<L,D,TLS,?>,
					FILLING extends JeeslStatus<L,D,FILLING>,
					TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>,
					AGGREGATION extends JeeslStatus<L,D,AGGREGATION>>
	IoReportRestService<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION,AGGREGATION>
			factory(JeeslIoReportFacade<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fReport,
					final ReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fbReport,
					final Class<L> cL, final Class<D> cD, Class<CATEGORY> cCategory, final Class<REPORT> cReport, final Class<WORKBOOK> cWorkbook, final Class<SHEET> cSheet, final Class<GROUP> cGroup, final Class<COLUMN> cColumn, final Class<ROW> cRow, final Class<TEMPLATE> cTemplate, final Class<CELL> cCell, final Class<STYLE> cStyle, final Class<CDT> cDataType, final Class<CW> cColumWidth, final Class<RT> cRt, final Class<IMPLEMENTATION> cImplementation, final Class<FILLING> cFilling,final Class<TRANSFORMATION> cTransformation,final Class<AGGREGATION> cAggregation)
	{
		return new IoReportRestService<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION,AGGREGATION>(fReport,fbReport,cL,cD,cCategory,cReport,cWorkbook,cSheet,cGroup,cColumn,cRow,cTemplate,cCell,cStyle,cDataType,cColumWidth,cRt,cFilling,cTransformation,cImplementation,cAggregation);
	}
	
	@Override public Container exportSystemIoReportCategories() {return xfContainer.build(fReport.allOrderedPosition(cCategory));}
	@Override public Container exportSystemIoReportAggregation() {return xfContainer.build(fReport.allOrderedPosition(cAggregation));}
	@Override public Container exportSystemIoReportSettingFilling() {return xfContainer.build(fReport.allOrderedPosition(cFilling));}
	@Override public Container exportSystemIoReportSettingTransformation() {return xfContainer.build(fReport.allOrderedPosition(cTransformation));}
	@Override public Container exportSystemIoReportSettingImplementation() {return xfContainer.build(fReport.allOrderedPosition(cImplementation));}
	@Override public Container exportSystemIoReportRowType() {return xfContainer.build(fReport.allOrderedPosition(cRt));}
	@Override public Container exportSystemIoReportColumnWidth() {return xfContainer.build(fReport.allOrderedPosition(cColumWidth));}

	@Override public Container exportSystemIoReportStyleAlignment() {logger.warn("NYI****************************");return xfContainer.build(fReport.allOrderedPosition(cColumWidth));}
	
	@Override public Reports exportSystemIoReports()
	{
		Reports reports = XmlReportsFactory.build();
		List<REPORT> list = fReport.all(cReport);
		Collections.sort(list,comparatorReport);
		for(REPORT report : list)
		{
			reports.getReport().add(xfReport.build(report));
		}
		return reports;
	}
	
	@Override public Reports exportSystemIoReport(String code)
	{
		Reports reports = XmlReportsFactory.build();
		try
		{
			REPORT r = fReport.fByCode(cReport, code);
			reports.getReport().add(xfReport.build(r));
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		return reports;
	}
	
	@Override public Templates exportSystemIoReportTemplates()
	{
		Templates templates = XmlTemplatesFactory.build();
		List<TEMPLATE> list = fReport.all(cTemplate);
		Collections.sort(list,comparatorTemplate);
		for(TEMPLATE template : list)
		{
			templates.getTemplate().add(xfTemplate.build(template));
		}
		return templates;
	}
	
	@Override public Styles exportSystemIoReportStyles()
	{
		Styles styles = XmlStylesFactory.build();
		List<STYLE> list = fReport.all(cStyle);
		Collections.sort(list,comparatorStyle);
		for(STYLE style : list)
		{
			styles.getStyle().add(xfStyle.build(style));
		}
		return styles;
	}
	
	@Override public DataUpdate importSystemIoReportCategories(Container categories){return importStatus(cCategory,categories,null);}
	@Override public DataUpdate importSystemIoReportAggregation(Container container){return importStatus(cAggregation,container,null);}
	@Override public DataUpdate importSystemIoReportSettingFilling(Container types){return importStatus(cFilling,types,null);}
	@Override public DataUpdate importSystemIoReportSettingTransformation(Container types){return importStatus(cTransformation,types,null);}
	@Override public DataUpdate importSystemIoReportSettingImplementation(Container types){return importStatus(cImplementation,types,null);}
	@Override public DataUpdate importSystemIoReportRowType(Container types){return importStatus(cRt,types,null);}
	@Override public DataUpdate importSystemIoReportColumnWidth(Container container){return importStatus(cColumWidth,container,null);}
	
	@Override public DataUpdate importSystemIoReportStyleAlignment(Container container) {logger.warn("NYI****************************");return importStatus(cColumWidth,container,null);}
	
	@Override public DataUpdate importSystemIoReportTemplates(Templates templates)
	{
		DataUpdateTracker dut = new DataUpdateTracker(true);
		dut.setType(XmlTypeFactory.build(cTemplate.getName(),"DB Import"));
		
		JeeslDbCodeEjbUpdater<TEMPLATE> dbUpdaterTemplate = JeeslDbCodeEjbUpdater.createFactory(cTemplate);
		dbUpdaterTemplate.dbEjbs(fReport);
		
		for(Template xTemplate : templates.getTemplate())
		{
			try
			{
				TEMPLATE eTemplate = importSystemIoReportTemplate(xTemplate);
				dbUpdaterTemplate.handled(eTemplate);
				dut.success();
			}
			catch (JeeslNotFoundException e) {dut.fail(e, true);}
			catch (JeeslConstraintViolationException e) {dut.fail(e, true);}
			catch (JeeslLockingException e) {dut.fail(e, true);}
			catch (UtilsProcessingException e) {dut.fail(e, true);}
		}
		dbUpdaterTemplate.remove(fReport);
		
		return dut.toDataUpdate();
	}
	
	private TEMPLATE importSystemIoReportTemplate(Template xTemplate) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, UtilsProcessingException
	{
		TEMPLATE eTemplate;
		try {eTemplate = fReport.fByCode(cTemplate, xTemplate.getCode());}
		catch (JeeslNotFoundException e)
		{
			eTemplate = efTemplate.build(xTemplate);
			eTemplate = fReport.save(eTemplate);
		}
		eTemplate = efTemplate.update(eTemplate, xTemplate);
		eTemplate = fReport.save(eTemplate);
		eTemplate = efTemplate.updateLD(fReport,eTemplate,xTemplate);
		eTemplate = fReport.load(eTemplate);	
		
		JeeslDbCodeEjbUpdater<CELL> dbuCell = JeeslDbCodeEjbUpdater.createFactory(cCell);
		
		dbuCell.dbEjbs(eTemplate.getCells());
		
		for(Cell xCell : xTemplate.getCell())
		{
			CELL eCell = importCell(eTemplate,xCell);
			dbuCell.handled(eCell);
		}
		for(CELL c : dbuCell.getEjbForRemove()){fReport.rmCell(c);}
			
		return eTemplate;
	}
	
	private CELL importCell(TEMPLATE eTemplate, Cell xCell) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, UtilsProcessingException
	{
		CELL eCell;
		try {eCell = fReport.fByCode(cCell, xCell.getCode());}
		catch (JeeslNotFoundException e)
		{
			eCell = efCell.build(eTemplate,xCell);
			eCell = fReport.save(eCell);
		}
		eCell = efCell.update(eCell,xCell);
		eCell = fReport.save(eCell);
		eCell = efCell.updateLD(fReport, eCell, xCell);
		
		return eCell;
	}
	
	@Override public DataUpdate importSystemIoReportStyles(Styles styles)
	{
		DataUpdateTracker dut = new DataUpdateTracker(true);
		dut.setType(XmlTypeFactory.build(cStyle.getName(),"DB Import"));
		
		JeeslDbCodeEjbUpdater<STYLE> dbuStyle = JeeslDbCodeEjbUpdater.createFactory(cStyle);
		dbuStyle.dbEjbs(fReport);
		
		for(Style xStyle : styles.getStyle())
		{
			try
			{
				STYLE eStyle = importSystemIoReportStyle(xStyle);
				dbuStyle.handled(eStyle);
				dut.success();
			}
			catch (JeeslNotFoundException e) {dut.fail(e, true);}
			catch (JeeslConstraintViolationException e) {dut.fail(e, true);}
			catch (JeeslLockingException e) {dut.fail(e, true);}
			catch (UtilsProcessingException e) {dut.fail(e, true);}
		}
		dbuStyle.remove(fReport);
		
		return dut.toDataUpdate();
	}
	
	private STYLE importSystemIoReportStyle(Style xStyle) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, UtilsProcessingException
	{
		STYLE eStyle;
		try {eStyle = fReport.fByCode(cStyle, xStyle.getCode());}
		catch (JeeslNotFoundException e)
		{
			eStyle = efStyle.build(xStyle);
			eStyle = fReport.save(eStyle);
		}
		eStyle = efStyle.update(eStyle,xStyle);
		eStyle = fReport.save(eStyle);
		eStyle = efStyle.updateLD(fReport,eStyle,xStyle);
		return eStyle;
	}
	
	@Override public DataUpdate importSystemIoReports(Reports reports)
	{
		DataUpdateTracker dut = new DataUpdateTracker(true);
		dut.setType(XmlTypeFactory.build(cReport.getName(),"DB Import"));
		
		JeeslDbCodeEjbUpdater<REPORT> dbUpdaterReport = JeeslDbCodeEjbUpdater.createFactory(cReport);
		dbUpdaterReport.dbEjbs(fReport);
		
		for(Report xReport : reports.getReport())
		{
			try
			{
				REPORT eReport = reportUpdater.importSystemIoReport(xReport);
				dbUpdaterReport.handled(eReport);
				dut.success();
			}
			catch (JeeslNotFoundException e) {dut.fail(e, true);}
			catch (JeeslConstraintViolationException e) {dut.fail(e, true);}
			catch (JeeslLockingException e) {dut.fail(e, true);}
			catch (UtilsProcessingException e) {dut.fail(e, true);}
		}
		dbUpdaterReport.remove(fReport);
		
		return dut.toDataUpdate();
	}
	
	@Override
	public DataUpdate importSystemIoReport(Report report)
	{
		DataUpdateTracker dut = new DataUpdateTracker(true);
		dut.setType(XmlTypeFactory.build(cReport.getName(),"DB Import"));
		try {reportUpdater.importSystemIoReport(report);}
		catch (JeeslNotFoundException e) {dut.fail(e, true);}
		catch (JeeslConstraintViolationException e) {dut.fail(e, true);}
		catch (JeeslLockingException e) {dut.fail(e, true);}
		catch (UtilsProcessingException e) {dut.fail(e, true);}
		return dut.toDataUpdate();
	}
}