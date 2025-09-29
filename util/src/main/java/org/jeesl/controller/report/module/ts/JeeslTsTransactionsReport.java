package org.jeesl.controller.report.module.ts;

import java.util.Date;

import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.factory.xml.module.ts.XmlTransactionFactory;
import org.jeesl.factory.xml.module.ts.XmlTsFactory;
import org.jeesl.factory.xml.system.io.report.XmlReportFactory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsDataSource2;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.module.ts.Ts;
import org.jeesl.util.query.xml.module.XmlTsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslTsTransactionsReport <L extends JeeslLang,D extends JeeslDescription,
						
						TX extends JeeslTsTransaction<SRC,DATA,USER,?>,
						SRC extends JeeslTsDataSource2<L,D>, 
						
						DATA extends JeeslTsData<?,TX,?,?,?>,
						
						USER extends JeeslUser<?>
						>
					
//implements JeeslReportHeader//,JeeslFlatReport,JeeslXlsReport
{
	final static Logger logger = LoggerFactory.getLogger(JeeslTsTransactionsReport.class);

	private final JeeslTsFacade<?,?,?,?,?,?,TX,SRC,?,?,?,?,?,?,?,?,USER,?,?> fTs;
	
	private final XmlTransactionFactory<L,D,TX,SRC,USER> xfTransaction;
	
	public JeeslTsTransactionsReport(String localeCode,
			final JeeslTsFacade<?,?,?,?,?,?,TX,SRC,?,?,?,?,?,?,?,?,USER,?,?> fTs)
	{
	
		this.fTs=fTs;
		
		xfTransaction = new XmlTransactionFactory<>(XmlTsQuery.get(XmlTsQuery.Key.rTransaction, localeCode));
	}
	
	public Report build(Date from, Date to)
	{
		Report xml = XmlReportFactory.build();
		
		Ts ts = XmlTsFactory.build();
		
		for(TX t : fTs.fTransactions(null,JeeslTsData.QueryInterval.closedOpen,from,to))
		{
			ts.getTransaction().add(xfTransaction.build(t));
		}
		xml.setTs(ts);
		
		return xml;
	}
	
	
	
}