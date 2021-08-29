package org.jeesl.web.mbean.prototype.io.report;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.api.facade.system.JeeslExportRestFacade;
import org.jeesl.api.rest.system.io.report.JeeslIoReportRestExport;
import org.jeesl.controller.handler.sb.SbMultiHandler;
import org.jeesl.controller.handler.ui.helper.UiHelperIoReport;
import org.jeesl.controller.report.JeeslReportUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.factory.builder.system.ReportFactoryBuilder;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnGroupFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportRowFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportSheetFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportWorkbookFactory;
import org.jeesl.factory.xml.system.io.report.XmlReportFactory;
import org.jeesl.factory.xml.system.io.report.XmlReportsFactory;
import org.jeesl.interfaces.bean.sb.SbToggleBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportColumnComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportGroupComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportRowComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportSheetComparator;
import org.jeesl.util.filter.xml.system.io.XmlReportFilter;
import org.jeesl.util.query.xml.system.io.XmlReportQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;
import net.sf.ahtutils.xml.report.Report;
import net.sf.ahtutils.xml.report.Reports;
import net.sf.ahtutils.xml.report.XlsSheet;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.util.xml.JaxbUtil;

public class AbstractAdminIoReportDefinitionBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
						RT extends JeeslStatus<L,D,RT>,
						ENTITY extends EjbWithId,
						ATTRIBUTE extends EjbWithId,
						TL extends JeeslTrafficLight<L,D,TLS>,
						TLS extends JeeslTrafficLightScope<L,D,TLS,?>,
						FILLING extends JeeslStatus<L,D,FILLING>,
						TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>,
						RCAT extends JeeslRevisionCategory<L,D,RCAT,?>,	
						RE extends JeeslRevisionEntity<L,D,RCAT,?,RA,?>,
						RA extends JeeslRevisionAttribute<L,D,RE,?,CDT>
						>
	extends AbstractIoReportBean<L,D,LOC,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION,RCAT,RE,RA>
	implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminIoReportDefinitionBean.class);

	private List<RCAT> revisionCategories; public List<RCAT> getRevisionCategories() {return revisionCategories;}
	private List<CW> columnWidths; public List<CW> getColumnWidths() {return columnWidths;}
	private List<RT> rowTypes; public List<RT> getRowTypes() {return rowTypes;}
	private List<CDT> attributeTypes; public List<CDT> getAttributeTypes() {return attributeTypes;}
	private List<REPORT> reports; public List<REPORT> getReports() {return reports;}
	private List<IMPLEMENTATION> implementations; public List<IMPLEMENTATION> getImplementations() {return implementations;}
	private List<SHEET> sheets; public List<SHEET> getSheets() {return sheets;}
	private List<ROW> rows; public List<ROW> getRows() {return rows;}
	private List<GROUP> groups; public List<GROUP> getGroups() {return groups;}
	private List<COLUMN> columns; public List<COLUMN> getColumns() {return columns;}
	private List<TEMPLATE> templates; public List<TEMPLATE> getTemplates() {return templates;}
	private List<STYLE> styles; public List<STYLE> getStyles() {return styles;}
	private List<TLS> trafficLightScopes; public List<TLS> getTrafficLightScopes() {return trafficLightScopes;}
	
	private RCAT revisionCategory; public RCAT getRevisionCategory() {return revisionCategory;} public void setRevisionCategory(RCAT revisionCategory) {this.revisionCategory = revisionCategory;}
	private REPORT report; public REPORT getReport() {return report;} public void setReport(REPORT report) {this.report = report;}
	private SHEET sheet; public SHEET getSheet() {return sheet;} public void setSheet(SHEET sheet) {this.sheet = sheet;}
	private ROW row; public ROW getRow() {return row;} public void setRow(ROW row) {this.row = row;}
	private GROUP group; public GROUP getGroup() {return group;}public void setGroup(GROUP group) {this.group = group;}
	private COLUMN column; public COLUMN getColumn() {return column;} public void setColumn(COLUMN column) {this.column = column;}
	
	private UiHelperIoReport<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> uiHelper; public UiHelperIoReport<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> getUiHelper() {return uiHelper;}
	protected SbMultiHandler<CATEGORY> sbhCategory; public SbMultiHandler<CATEGORY> getSbhCategory() {return sbhCategory;}
	
	private Comparator<REPORT> comparatorReport;
	private Comparator<SHEET> comparatorSheet;
	private Comparator<GROUP> comparatorGroup;
	private Comparator<COLUMN> comparatorColumn;
	private Comparator<ROW> comparatorRow;
	
	private EjbIoReportFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> efReport;
	private EjbIoReportWorkbookFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> efWorkbook;
	private EjbIoReportSheetFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> efSheet;
	private EjbIoReportColumnGroupFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> efGroup;
	private EjbIoReportColumnFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> efColumn;
	private EjbIoReportRowFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> efRow;
	
	private final XmlReportFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> xfReport;
	private JeeslReportUpdater<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> reportUpdater;

	private String restUrl; public String getRestUrl() {return restUrl;} public void setRestUrl(String restUrl) {this.restUrl = restUrl;}
	protected JeeslFacade fRest;
	
	protected AbstractAdminIoReportDefinitionBean(final ReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fbReport)
	{
		super(fbReport);
		sbhCategory = new SbMultiHandler<CATEGORY>(fbReport.getClassCategory(),this);
		xfReport = fbReport.xmlReport(XmlReportQuery.get(XmlReportQuery.Key.exReport));
	}
	
	protected void postConstructReportDefinition(JeeslIoReportFacade<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fReport,
												JeeslFacade fRest,JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage)
	{
		super.initSuperReport(bTranslation,bMessage,fReport);
		this.fRest=fRest;
		
		efReport = fbReport.report();
		efWorkbook = fbReport.workbook();
		efSheet = fbReport.sheet();
		efGroup = fbReport.group();
		efColumn = fbReport.column();
		efRow = fbReport.row();
		
		reportUpdater = fbReport.ejbUpdater(fReport);
		
		uiHelper = new UiHelperIoReport<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>();
		
		revisionCategories = fReport.allOrderedPositionVisible(fbReport.getClassRevisionCategory());
		
		comparatorReport = new IoReportComparator<L,D,CATEGORY,REPORT>().factory(IoReportComparator.Type.position);
		comparatorSheet = new IoReportSheetComparator<CATEGORY,REPORT,WORKBOOK,SHEET>().factory(IoReportSheetComparator.Type.position);
		comparatorGroup = new IoReportGroupComparator<L,D,CATEGORY,REPORT,WORKBOOK,SHEET,GROUP>().factory(IoReportGroupComparator.Type.position);
		comparatorColumn = new IoReportColumnComparator<L,D,CATEGORY,REPORT,WORKBOOK,SHEET,GROUP,COLUMN>().factory(IoReportColumnComparator.Type.position);
		comparatorRow  = new IoReportRowComparator<L,D,CATEGORY,REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW>().factory(IoReportRowComparator.Type.position);
		
		implementations = fReport.allOrderedPositionVisible(fbReport.getClassImplementation());
		attributeTypes = fReport.allOrderedPositionVisible(fbReport.getClassCellDataType());
		columnWidths = fReport.allOrderedPositionVisible(fbReport.getClassColumnWidth());
		rowTypes = fReport.allOrderedPositionVisible(fbReport.getClassRowType());
		templates = fReport.allOrderedPositionVisible(fbReport.getClassTemplate());
		styles = fReport.allOrderedPositionVisible(fbReport.getClassStyle());
		trafficLightScopes = fReport.allOrderedPositionVisible(fbReport.getClassTrafficLightScope());
		
		reloadCategories();
		reloadReports();
	}
	
	protected void reloadCategories()
	{
		sbhCategory.setList(fReport.allOrderedPositionVisible(fbReport.getClassCategory()));
	}
	
	public void toggled(Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
		reloadReports();
		cancelReport();
	}
	
	
	private void reset(boolean rReport, boolean rSheet, boolean rRow, boolean rGroup, boolean rColumn)
	{
		if(rReport){report=null;}
		if(rSheet){sheet=null;}
		if(rRow){row=null;}
		if(rGroup){group=null;}
		if(rColumn){column=null;}
	}
	
	//*************************************************************************************
	private void reloadReports()
	{
		reports = fReport.fReports(sbhCategory.getSelected(), true);
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbReport.getClassReport(),reports));}
		Collections.sort(reports,comparatorReport);
	}
	
	public void addReport()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.addEntity(fbReport.getClassReport()));}
		report = efReport.build(null);
		report.setName(efLang.createEmpty(localeCodes));
		report.setDescription(efDescription.createEmpty(localeCodes));
		report.setWorkbook(efWorkbook.build(report));
		uiHelper.check(report);
		reset(false,true,true,true,true);
	}
	
	private void reloadReport()
	{
		if(report.getWorkbook()==null)
		{
			try
			{
				WORKBOOK wb = efWorkbook.build(report);
				fReport.saveTransaction(wb);
			}
			catch (JeeslConstraintViolationException e) {logger.error(e.getMessage());}
			catch (JeeslLockingException e) {logger.error(e.getMessage());}
		}
		report = fReport.load(report,false);
		sheets = report.getWorkbook().getSheets();
		
		Collections.sort(sheets, comparatorSheet);
	}
	
	public void selectReport() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(report));}
		report = fReport.find(fbReport.getClassReport(), report);
		report = efLang.persistMissingLangs(fReport,localeCodes,report);
		report = efDescription.persistMissingLangs(fReport,localeCodes,report);
		if(report.getWorkbook()==null)
		{
			report.setWorkbook(efWorkbook.build(report));
			report = fReport.saveTransaction(report);
		}
		reloadReport();
		reset(false,true,true,true,true);
		uiHelper.check(report);
	}
	
	public void saveReport() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(report));}
		if(report.getCategory()!=null){report.setCategory(fReport.find(fbReport.getClassCategory(), report.getCategory()));}
		report = fReport.save(report);
		reloadReports();
		reloadReport();
		bMessage.growlSuccessSaved();
		updatePerformed();
	}
	
	public void rmReport() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(report));}
		fReport.rm(report);
		reset(true,true,true,true,true);
		bMessage.growlSuccessRemoved();
		reloadReports();
		updatePerformed();
	}
	
	public void cancelReport()
	{
		reset(true,true,true,true,true);
		uiHelper.check(report);
		uiHelper.check(sheet);
		uiHelper.check(group);
	}
		
	//*************************************************************************************

	public void addSheet()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.addEntity(fbReport.getClassSheet()));}
		sheet = efSheet.build(report.getWorkbook());
		sheet.setName(efLang.createEmpty(localeCodes));
		sheet.setDescription(efDescription.createEmpty(localeCodes));
		uiHelper.check(sheet);
		reset(false,false,true,true,true);
	}
	
	public void selectSheet()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(sheet));}
		sheet = fReport.find(fbReport.getClassSheet(), sheet);
		if(sheet.getCode()==null)
		{
			try
			{
				sheet.setCode(UUID.randomUUID().toString());
				sheet = fReport.save(sheet);
			}
			catch (JeeslConstraintViolationException e) {logger.error(e.getMessage());}
			catch (JeeslLockingException e) {logger.error(e.getMessage());}
		}
		reloadSheet();
		reset(false,false,true,true,true);
		uiHelper.check(sheet);
		uiHelper.check(group);
	}
	
	private void reloadSheet()
	{
		sheet = fReport.load(sheet,false);
		groups = sheet.getGroups();
		rows = sheet.getRows();
		Collections.sort(groups, comparatorGroup);
		Collections.sort(rows,comparatorRow);
	}
		
	public void saveSheet() throws JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(sheet));}
		try
		{
			if(sheet.getImplementation()!=null){sheet.setImplementation(fReport.find(fbReport.getClassImplementation(), sheet.getImplementation()));}
			sheet = fReport.save(sheet);
			reloadReport();
			reloadSheet();
			bMessage.growlSuccessSaved();
			updatePerformed();
			uiHelper.check(sheet);
		}
		catch (JeeslConstraintViolationException e) {bMessage.errorConstraintViolationDuplicateObject();}
	}
		
	public void rmSheet() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(sheet));}
		fReport.rmSheet(sheet);
		reset(false,true,true,true,true);
		bMessage.growlSuccessRemoved();
		reloadReport();
		updatePerformed();
	}
	
	public void cancelSheet()
	{
		reset(false,true,true,true,true);
		uiHelper.check(sheet);
		uiHelper.check(group);
	}
	
	//*************************************************************************************

	public void addGroup()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.addEntity(fbReport.getClassGroup()));}
		group = efGroup.build(sheet,groups);
		group.setName(efLang.createEmpty(localeCodes));
		group.setDescription(efDescription.createEmpty(localeCodes));
		reset(false,false,true,false,true);
		uiHelper.check(group);
	}
	
	public void selectGroup()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(group));}
		group = fReport.find(fbReport.getClassGroup(), group);
		if(group.getCode()==null)
		{
			try
			{
				group.setCode(UUID.randomUUID().toString());
				group = fReport.save(group);
			}
			catch (JeeslConstraintViolationException e) {logger.error(e.getMessage());}
			catch (JeeslLockingException e) {logger.error(e.getMessage());}
		}
		reloadGroup();
		reset(false,false,true,false,true);
		uiHelper.check(group);
	}
	
	private void reloadGroup()
	{
		group = fReport.load(group);
		columns = fReport.allForParent(fbReport.getClassColumn(), group);
		Collections.sort(columns, comparatorColumn);
	}
	
	public void saveGroup() throws JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(group));}
		try
		{
			if(group.getStyleHeader()!=null){group.setStyleHeader(fReport.find(fbReport.getClassStyle(),group.getStyleHeader()));}
			group = fReport.save(group);
			reloadReport();
			reloadSheet();
			reloadGroup();
			bMessage.growlSuccessSaved();
			updatePerformed();
			uiHelper.check(group);
		}
		catch (JeeslConstraintViolationException e) {bMessage.errorConstraintViolationDuplicateObject();}
	}
	
	public void rmGroup() throws JeeslConstraintViolationException
	{
		fReport.rmGroup(group);
		reloadReport();
		reloadSheet();
		reset(false,false,true,true,true);
		uiHelper.check(group);
	}
	
	public void cancelGroup()
	{
		reset(false,false,true,true,true);
		uiHelper.check(group);
	}
	
	//*************************************************************************************

	public void addColumn()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.addEntity(fbReport.getClassColumn()));}
		column = efColumn.build(group,columns);
		column.setName(efLang.createEmpty(localeCodes));
		column.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void selectColumn()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(column));}
		if(column.getCode()==null)
		{
			try
			{
				column.setCode(UUID.randomUUID().toString());
				column = fReport.save(column);
			}
			catch (JeeslConstraintViolationException e) {logger.error(e.getMessage());}
			catch (JeeslLockingException e) {logger.error(e.getMessage());}
		}
		reloadColumn();
	}
	
	private void reloadColumn()
	{
		column = fReport.find(fbReport.getClassColumn(), column);
	}
	
	public void saveColumn() throws JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(column));}
		try
		{
			if(column.getDataType()!=null) {column.setDataType(fReport.find(fbReport.getClassCellDataType(),column.getDataType()));}
			if(column.getColumWidth()!=null) {column.setColumWidth(fReport.find(fbReport.getClassColumnWidth(),column.getColumWidth()));}
			if(column.getTrafficLightScope()!=null) {column.setTrafficLightScope(fReport.find(fbReport.getClassTrafficLightScope(),column.getTrafficLightScope()));}
			column.setGroup(fReport.find(fbReport.getClassGroup(),column.getGroup()));
			column = fReport.save(column);
		
			reloadReport();
			reloadSheet();
			reloadGroup();
			reloadColumn();
			bMessage.growlSuccessSaved();
			updatePerformed();
		}
		catch (JeeslConstraintViolationException e) {bMessage.errorConstraintViolationDuplicateObject();}
	}
		
	public void rmColumn() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(column));}
		fReport.rmColumn(column);
		column=null;
		bMessage.growlSuccessRemoved();
		reloadGroup();
		updatePerformed();
	}
	
	public void cancelColumn()
	{
		reset(false,false,false,false,true);
	}
	
	public void changeColumnDataType()
	{
		if(column.getDataType()!=null){column.setDataType(fReport.find(fbReport.getClassCellDataType(),column.getDataType()));}
	}
	
	public void changeRevisionCategory()
	{
		revisionCategory = fReport.find(fbReport.getClassRevisionCategory(), revisionCategory);
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(revisionCategory));}
	}
	
	//*************************************************************************************
	
	public void addRow()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.addEntity(fbReport.getClassRow()));}
		row = efRow.build(sheet);
		row.setName(efLang.createEmpty(localeCodes));
		row.setDescription(efDescription.createEmpty(localeCodes));
		reset(false,false,false,true,true);
	}
	
	public void selectRow()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(row));}
		reloadRow();
	}
	
	private void reloadRow()
	{
		row = fReport.find(fbReport.getClassRow(),row);
	}
	
	public void saveRow() throws JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(row));}
		try
		{
			if(row.getDataType()!=null){row.setDataType(fReport.find(fbReport.getClassCellDataType(),row.getDataType()));}
			if(row.getTemplate()!=null){row.setTemplate(fReport.find(fbReport.getClassTemplate(), row.getTemplate()));}
			row.setType(fReport.find(fbReport.getClassRowType(),row.getType()));
			row = fReport.save(row);
			reloadReport();
			reloadSheet();
			reloadRow();
			bMessage.growlSuccessSaved();
			updatePerformed();
		}
		catch (JeeslConstraintViolationException e) {bMessage.errorConstraintViolationDuplicateObject();}
	}
	
	public void rmRow() throws JeeslConstraintViolationException
	{
		fReport.rmRow(row);
		reloadReport();
		reloadSheet();
		cancelRow();
	}
	public void cancelRow(){reset(false,false,true,true,true);}
	
	public void cloneReport() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, UtilsProcessingException
	{
		logger.info("Cloning "+fbReport.getClassReport().getSimpleName());
		Report xml  = xfReport.build(report);
		reportUpdater.cloneIoReport(xml);
		reloadReports();
	}
	
	public void cloneSheet() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, UtilsProcessingException, ExlpXpathNotFoundException
	{
		logger.info("Cloning "+fbReport.getClassSheet().getSimpleName());
		Report xml  = xfReport.build(report);
		JaxbUtil.info(xml);
		XlsSheet xSheet  = XmlReportFilter.fSheet(xml,sheet.getCode());
		sheet = reportUpdater.cloneIoSheet(report,xSheet);
		reloadReport();
	}
    
	//*************************************************************************************
	public void reorderReports() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fReport, fbReport.getClassReport(), reports);Collections.sort(reports, comparatorReport);}
	public void reorderSheets() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fReport, fbReport.getClassSheet(), sheets);Collections.sort(sheets, comparatorSheet);}
	public void reorderGroups() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fReport, fbReport.getClassGroup(), groups);Collections.sort(groups, comparatorGroup);}
	public void reorderColumns() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fReport, fbReport.getClassColumn(), columns);Collections.sort(columns, comparatorColumn);}
	public void reorderRows() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fReport, fbReport.getClassRow(), rows);Collections.sort(rows, comparatorRow);}
	
	protected void updatePerformed(){}	
	
	@SuppressWarnings("rawtypes")
	@Override protected void updateSecurity2(JeeslJsfSecurityHandler jsfSecurityHandler, String actionDeveloper)
	{
		uiAllowSave = jsfSecurityHandler.allow(actionDeveloper);

		if(logger.isTraceEnabled())
		{
			logger.info(uiAllowSave+" allowSave ("+actionDeveloper+")");
		}
	}
	
	public  void download() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UtilsConfigurationException, JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, UtilsProcessingException
	{
		logger.info("Downloading Report from REST: "+restUrl);
	
		Reports xml = null;
		if(fRest instanceof JeeslExportRestFacade)
		{
			logger.info("Using Facade Connection (JBoss EAP6)");
			xml = ((JeeslExportRestFacade)fRest).exportIoReport(report.getCode());
			xml = XmlReportsFactory.build();
		}
		else
		{
			logger.info("Using Direct Connection (JBoss EAP7)");
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget restTarget = client.target(restUrl);
			JeeslIoReportRestExport rest = restTarget.proxy(JeeslIoReportRestExport.class);
			xml = rest.exportSystemIoReport(report.getCode());
		}

		JaxbUtil.info(xml);
		if(!xml.getReport().isEmpty()) {reportUpdater.importSystemIoReport(xml.getReport().get(0));}
        
        reloadReports();
	}
}