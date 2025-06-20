package org.jeesl.factory.ejb.io.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.controller.io.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.io.db.updater.JeeslDbLangUpdater;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.data.JeeslReportQueryType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.style.JeeslReportLayout;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.report.Size;
import org.jeesl.model.xml.io.report.XlsColumn;
import org.jeesl.util.query.xpath.ReportXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;

public class EjbIoReportColumnFactory<L extends JeeslLang,D extends JeeslDescription,
								
								SHEET extends JeeslReportSheet<L,D,?,?,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,?>,
								ROW extends JeeslReportRow<L,D,SHEET,?,CDT,RT>,
								CELL extends JeeslReportCell<L,D,?>,
								STYLE extends JeeslReportStyle<L,D,?>,
								CDT extends JeeslReportCellType<L,D,CDT,?>,
								CW extends JeeslStatus<L,D,CW>,
								RT extends JeeslReportRowType<L,D,RT,?>
//,
//								FILLING extends JeeslStatus<L,D,FILLING>,
//								TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>
>
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
	
	public COLUMN build(JeeslIoReportFacade<?,?,SHEET,GROUP,COLUMN,ROW,?,CELL> fReport, GROUP group, XlsColumn column) throws JeeslNotFoundException
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
	
	public COLUMN update(JeeslIoReportFacade<?,?,SHEET,GROUP,COLUMN,ROW,?,CELL> fReport, GROUP group, COLUMN eColumn, XlsColumn xColumn) throws JeeslNotFoundException
	{
		CDT eDataType = null;if(xColumn.getDataType()!=null){eDataType = fReport.fByCode(cDataType, xColumn.getDataType().getCode());}
		eColumn.setGroup(group);
		eColumn.setDataType(eDataType);
		
		eColumn.setPosition(xColumn.getPosition());
		eColumn.setVisible(xColumn.isVisible());
		
		eColumn.setShowLabel(xColumn.isShowLabel());
		eColumn.setShowWeb(BooleanComparator.active(xColumn.isShowWeb()));
		
		if(Objects.nonNull(xColumn.getQueries()))
		{
			try{eColumn.setQueryHeader(ReportXpath.getQuery(JeeslReportQueryType.Column.header.toString(), xColumn.getQueries()).getValue());}
			catch (ExlpXpathNotFoundException e) {eColumn.setQueryHeader(null);}
			
			try{eColumn.setQueryCell(ReportXpath.getQuery(JeeslReportQueryType.Column.cell.toString(), xColumn.getQueries()).getValue());}
			catch (ExlpXpathNotFoundException e) {eColumn.setQueryCell(null);}
			
			try{eColumn.setQueryFooter(ReportXpath.getQuery(JeeslReportQueryType.Column.footer.toString(), xColumn.getQueries()).getValue());}
			catch (ExlpXpathNotFoundException e) {eColumn.setQueryFooter(null);}
		}
		if(Objects.nonNull(xColumn.getLayout()))
		{
			if(ObjectUtils.isNotEmpty(xColumn.getLayout().getSize()))
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
			boolean visible = EjbIoReportColumnGroupFactory.visible(g,mapGroupVisibilityToggle);
			if(visible)
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
		
		StringBuilder sb = new StringBuilder();
		sb.append("No CellDataType for [").append(column.getId()).append("]");
		
		if(Objects.nonNull(column.getGroup().getName()) && column.getGroup().getName().containsKey(JeeslLang.en))
		{
			sb.append(" ").append(column.getGroup().getName().get(JeeslLang.en).getLang());
		}
		if(Objects.nonNull(column.getName()) && column.getName().containsKey(JeeslLang.en))
		{
			sb.append(": ").append(column.getName().get(JeeslLang.en).getLang());
		}
		
		logger.warn(sb.toString());
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