package org.jeesl.controller.processor.system.job;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;

import javax.naming.NamingException;

import org.jeesl.api.facade.JeeslFacadeLookup;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.factory.txt.system.status.TxtStatusFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenance;
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
	
	protected final TxtStatusFactory<L,D,MNT> tfMaintenance;
	
	protected ThreadPoolExecutor pool;
	
	protected final Queue<T> queue;
	
	public AbstractMaintenanceProcessor(JeeslFacadeLookup jfl,
											JeeslFacade facade,
											JobFactoryBuilder<L,D,?,?,?,?,?,?,?,?,MS,?,?,MNT,?,?> fbJob) throws NamingException
	{
		this.jfl=jfl;
		
		cacheJob = EjbCodeCache.instance(fbJob.getClassStatus()).facade(facade);
		cacheMaintenance = EjbCodeCache.instance(fbJob.getClassMaintenance()).facade(facade);
		
		tfMaintenance = TxtStatusFactory.factory(jfl.getLocaleCode());
		
		queue = new ConcurrentLinkedQueue<>();
	}
}