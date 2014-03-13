package net.sf.ahtutils.monitor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import net.sf.ahtutils.bootstrap.UtilsMonitorBootstrap;
import net.sf.ahtutils.controller.facade.UtilsFacadeBean;
import net.sf.ahtutils.interfaces.facade.UtilsFacade;
import net.sf.ahtutils.monitor.result.net.IcmpResult;
import net.sf.ahtutils.monitor.task.AnalysisTask;
import net.sf.ahtutils.monitor.task.MonitoringTask;

import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndicatorWorker
{
	final static Logger logger = LoggerFactory.getLogger(MonitoringTask.class);
	
	public IndicatorWorker()
	{		
		EntityManagerFactory emf = UtilsMonitorBootstrap.buildEmf(false);
        EntityManager em = emf.createEntityManager();
	
        UtilsFacade ufb = new UtilsFacadeBean(em);
        
        logger.info("First is "+ufb.fFirst(IcmpResult.class).toString());
        logger.info("Last is "+ufb.fLast(IcmpResult.class).toString());
        
        Duration range = Duration.standardHours(1);
        Duration sleep = Duration.standardSeconds(30);
        
        Thread t = new Thread(new AnalysisTask(sleep,range));
        t.start();
        
        try {Thread.sleep(7000);} catch (InterruptedException e){}
        logger.info("Will interrupt it!");
        t.interrupt();
	}
}