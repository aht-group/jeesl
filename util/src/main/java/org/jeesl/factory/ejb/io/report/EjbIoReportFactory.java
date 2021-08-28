package org.jeesl.factory.ejb.io.report;

import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.controller.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.report.Report;

public class EjbIoReportFactory<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
								ROW extends JeeslReportRow<L,D,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
								STYLE extends JeeslReportStyle<L,D>,
								CDT extends JeeslStatus<L,D,CDT>,
								CW extends JeeslStatus<L,D,CW>,
								RT extends JeeslStatus<L,D,RT>,
								ENTITY extends EjbWithId,
								ATTRIBUTE extends EjbWithId,
								TL extends JeeslTrafficLight<L,D,TLS>,
								TLS extends JeeslStatus<L,D,TLS>,
								FILLING extends JeeslStatus<L,D,FILLING>,
								TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoReportFactory.class);
	
	private final Class<CATEGORY> cCategory;
	private final Class<REPORT> cReport;
	
	private JeeslDbLangUpdater<REPORT,L> dbuReportLang;
	private JeeslDbDescriptionUpdater<REPORT,D> dbuReportDescription;
    
	public EjbIoReportFactory(final Class<L> cL,final Class<D> cD,final Class<CATEGORY> cCategory, final Class<REPORT> cReport)
	{
		this.cCategory = cCategory;
        this.cReport = cReport;
        
		dbuReportLang = JeeslDbLangUpdater.factory(cReport, cL);
		dbuReportDescription = JeeslDbDescriptionUpdater.factory(cReport, cD);
	}
	    
	public REPORT build(CATEGORY category)
	{
		REPORT ejb = null;
		try
		{
			ejb = cReport.newInstance();
			ejb.setCategory(category);
			ejb.setPosition(1);
			ejb.setVisible(true);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public REPORT build(JeeslIoReportFacade<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fReport, Report xReport) throws JeeslNotFoundException
	{
		REPORT ejb = null;
		try
		{
			ejb = cReport.newInstance();
			ejb.setCode(xReport.getCode());
			ejb = update(fReport,ejb,xReport);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public REPORT update (JeeslFacade fUtils, REPORT eReport, Report xReport) throws JeeslNotFoundException
	{
		CATEGORY eCategory = fUtils.fByCode(cCategory, xReport.getCategory().getCode());

		eReport.setCategory(eCategory);
		eReport.setPosition(xReport.getPosition());
		eReport.setVisible(xReport.isVisible());
		eReport.setXmlExample(xReport.getXmlExample());
		return eReport;
	}
	
	public REPORT updateLD(JeeslFacade fUtils, REPORT eReport, Report xReport) throws JeeslConstraintViolationException, JeeslLockingException
	{
		eReport=dbuReportLang.handle(fUtils, eReport, xReport.getLangs());
		eReport = fUtils.save(eReport);
		
		eReport=dbuReportDescription.handle(fUtils, eReport, xReport.getDescriptions());
		eReport = fUtils.save(eReport);
		
		return eReport;
	}
}