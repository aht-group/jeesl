package org.jeesl.web.rest.system.io;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.api.rest.i.io.report.JeeslIoReportExportRestInterface;
import org.jeesl.api.rest.i.io.report.JeeslIoReportImportRestInterface;
import org.jeesl.controller.io.db.updater.JeeslDbCodeEjbUpdater;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.controller.report.JeeslReportUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.factory.builder.io.IoReportFactoryBuilder;
import org.jeesl.factory.ejb.io.report.EjbIoReportCellFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportStyleFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportTemplateFactory;
import org.jeesl.factory.xml.io.locale.status.XmlTypeFactory;
import org.jeesl.factory.xml.system.io.report.XmlReportFactory;
import org.jeesl.factory.xml.system.io.report.XmlReportsFactory;
import org.jeesl.factory.xml.system.io.report.XmlStyleFactory;
import org.jeesl.factory.xml.system.io.report.XmlStylesFactory;
import org.jeesl.factory.xml.system.io.report.XmlTemplateFactory;
import org.jeesl.factory.xml.system.io.report.XmlTemplatesFactory;
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
import org.jeesl.model.xml.io.report.Cell;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Reports;
import org.jeesl.model.xml.io.report.Style;
import org.jeesl.model.xml.io.report.Styles;
import org.jeesl.model.xml.io.report.Template;
import org.jeesl.model.xml.io.report.Templates;
import org.jeesl.model.xml.io.ssi.sync.DataUpdate;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportStyleComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportTemplateComparator;
import org.jeesl.util.query.xml.system.io.XmlReportQuery;
import org.jeesl.web.rest.AbstractJeeslRestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
									STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
									ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
									CDT extends JeeslReportCellType<L,D,CDT,?>,
									CW extends JeeslReportColumnWidth<L,D,CW,?>,
									RT extends JeeslReportRowType<L,D,RT,?>,
									RCAT extends JeeslStatus<L,D,RCAT>,
									ENTITY extends EjbWithId,
									ATTRIBUTE extends EjbWithId,
									TL extends JeeslTrafficLight<L,D,TLS>,
									TLS extends JeeslTrafficLightScope<L,D,TLS,?>>
					extends AbstractJeeslRestHandler<L,D>
					implements JeeslIoReportExportRestInterface,JeeslIoReportImportRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(IoReportRestService.class);
	
	private JeeslIoReportFacade<REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL> fReport;
	
	final IoReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,?,?> fbReport;

	private XmlReportFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT> xfReport;
	private XmlTemplateFactory<L,D,TEMPLATE,CELL> xfTemplate;
	private XmlStyleFactory<L,D,GROUP,COLUMN,ROW,STYLE,ALIGNMENT,CDT,CW> xfStyle;
		
	private final JeeslReportUpdater<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT> reportUpdater;

	private EjbIoReportTemplateFactory<L,D,TEMPLATE,CELL,STYLE,CDT,CW> efTemplate;
	private EjbIoReportCellFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT> efCell;
	private EjbIoReportStyleFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT> efStyle;
		
	private Comparator<REPORT> comparatorReport;
	private Comparator<TEMPLATE> comparatorTemplate;
	private Comparator<STYLE> comparatorStyle;
	
	private IoReportRestService(JeeslIoReportFacade<REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL> fReport,
			final IoReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,?,?> fbReport)
	{
		super(fReport,fbReport.getClassL(),fbReport.getClassD());
		this.fbReport=fbReport;
		this.fReport=fReport;
		
		reportUpdater = new JeeslReportUpdater<>(fReport,fbReport);
		
		xfReport = new XmlReportFactory<>(XmlReportQuery.get(XmlReportQuery.Key.exReport));
		xfTemplate = new XmlTemplateFactory<>(XmlReportQuery.exTemplate());
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
					STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
					ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
					CDT extends JeeslReportCellType<L,D,CDT,?>,
					CW extends JeeslReportColumnWidth<L,D,CW,?>,
					RT extends JeeslReportRowType<L,D,RT,?>,
					RCAT extends JeeslStatus<L,D,RCAT>,
					ENTITY extends EjbWithId,
					ATTRIBUTE extends EjbWithId,
					TL extends JeeslTrafficLight<L,D,TLS>,
					TLS extends JeeslTrafficLightScope<L,D,TLS,?>>
	IoReportRestService<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS>
			factory(JeeslIoReportFacade<REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL> fReport,
					final IoReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,?,?> fbReport)
	{
		return new IoReportRestService<>(fReport,fbReport);
	}
	
	@Override public Container exportSystemIoReportCategories() {return xfContainer.build(fReport.allOrderedPosition(fbReport.getClassCategory()));}

	@Override public Reports exportSystemIoReports()
	{
		Reports reports = XmlReportsFactory.build();
		List<REPORT> list = fReport.all(fbReport.getClassReport());
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
			REPORT r = fReport.fByCode(fbReport.getClassReport(), code);
			reports.getReport().add(xfReport.build(r));
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		return reports;
	}
	
	@Override public Templates exportSystemIoReportTemplates()
	{
		Templates templates = XmlTemplatesFactory.build();
		List<TEMPLATE> list = fReport.all(fbReport.getClassTemplate());
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
		List<STYLE> list = fReport.all(fbReport.getClassStyle());
		Collections.sort(list,comparatorStyle);
		for(STYLE style : list)
		{
			styles.getStyle().add(xfStyle.build(style));
		}
		return styles;
	}
	
	@Override public DataUpdate importSystemIoReportCategories(Container categories){return importStatus(fbReport.getClassCategory(),categories,null);}	
	@Override public DataUpdate importSystemIoReportTemplates(Templates templates)
	{
		DataUpdateTracker dut = new DataUpdateTracker(true);
		dut.setType(XmlTypeFactory.build(fbReport.getClassTemplate().getName(),"DB Import"));
		
		JeeslDbCodeEjbUpdater<TEMPLATE> dbUpdaterTemplate = JeeslDbCodeEjbUpdater.instance(fbReport.getClassTemplate());
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
		try {eTemplate = fReport.fByCode(fbReport.getClassTemplate(), xTemplate.getCode());}
		catch (JeeslNotFoundException e)
		{
			eTemplate = efTemplate.build(xTemplate);
			eTemplate = fReport.save(eTemplate);
		}
		eTemplate = efTemplate.update(eTemplate, xTemplate);
		eTemplate = fReport.save(eTemplate);
		eTemplate = efTemplate.updateLD(fReport,eTemplate,xTemplate);
		eTemplate = fReport.load(eTemplate);	
		
		JeeslDbCodeEjbUpdater<CELL> dbuCell = JeeslDbCodeEjbUpdater.instance(fbReport.getClassCell());
		
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
		try {eCell = fReport.fByCode(fbReport.getClassCell(), xCell.getCode());}
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
		dut.setType(XmlTypeFactory.build(fbReport.getClassStyle().getName(),"DB Import"));
		
		JeeslDbCodeEjbUpdater<STYLE> dbuStyle = JeeslDbCodeEjbUpdater.instance(fbReport.getClassStyle());
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
		try {eStyle = fReport.fByCode(fbReport.getClassStyle(), xStyle.getCode());}
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
		dut.setType(XmlTypeFactory.build(fbReport.getClassReport().getName(),"DB Import"));
		
		JeeslDbCodeEjbUpdater<REPORT> dbUpdaterReport = JeeslDbCodeEjbUpdater.instance(fbReport.getClassReport());
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
		dut.setType(XmlTypeFactory.build(fbReport.getClassReport().getName(),"DB Import"));
		try {reportUpdater.importSystemIoReport(report);}
		catch (JeeslNotFoundException e) {dut.fail(e, true);}
		catch (JeeslConstraintViolationException e) {dut.fail(e, true);}
		catch (JeeslLockingException e) {dut.fail(e, true);}
		catch (UtilsProcessingException e) {dut.fail(e, true);}
		return dut.toDataUpdate();
	}
}