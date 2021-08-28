package org.jeesl.factory.ejb.io.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.controller.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.data.JeeslReportQueryType;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportLayout;
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
import org.jeesl.util.query.xpath.ReportXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.report.Size;
import net.sf.ahtutils.xml.report.XlsColumn;
import net.sf.exlp.exception.ExlpXpathNotFoundException;

public class EjbIoReportColumnFactory<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
								ROW extends JeeslReportRow<L,D,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
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
								TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoReportColumnFactory.class);
	
	final Class<COLUMN> cColumn;
	final Class<CDT> cDataType;
	final Class<CW> cColumnWidth;
	
	private final JeeslDbLangUpdater<COLUMN,L> dbuLang;
	private final JeeslDbDescriptionUpdater<COLUMN,D> dbuDescription;
    
	public EjbIoReportColumnFactory(final Class<L> cL,final Class<D> cD,final Class<COLUMN> cColumn, Class<CDT> cDataType, Class<CW> cColumnWidth)
	{       
        this.cColumn = cColumn;
        this.cDataType=cDataType;
        this.cColumnWidth=cColumnWidth;
        dbuLang = JeeslDbLangUpdater.factory(cColumn,cL);
        dbuDescription = JeeslDbDescriptionUpdater.factory(cColumn,cD);
	}
	    
	public COLUMN build(GROUP group, List<COLUMN> list)
	{
		COLUMN ejb = null;
		try
		{
			ejb = cColumn.newInstance();
			ejb.setCode(UUID.randomUUID().toString());
			ejb.setGroup(group);
			
			ejb.setColumSize(0);
			ejb.setVisible(false);
			ejb.setShowLabel(true);
			if(list==null) {ejb.setPosition(1);}
			else {ejb.setPosition(list.size()+1);}
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public COLUMN build(JeeslIoReportFacade<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fReport, GROUP group, XlsColumn column) throws JeeslNotFoundException
	{
		COLUMN ejb = null;
		try
		{
			ejb = cColumn.newInstance();
			ejb.setCode(column.getCode());
			ejb.setGroup(group);
			ejb = update(fReport,group,ejb,column);

		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
	
	public COLUMN update(JeeslIoReportFacade<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fReport, GROUP group, COLUMN eColumn, XlsColumn xColumn) throws JeeslNotFoundException
	{
		CDT eDataType = null;if(xColumn.getDataType()!=null){eDataType = fReport.fByCode(cDataType, xColumn.getDataType().getCode());}
		eColumn.setGroup(group);
		eColumn.setDataType(eDataType);
		
		eColumn.setPosition(xColumn.getPosition());
		eColumn.setVisible(xColumn.isVisible());
		
		eColumn.setShowLabel(xColumn.isShowLabel());
		eColumn.setShowWeb(xColumn.isSetShowWeb() && xColumn.isShowWeb());
		
		if(xColumn.isSetQueries())
		{
			try{eColumn.setQueryHeader(ReportXpath.getQuery(JeeslReportQueryType.Column.header.toString(), xColumn.getQueries()).getValue());}
			catch (ExlpXpathNotFoundException e) {eColumn.setQueryHeader(null);}
			
			try{eColumn.setQueryCell(ReportXpath.getQuery(JeeslReportQueryType.Column.cell.toString(), xColumn.getQueries()).getValue());}
			catch (ExlpXpathNotFoundException e) {eColumn.setQueryCell(null);}
			
			try{eColumn.setQueryFooter(ReportXpath.getQuery(JeeslReportQueryType.Column.footer.toString(), xColumn.getQueries()).getValue());}
			catch (ExlpXpathNotFoundException e) {eColumn.setQueryFooter(null);}
		}
		if(xColumn.isSetLayout())
		{
			if(xColumn.getLayout().isSetSize())
			{
				try
				{
					Size size = ReportXpath.getSize(JeeslReportLayout.Code.columnWidth.toString(), xColumn.getLayout());
					eColumn.setColumWidth(fReport.fByCode(cColumnWidth, size.getType().getCode()));
					eColumn.setColumSize(size.getValue());
				}
				catch (ExlpXpathNotFoundException e)
				{
					eColumn.setColumWidth(null);
					eColumn.setColumSize(null);
				}
			}
		}
		return eColumn;
	}
	
	public COLUMN updateLD(JeeslFacade fUtils, COLUMN eColumn, XlsColumn xColumn) throws JeeslConstraintViolationException, JeeslLockingException
	{
		eColumn = dbuLang.handle(fUtils, eColumn, xColumn.getLangs());
		eColumn = fUtils.save(eColumn);
		eColumn = dbuDescription.handle(fUtils, eColumn, xColumn.getDescriptions());
		eColumn = fUtils.save(eColumn);
		return eColumn;
	}
	
	public List<COLUMN> toListVisibleColumns(SHEET sheet) {return toListVisibleColumns(sheet,null);}

	public List<COLUMN> toListVisibleColumns(SHEET sheet, Map<GROUP,Boolean> mapGroupVisibilityToggle)
	{
		List<COLUMN> list = new ArrayList<COLUMN>();
		for(GROUP g : sheet.getGroups())
		{			
			if(EjbIoReportColumnGroupFactory.visible(g,mapGroupVisibilityToggle))
			{
				for(COLUMN c : g.getColumns())
				{
					if(c.isVisible()){list.add(c);}
				}
			}
		}
		return list;
	}
	
	public  List<COLUMN> toListVisibleColumns(GROUP group)
	{
		List<COLUMN> list = new ArrayList<COLUMN>();
		for(COLUMN c : group.getColumns())
		{
			if(c.isVisible()){list.add(c);}
		}
		return list;
	}
	
	public CDT toCellDataType(COLUMN column)
	{
		if(column.getDataType()!=null){return column.getDataType();}
		logger.warn("No CellDataType for "+column.toString());
		return null;
	}
	
	public CDT toRowDataType(ROW row)
	{
		if(row.getDataType()!=null){return row.getDataType();}
		return null;
	}
	
	public boolean hasFooter(COLUMN column)
	{
		return (column.getQueryFooter()!=null && column.getQueryFooter().trim().length()>0);
	}
}