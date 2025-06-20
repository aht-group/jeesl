package org.jeesl.factory.ejb.io.report;

import java.util.UUID;

import org.jeesl.controller.io.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.io.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
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
import org.jeesl.model.xml.io.report.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoReportCellFactory<L extends JeeslLang,D extends JeeslDescription,
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
								STYLE extends JeeslReportStyle<L,D,?>,
								CDT extends JeeslReportCellType<L,D,CDT,?>,
								CW extends JeeslStatus<L,D,CW>,
								RT extends JeeslReportRowType<L,D,RT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoReportCellFactory.class);
	
	final Class<CELL> cCell;
	
	private JeeslDbLangUpdater<CELL,L> dbuLang;
	private JeeslDbDescriptionUpdater<CELL,D> dbuDescription;
    
	public EjbIoReportCellFactory(final Class<L> cL,final Class<D> cD,final Class<CELL> cCell)
	{       
        this.cCell = cCell;
        dbuLang = JeeslDbLangUpdater.factory(cCell, cL);
        dbuDescription = JeeslDbDescriptionUpdater.factory(cCell, cD);
	}
	    
	public CELL build(TEMPLATE template)
	{
		CELL ejb = null;
		try
		{
			ejb = cCell.newInstance();
			ejb.setCode(UUID.randomUUID().toString());
			ejb.setTemplate(template);
			ejb.setRowNr(1);
			ejb.setColNr(1);
			ejb.setVisible(false);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public CELL build(TEMPLATE template, Cell cell)
	{
		CELL ejb = null;
		try
		{
			ejb = cCell.newInstance();
			ejb.setCode(cell.getCode());
			ejb.setTemplate(template);
			ejb = update(ejb,cell);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
		
	public CELL update(CELL eCell, Cell xCell)
	{
		eCell.setVisible(xCell.isVisible());
		eCell.setRowNr(xCell.getRowNr());
		eCell.setColNr(xCell.getColNr());
		return eCell;
	}
		
	public CELL updateLD(JeeslFacade fUtils, CELL eCell, Cell xCell) throws JeeslConstraintViolationException, JeeslLockingException
	{
		eCell=dbuLang.handle(fUtils, eCell, xCell.getLangs());
		eCell = fUtils.save(eCell);
		eCell=dbuDescription.handle(fUtils, eCell, xCell.getDescriptions());
		eCell = fUtils.save(eCell);
		return eCell;
	}
}