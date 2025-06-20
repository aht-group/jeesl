package org.jeesl.factory.ejb.io.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jeesl.controller.io.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.io.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoReportFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.report.data.JeeslReportQueryType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.style.JeeslReportAlignment;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.report.Queries;
import org.jeesl.model.xml.io.report.XlsSheet;
import org.jeesl.util.query.xpath.ReportXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;

public class EjbIoReportSheetFactory<L extends JeeslLang,D extends JeeslDescription,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<?,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,?,?,?>,
								ROW extends JeeslReportRow<L,D,SHEET,?,?,?>,
								STYLE extends JeeslReportStyle<L,D,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoReportSheetFactory.class);
	
	private final EjbIoReportColumnFactory<L,D,SHEET,GROUP,COLUMN,ROW,?,STYLE,?,?,?> efColumn;
	
	final Class<IMPLEMENTATION> cImplementation;
	final Class<SHEET> cSheet;
    
	private JeeslDbLangUpdater<SHEET,L> dbuLang;
	private JeeslDbDescriptionUpdater<SHEET,D> dbuDescription;
	
	public EjbIoReportSheetFactory(final IoReportFactoryBuilder<L,D,?,?,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,?,?,STYLE,?,?,?,?,?,?,?,?,?,?,?> fbReport,
			final Class<L> cL,final Class<D> cD,final Class<IMPLEMENTATION> cImplementation,final Class<SHEET> cSheet)
	{
		this.cImplementation=cImplementation;
        this.cSheet = cSheet;
        
        efColumn = fbReport.column();
        
        dbuLang = JeeslDbLangUpdater.factory(cSheet, cL);
        dbuDescription = JeeslDbDescriptionUpdater.factory(cSheet, cD);
	}
	    
	public SHEET build(WORKBOOK workbook)
	{
		SHEET ejb = null;
		try
		{
			ejb = cSheet.newInstance();
			ejb.setCode(UUID.randomUUID().toString());
			ejb.setWorkbook(workbook);
			ejb.setPosition(1);
			ejb.setVisible(false);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public SHEET build(JeeslFacade fReport, WORKBOOK workbook, XlsSheet sheet) throws JeeslNotFoundException
	{
		SHEET ejb = null;
		try
		{
			ejb = cSheet.newInstance();
			ejb.setCode(sheet.getCode());
			ejb.setWorkbook(workbook);
			ejb = update(fReport,ejb,sheet);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public SHEET update(JeeslFacade fReport, SHEET eSheet, XlsSheet xSheet) throws JeeslNotFoundException
	{
		try {eSheet.setImplementation(fReport.fByCode(cImplementation, ReportXpath.getImplementation(xSheet).getCode()));}
		catch (ExlpXpathNotFoundException e) {throw new JeeslNotFoundException(e.getMessage());}
		
		eSheet.setPosition(xSheet.getPosition());
		eSheet.setVisible(xSheet.isVisible());
		
		try
		{
			Queries queries = ReportXpath.getQueries(xSheet);
			try{eSheet.setQueryTable(ReportXpath.getQuery(JeeslReportQueryType.Sheet.table.toString(), queries).getValue());}
			catch (ExlpXpathNotFoundException e) {eSheet.setQueryTable(null);}
		}
		catch (ExlpXpathNotFoundException e) {}
		
		
		return eSheet;
	}
	
	public SHEET updateLD(JeeslFacade fUtils, SHEET eSheet, XlsSheet xSheet) throws JeeslConstraintViolationException, JeeslLockingException, ExlpXpathNotFoundException
	{
		eSheet=dbuLang.handle(fUtils, eSheet, ReportXpath.getLangs(xSheet));
		eSheet = fUtils.save(eSheet);
		
		eSheet=dbuDescription.handle(fUtils, eSheet, ReportXpath.getDescriptions(xSheet));
		eSheet = fUtils.save(eSheet);
		return eSheet;
	}
	
	public boolean hasFooters(SHEET sheet){return hasFooters(sheet,null);}
	public boolean hasFooters(SHEET sheet, Map<GROUP,Boolean> mapGroupVisibilityToggle)
	{
		boolean withFooter = false;
		{
			for(COLUMN c : efColumn.toListVisibleColumns(sheet,mapGroupVisibilityToggle))
			{
				if(efColumn.hasFooter(c)){withFooter=true;}
			}
		}
		return withFooter;
	}
	
	public static <WORKBOOK extends JeeslReportWorkbook<?,SHEET>,
					SHEET extends JeeslReportSheet<?,?,?,WORKBOOK,?,?>>
				List<SHEET> toListVisibleShets(WORKBOOK workbook,Map<SHEET,Boolean> mapVisibilityToggle)
	{
		List<SHEET> list = new ArrayList<SHEET>();
		for(SHEET g : workbook.getSheets())
		{
			if(visible(g,mapVisibilityToggle))
			{
				list.add(g);
			}
		}
		return list;
	}
	
	private static <SHEET extends JeeslReportSheet<?,?,?,?,?,?>> boolean visible(SHEET s, Map<SHEET,Boolean> mapVisibilityToggle)
	{
		boolean toggle = true;
		if(mapVisibilityToggle!=null)
		{
			if(mapVisibilityToggle.containsKey(s)){toggle = mapVisibilityToggle.get(s);}
			else{toggle = false;}
		}
		return s.isVisible() && toggle;
	}
}