package org.jeesl.controller.processor.system.job;

import java.util.Objects;
import java.util.Queue;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.interfaces.controller.processor.system.job.SystemMaintenanceRunnable;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenance;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob2;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMaintenanceWorker <MNT extends JeeslJobMaintenance<?,?,MNT,?>, T extends EjbWithId>
						implements SystemMaintenanceRunnable<MNT>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractMaintenanceWorker.class);
	
	protected final int id;
	private final Queue<T> queue;
	
	protected boolean active; @Override public void shutdown() {active=false;}
	protected MNT task; @Override public void task(MNT task) {this.task = task;}
	
	public AbstractMaintenanceWorker(int id, Queue<T> queue)
	{
		this.id=id;
		this.queue=queue;
		active=true;
		logger.info(id+" ready");
	}
	
	@Override
	public void run()
	{   
		while(active)
		{
			T p = queue.poll();
			if(Objects.nonNull(p))
			{
   			try
   			{
   				this.handle(p);
   			}
   			catch (JeeslConstraintViolationException | JeeslLockingException | JeeslNotFoundException e) {e.printStackTrace();} catch (UtilsProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   		}
			else
   			{
   				logger.trace("Thread "+id+" waiting for queue ...");
				try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
   			}
		}
		logger.info("Shutting down "+id);
	}
	
	protected abstract void handle(T t) throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException, UtilsProcessingException;
}