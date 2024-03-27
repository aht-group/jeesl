package org.jeesl.factory.ejb.io.report;

import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.controller.io.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.io.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
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
import org.jeesl.model.xml.io.report.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoReportFactory<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,?>>
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
	
	public REPORT build(JeeslIoReportFacade<REPORT,?,?,?,?,?,?,?,?> fReport, Report xReport) throws JeeslNotFoundException
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