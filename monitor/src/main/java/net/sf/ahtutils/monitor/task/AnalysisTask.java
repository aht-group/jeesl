package net.sf.ahtutils.monitor.task;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jeesl.api.facade.module.JeeslMonitoringFacade;
import org.jeesl.controller.monitoring.result.net.IcmpResult;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.model.xml.module.monitoring.Indicator;
import org.jeesl.model.xml.module.monitoring.Transmission;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.MutableDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.bootstrap.UtilsMonitorBootstrap;
import net.sf.ahtutils.controller.facade.UtilsMonitoringFacadeBean;
import net.sf.ahtutils.monitor.factory.TransmissionFactory;
import net.sf.ahtutils.monitor.factory.net.TxIcmpFactory;
import net.sf.ahtutils.monitor.util.DbCleaner;
import net.sf.ahtutils.monitor.util.IntervalHelper;
import net.sf.ahtutils.monitor.util.RestTransmission;

public class AnalysisTask  implements Runnable 
{
	final static Logger logger = LoggerFactory.getLogger(AnalysisTask.class);
	
	private Duration range;
	private Duration sleep;
	
	private EntityManager em;
	private JeeslMonitoringFacade fUm;
	
	private TxIcmpFactory fIcmp;
	private List<Indicator> listIndicators;
	private DbCleaner dbCleaner;
	
	public AnalysisTask(Duration sleep, Duration range)
	{
		this.sleep=sleep;
		this.range=range;
		em = UtilsMonitorBootstrap.buildEmf(false).createEntityManager();
		fUm = new UtilsMonitoringFacadeBean(em);
		
		dbCleaner = new DbCleaner(em);
		listIndicators = new ArrayList<Indicator>();
		fIcmp = new TxIcmpFactory(fUm,dbCleaner);
	}
	
	public void run()
	{
		while(!Thread.currentThread().isInterrupted())
		{
			logger.info("Running ...");
			listIndicators.clear();
			dbCleaner.clear();
			
			try
			{
				check(IcmpResult.class);
			}
			catch (UtilsProcessingException e) {e.printStackTrace();}
	        
			if(listIndicators.size()>0)
			{
				TransmissionFactory fTransmission = new TransmissionFactory(fUm);
				RestTransmission restTransmission = new RestTransmission(fUm);
				
				Transmission transmission = fTransmission.build(listIndicators);
				restTransmission.send(transmission);
				dbCleaner.clean();
				dbCleaner.clear();
			}
			
	        logger.info("Will sleep");
			try {Thread.sleep(sleep.getMillis());}
			catch (InterruptedException e)
			{
				logger.info("I'm interrupted");
				return;
			}
			logger.info("Finished loop ...");
		}
	}
	
	private <T extends EjbWithRecord> void check(Class<T> t) throws UtilsProcessingException
	{
		DateTime first = new DateTime(fUm.fFirst(t).getRecord());
		DateTime last = new DateTime(fUm.fLast(t).getRecord());
		Duration d = new Duration(first,last);
		
		logger.info("**************");
		logger.info(t.getName());
		logger.info("First record: "+first.toString());
		logger.info("Last record:  "+last.toString());
		logger.info("Range:        "+range.toString());
		
		MutableDateTime ceilFirst = IntervalHelper.ceil(first, range);
		MutableDateTime floorLast = IntervalHelper.floor(last, range);
		logger.info("first-ceil:   "+ceilFirst);
		logger.info("last-floor:   "+floorLast);
		
		Duration tripleRange = range;
		logger.warn("Range needs to be tripled");
//		tripleRange = tripleRange.plus(range);
//		tripleRange = tripleRange.plus(range);
		
		if(d.isLongerThan(tripleRange))
		{
			if(IcmpResult.class.isAssignableFrom(t)){listIndicators.add(fIcmp.build(ceilFirst.toDateTime(),floorLast.toDateTime(),range));}
		}
		else
		{
			logger.debug("Duration "+d+" is smaller than triple-range "+range);
		}
	}
}