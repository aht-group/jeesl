package net.sf.ahtutils.monitor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import net.sf.ahtutils.bootstrap.UtilsMonitorBootstrap;
import net.sf.ahtutils.monitor.task.AnalysisTask;
import net.sf.ahtutils.monitor.worker.MonitoringTaskBuilder;

import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.monitoring.result.net.IcmpResult;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndicatorWorker
{
	final static Logger logger = LoggerFactory.getLogger(MonitoringTaskBuilder.class);
	
	public IndicatorWorker()
	{		
		EntityManagerFactory emf = UtilsMonitorBootstrap.buildEmf(false);
        EntityManager em = emf.createEntityManager();
	
        JeeslFacade ufb = new JeeslFacadeBean(em);
        
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