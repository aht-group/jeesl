package org.jeesl.controller.report;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.controller.io.db.updater.JeeslDbCodeEjbUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.factory.builder.io.IoReportFactoryBuilder;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnGroupFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportRowFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportSheetFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportWorkbookFactory;
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
import org.jeesl.model.xml.io.report.ColumnGroup;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Row;
import org.jeesl.model.xml.io.report.Rows;
import org.jeesl.model.xml.io.report.XlsColumn;
import org.jeesl.model.xml.io.report.XlsSheet;
import org.jeesl.model.xml.io.report.XlsWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;

public class JeeslReportUpdater <L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,?>,
								ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,TEMPLATE>,
								STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
								ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
								CDT extends JeeslReportCellType<L,D,CDT,?>,
								CW extends JeeslReportColumnWidth<L,D,CW,?>,
								RT extends JeeslReportRowType<L,D,RT,?>,
								RCAT extends JeeslStatus<L,D,RCAT>
>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslReportUpdater.class);
	
	private final boolean debugCreation = false;
	
	private final JeeslIoReportFacade<REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL> fReport;
	private final IoReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT,?,?,?,?,?,?> fbReport;

	private final EjbIoReportFactory<L,D,CATEGORY,REPORT> efReport;
	private final EjbIoReportWorkbookFactory<REPORT,WORKBOOK> efWorkbook;
	private final EjbIoReportSheetFactory<L,D,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,STYLE> efSheet;
	private final EjbIoReportColumnGroupFactory<L,D,SHEET,GROUP,COLUMN,STYLE> efGroup;
	private final EjbIoReportRowFactory<L,D,SHEET,GROUP,ROW,TEMPLATE,STYLE,CDT,RT> efRow;
	private final EjbIoReportColumnFactory<L,D,SHEET,GROUP,COLUMN,ROW,CELL,STYLE,CDT,CW,RT> efColumn;

	public JeeslReportUpdater(JeeslIoReportFacade<REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL> fReport,
			final IoReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT,?,?,?,?,?,?> fbReport)
	{
		this.fReport=fReport;
		this.fbReport=fbReport;
		
		efReport = fbReport.report();
		efWorkbook = fbReport.workbook();
		efSheet = fbReport.sheet();
		efGroup = fbReport.group();
		efColumn = fbReport.column();
		efRow = fbReport.row();
	}
	
	public REPORT cloneIoReport(Report xReport) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, UtilsProcessingException
	{
		generateUuidsForCloning(xReport);
		return importSystemIoReport(xReport);
	}
	
	public SHEET cloneIoSheet(REPORT eReport, XlsSheet xSheet) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, UtilsProcessingException, ExlpXpathNotFoundException
	{
		generateUuidsForCloning(xSheet);
		return importSheet(eReport.getWorkbook(),xSheet);
	}
	
	private void generateUuidsForCloning(Report xReport)
	{
		xReport.setCode(UUID.randomUUID().toString());
		if(Objects.nonNull(xReport.getXlsWorkbook()) && ObjectUtils.isNotEmpty(xReport.getXlsWorkbook().getXlsSheets()))
		{
			for(XlsSheet xSheet : xReport.getXlsWorkbook().getXlsSheets().getXlsSheet())
			{
				generateUuidsForCloning(xSheet);
			}
		}
	}
	
	private void generateUuidsForCloning(XlsSheet xSheet)
	{
		xSheet.setCode(UUID.randomUUID().toString());
		for(Serializable s : xSheet.getContent())
		{
			if(s instanceof ColumnGroup)
			{
				ColumnGroup xGroup = (ColumnGroup)s;
				xGroup.setCode(UUID.randomUUID().toString());
				for(XlsColumn xColumn : xGroup.getXlsColumn())
				{
					xColumn.setCode(UUID.randomUUID().toString());
				}
			}
			else if(s instanceof Rows)
			{
				Rows xRows = (Rows)s;
				for(Row xRow : xRows.getRow())
				{
					xRow.setCode(UUID.randomUUID().toString());
				}
			}
		}
	}
	
	public REPORT importSystemIoReport(Report xReport) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, UtilsProcessingException
	{
		REPORT eReport;
		
		try
		{
			eReport = fReport.fByCode(fbReport.getClassReport(), xReport.getCode());
			if(debugCreation) {logger.debug("Existing "+fbReport.getClassReport());}
		}
		catch (JeeslNotFoundException e)
		{
			if(debugCreation) {logger.debug("New "+fbReport.getClassReport());}
			eReport = efReport.build(fReport,xReport);
			eReport = fReport.save(eReport);
		}
		eReport = efReport.update(fReport,eReport, xReport);
		
		eReport = fReport.save(eReport);
		eReport = efReport.updateLD(fReport,eReport,xReport);
				
		if(Objects.nonNull(xReport.getXlsWorkbook()))
		{
			importWorkbook(eReport,xReport.getXlsWorkbook());
		}
		
		return eReport;
	}
	
	private REPORT importWorkbook(REPORT eReport, XlsWorkbook xWorkbook) throws JeeslConstraintViolationException, JeeslLockingException, UtilsProcessingException
	{
		WORKBOOK eWorkbook;
		if(eReport.getWorkbook()!=null)
		{
			eWorkbook = eReport.getWorkbook();
			if(debugCreation) {logger.debug("Existing WB");}
		}
		else
		{
			if(debugCreation) {logger.debug("NEW WB");}
			eWorkbook = efWorkbook.build(eReport);
			eWorkbook = fReport.save(eWorkbook);
			eReport.setWorkbook(eWorkbook);
		}
		eWorkbook = fReport.load(eWorkbook);
		
		JeeslDbCodeEjbUpdater<SHEET> dbUpdaterSheet = JeeslDbCodeEjbUpdater.instance(fbReport.getClassSheet());
		dbUpdaterSheet.dbEjbs(eWorkbook.getSheets());
		if(ObjectUtils.isNotEmpty(xWorkbook.getXlsSheets()))
		{
			for(XlsSheet xSheet : xWorkbook.getXlsSheets().getXlsSheet())
			{
				try
				{
					SHEET eSheet = importSheet(eWorkbook,xSheet);
					dbUpdaterSheet.handled(eSheet);
				}
				catch (JeeslNotFoundException e) {throw new UtilsProcessingException(e.getMessage());}
				catch (JeeslConstraintViolationException e) {throw new UtilsProcessingException(e.getMessage());}
				catch (JeeslLockingException e) {throw new UtilsProcessingException(e.getMessage());}
				catch (ExlpXpathNotFoundException e) {throw new UtilsProcessingException(e.getMessage());}
			}
		}
		for(SHEET s : dbUpdaterSheet.getEjbForRemove()){fReport.rmSheet(s);}
		
		return eReport;
	}
	
	private SHEET importSheet(WORKBOOK workbook, XlsSheet xSheet) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, ExlpXpathNotFoundException, UtilsProcessingException
	{
		logger.trace("Importing "+fbReport.getClassSheet().getSimpleName()+" "+workbook.getReport().getCategory().getPosition()+"."+workbook.getReport().getPosition()+"."+xSheet.getPosition());
		SHEET eSheet;
		try
		{
			eSheet = fReport.fSheet(workbook, xSheet.getCode());
			if(debugCreation) {logger.debug("Existing Sheet");}
		}
		catch (JeeslNotFoundException e)
		{
			if(debugCreation) {logger.debug("NEW Sheet");}
			eSheet = efSheet.build(fReport,workbook,xSheet);
			eSheet = fReport.save(eSheet);
		}
		eSheet = efSheet.update(fReport,eSheet,xSheet);
		eSheet = fReport.save(eSheet);
		eSheet = efSheet.updateLD(fReport, eSheet, xSheet);
		eSheet = fReport.load(eSheet,false);
		
		JeeslDbCodeEjbUpdater<GROUP> dbUpdaterGroup = JeeslDbCodeEjbUpdater.instance(fbReport.getClassGroup());
		JeeslDbCodeEjbUpdater<ROW> dbUpdaterRow = JeeslDbCodeEjbUpdater.instance(fbReport.getClassRow());
		
		dbUpdaterGroup.dbEjbs(eSheet.getGroups());
		dbUpdaterRow.dbEjbs(eSheet.getRows());
		
		for(Serializable s : xSheet.getContent())
		{
			if(s instanceof ColumnGroup)
			{
				ColumnGroup xGroup = (ColumnGroup)s;
				GROUP eGroup = importGroup(eSheet,xGroup);
				dbUpdaterGroup.handled(eGroup);
			}
			else if(s instanceof Rows)
			{
				Rows xRows = (Rows)s;
				for(Row xRow : xRows.getRow())
				{
					ROW eRow = importRow(eSheet,xRow);
					dbUpdaterRow.handled(eRow);
				}
			}
		}
		for(GROUP g : dbUpdaterGroup.getEjbForRemove()){fReport.rmGroup(g);}
		for(ROW r : dbUpdaterRow.getEjbForRemove()){fReport.rmRow(r);}
		
		return eSheet;
	}
	
	private ROW importRow(SHEET eSheet, Row xRow) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, ExlpXpathNotFoundException, UtilsProcessingException
	{
		ROW eRow;
		try
		{
			eRow = fReport.fByCode(fbReport.getClassRow(), xRow.getCode());
			if(debugCreation) {logger.debug("Existing "+fbReport.getClassRow());}
		}
		catch (JeeslNotFoundException e)
		{
			if(debugCreation) {logger.debug("NEW "+fbReport.getClassRow());}
			eRow = efRow.build(fReport,eSheet,xRow);
			eRow = fReport.save(eRow);
		}
		eRow = efRow.update(fReport,eRow,xRow);
		eRow = fReport.save(eRow);
		eRow = efRow.updateLD(fReport,eRow,xRow);

		return eRow;
	}
	
	private GROUP importGroup(SHEET eSheet, ColumnGroup xGroup) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, ExlpXpathNotFoundException, UtilsProcessingException
	{
		GROUP eGroup;
		try
		{
			eGroup = fReport.fByCode(fbReport.getClassGroup(),xGroup.getCode());
			if(debugCreation) {logger.debug("Existing "+fbReport.getClassGroup());}
		}
		catch (JeeslNotFoundException e)
		{
			if(debugCreation) {logger.debug("New "+fbReport.getClassGroup());}
			eGroup = efGroup.build(fReport,eSheet,xGroup);
			eGroup = fReport.save(eGroup);
		}
		eGroup = efGroup.update(fReport,eGroup, xGroup);
		eGroup = fReport.save(eGroup);
		eGroup = efGroup.updateLD(fReport,eGroup, xGroup);
		eGroup = fReport.load(eGroup);
		
		JeeslDbCodeEjbUpdater<COLUMN> dbUpdaterColumn = JeeslDbCodeEjbUpdater.instance(fbReport.getClassColumn());
		dbUpdaterColumn.dbEjbs(eGroup.getColumns());
		for(XlsColumn xColumn : xGroup.getXlsColumn())
		{
			COLUMN eColumn = importColumn(eGroup,xColumn);
			dbUpdaterColumn.handled(eColumn);
		}
		for(COLUMN c : dbUpdaterColumn.getEjbForRemove()){fReport.rmColumn(c);}
		
		return eGroup;
	}
	
	private COLUMN importColumn(GROUP eGroup, XlsColumn xColumn) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, ExlpXpathNotFoundException
	{
		COLUMN eColumn;
		try
		{
			eColumn = fReport.fByCode(fbReport.getClassColumn(), xColumn.getCode());
			if(debugCreation) {logger.debug("Existing "+fbReport.getClassColumn());}
		}
		catch (JeeslNotFoundException e)
		{
			if(debugCreation) {logger.debug("NEW "+fbReport.getClassColumn());}
			eColumn = efColumn.build(fReport,eGroup,xColumn);
			eColumn = fReport.save(eColumn);
		}
		efColumn.update(fReport,eGroup,eColumn,xColumn);
		eColumn = fReport.save(eColumn);
		eColumn = efColumn.updateLD(fReport,eColumn,xColumn);
		return eColumn;
	}
}