package org.jeesl.factory.ejb.io.report;

import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoReportWorkbookFactory<REPORT extends JeeslIoReport<?,?,?,WORKBOOK>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoReportWorkbookFactory.class);
	
	final Class<WORKBOOK> cWorkbook;
    
	public EjbIoReportWorkbookFactory(final Class<WORKBOOK> cWorkbook)
	{       
        this.cWorkbook = cWorkbook;
	}
	    
	public WORKBOOK build(REPORT report)
	{
		WORKBOOK ejb = null;
		try
		{
			ejb = cWorkbook.newInstance();
			ejb.setReport(report);
			ejb.getReport().setWorkbook(ejb);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}