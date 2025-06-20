package org.jeesl.factory.ejb.io.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.controller.io.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.io.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.report.data.JeeslReportQueryType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.report.Row;
import org.jeesl.util.query.xpath.ReportXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;

public class EjbIoReportRowFactory<L extends JeeslLang, D extends JeeslDescription,
								SHEET extends JeeslReportSheet<L,D,?,?,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,?,STYLE>,
								ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
								TEMPLATE extends JeeslReportTemplate<L,D,?>,
								
								STYLE extends JeeslReportStyle<L,D,?>,
								CDT extends JeeslStatus<L,D,CDT>,
								RT extends JeeslReportRowType<L,D,RT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoReportRowFactory.class);
	
	final Class<ROW> cRow;
	final Class<TEMPLATE> cTemplate;
	final Class<CDT> cDataType;
	final Class<RT> cRt;
	
	private JeeslDbLangUpdater<ROW,L> dbuLang;
	private JeeslDbDescriptionUpdater<ROW,D> dbuDescription;
    
	public EjbIoReportRowFactory(final Class<L> cL,final Class<D> cD,final Class<ROW> cRow, final Class<TEMPLATE> cTemplate, final Class<CDT> cDataType, final Class<RT> cRt)
	{       
        this.cRow = cRow;
        this.cTemplate = cTemplate;
        this.cRt = cRt;
        this.cDataType=cDataType;
        dbuLang = JeeslDbLangUpdater.factory(cRow, cL);
        dbuDescription = JeeslDbDescriptionUpdater.factory(cRow, cD);
	}
	    
	public ROW build(SHEET sheet)
	{
		ROW ejb = null;
		try
		{
			ejb = cRow.newInstance();
			ejb.setCode(UUID.randomUUID().toString());
			ejb.setSheet(sheet);
			ejb.setPosition(1);
			ejb.setVisible(false);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public ROW build(JeeslIoReportFacade<?,?,SHEET,GROUP,?,ROW,TEMPLATE,?> fReport, SHEET sheet, Row row) throws JeeslNotFoundException
	{
		ROW ejb = null;
		try
		{
			ejb = cRow.newInstance();
			ejb.setCode(row.getCode());
			ejb.setSheet(sheet);
			ejb = update(fReport,ejb,row);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
		
	public ROW update(JeeslIoReportFacade<?,?,SHEET,GROUP,?,ROW,TEMPLATE,?> fReport, ROW eRow, Row xRow) throws JeeslNotFoundException
	{
		CDT eDataType = null; if(Objects.nonNull(xRow.getDataType())) {eDataType = fReport.fByCode(cDataType, xRow.getDataType().getCode());}
		TEMPLATE eTemplate = null; if(Objects.nonNull(xRow.getTemplate())) {eTemplate = fReport.fByCode(cTemplate, xRow.getTemplate().getCode());}
		
		eRow.setType(fReport.fByCode(cRt, xRow.getType().getCode()));
		eRow.setPosition(xRow.getPosition());
		eRow.setVisible(xRow.isVisible());
		eRow.setDataType(eDataType);
		eRow.setTemplate(eTemplate);
		
		if(Objects.nonNull(xRow.getQueries()))
		{			
			try{eRow.setQueryCell(ReportXpath.getQuery(JeeslReportQueryType.Row.cell.toString(), xRow.getQueries()).getValue());}
			catch (ExlpXpathNotFoundException e) {eRow.setQueryCell(null);}
		}
		if(Objects.nonNull(xRow.getLayout()))
		{
			if(Objects.nonNull(xRow.getLayout().getOffset()))
			{
				eRow.setOffsetRows(xRow.getLayout().getOffset().getRows());
				eRow.setOffsetColumns(xRow.getLayout().getOffset().getColumns());
			}
		}
		
		
		return eRow;
	}
		
	public ROW updateLD(JeeslFacade fUtils, ROW eRow, Row xRow) throws JeeslConstraintViolationException, JeeslLockingException
	{
		eRow=dbuLang.handle(fUtils, eRow, xRow.getLangs());
		eRow = fUtils.save(eRow);
		eRow=dbuDescription.handle(fUtils, eRow, xRow.getDescriptions());
		eRow = fUtils.save(eRow);
		return eRow;
	}
	
	public List<ROW> toListVisibleRows(SHEET sheet)
	{
		List<ROW> list = new ArrayList<ROW>();
		for(ROW r : sheet.getRows())
		{
			if(r.isVisible())
			{
				list.add(r);
			}
		}
		return list;
	}

	public void converter(JeeslFacade facade, ROW row)
	{
		
	}
}