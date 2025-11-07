package org.jeesl.controller.processor.system.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.naming.NamingException;

import org.jeesl.api.facade.JeeslFacadeLookup;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.factory.txt.system.status.TxtStatusFactory;
import org.jeesl.interfaces.controller.processor.system.job.SystemMaintenanceFunction;
import org.jeesl.interfaces.controller.processor.system.job.SystemMaintenanceRunnable;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenance;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob2;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMaintenanceProcessor <L extends JeeslLang, D extends JeeslDescription,
													MS extends JeeslJobStatus<L,D,MS,?>,
													MNT extends JeeslJobMaintenance<L,D,MNT,?>,
													T extends EjbWithId> 
{
	final static Logger logger = LoggerFactory.getLogger(AbstractMaintenanceProcessor.class);
	
	protected final JeeslFacadeLookup jfl;
	
	protected final EjbCodeCache<MS> cacheJob;
	protected final EjbCodeCache<MNT> cacheMaintenance;
	
	protected final TxtStatusFactory<L,MNT> tfMaintenance;
	
	protected ThreadPoolExecutor pool;
	
	protected final List<SystemMaintenanceRunnable<MNT>> threads;
	protected final Queue<T> queue;
	
	public AbstractMaintenanceProcessor(JeeslFacadeLookup jfl,
											JeeslFacade facade,
											JobFactoryBuilder<L,D,?,?,?,?,?,?,?,?,MS,?,?,MNT,?,?> fbJob) throws NamingException
	{
		this.jfl=jfl;
		
		cacheJob = EjbCodeCache.instance(fbJob.getClassStatus()).facade(facade);
		cacheMaintenance = EjbCodeCache.instance(fbJob.getClassMaintenance()).facade(facade);
		
		tfMaintenance = TxtStatusFactory.factory(jfl.getLocaleCode());
		
		threads = new ArrayList<>();
		queue = new ConcurrentLinkedQueue<>();
	}
	
	public <E extends Enum<E>> String job(E code)
	{
		return cacheMaintenance.ejb(code).getName().get(jfl.getLocaleCode()).getLang();
	}
	
	protected void executeThreadPool(int maxThreads)
	{
		pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(maxThreads);
        for(Runnable w : threads) {pool.execute(w);}
        pool.shutdown();
        logger.info("Active: "+pool.getActiveCount());
	}
	
	public void startThreadPool(int maxThreads) throws NamingException
    {
        for(int i=1;i<=maxThreads;i++) {threads.add(buildWorker(i));}
        if(maxThreads>0)
        {
        	pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(maxThreads);
            for(Runnable w : threads) {pool.execute(w);}
            pool.shutdown();
            logger.info("Active: "+pool.getActiveCount());
        }
    }
	protected abstract SystemMaintenanceRunnable<MNT> buildWorker(int i) throws NamingException;
	
	public <E extends Enum<E>> void fillQueue(E code, SystemMaintenanceFunction<T> f)
	{	
		MNT task = cacheMaintenance.ejb(code);
		logger.info(tfMaintenance.debug(task)); AtomicInteger i = new AtomicInteger(0);
		for(SystemMaintenanceRunnable<MNT> w : threads) {w.task(task);}
		List<T> list = new ArrayList<>(); list.add(null);
		while(!list.isEmpty())
		{
			logger.debug("Find entities");
			list.clear(); list.addAll(f.find());
			queue.addAll(list);
			logger.info("Added "+list.size()+" elements to the queue, loop: "+i.incrementAndGet()+" in "+tfMaintenance.codeLabel(task));
			while(queue.peek()!=null) {try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}}
		}
		for(SystemMaintenanceRunnable<MNT> w : threads) {w.shutdown();}
	}
}