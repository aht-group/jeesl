package net.sf.ahtutils.controller.facade;

import javax.persistence.EntityManager;

import net.sf.ahtutils.interfaces.model.issue.UtilsTask;
import net.sf.ahtutils.interfaces.model.with.EjbWithTask;

import org.jeesl.api.facade.core.JeeslIssueFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.module.task.EjbTaskFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsIssueFacadeBean extends JeeslFacadeBean implements JeeslIssueFacade
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(UtilsIssueFacadeBean.class);
	
	public UtilsIssueFacadeBean(EntityManager em)
	{
		super(em);
	}

	@Override
	public <T extends UtilsTask<T>, WT extends EjbWithTask<T>> T fcTask(Class<T> clTask, Class<WT> clWithTask, WT ejb)
	{
		ejb = em.find(clWithTask, ejb.getId());
		if(ejb.getTask()==null)
		{
			EjbTaskFactory<T> efTask = EjbTaskFactory.factory(clTask);
			T task = efTask.build(null);
			task.setCode(clWithTask.getSimpleName()+":"+ejb.getId());
			ejb.setTask(task);
			try
			{
				ejb = this.update(ejb);
			}
			catch (JeeslConstraintViolationException e) {return fcTask(clTask, clWithTask, ejb);}
			catch (JeeslLockingException e) {return fcTask(clTask, clWithTask, ejb);}
		}
		return ejb.getTask();
	}

	@Override
	public <T extends UtilsTask<T>> T load(Class<T> clTask, T task)
	{
		task = em.find(clTask, task.getId());
		if(task.getChilds()!=null && task.getChilds().size()>0)
		{
			for(T t : task.getChilds())
			{
				t = this.load(clTask, t);
			}
		}
		return task;
	}

	@Override
	public <T extends UtilsTask<T>, WT extends EjbWithTask<T>> T fTask(Class<T> clTask, Class<WT> clWithTask, WT ejb)
			throws JeeslNotFoundException
	{
		ejb = em.find(clWithTask, ejb.getId());
		if(ejb.getTask()==null){throw new JeeslNotFoundException("No Task found for "+ejb.toString());}
		else{return ejb.getTask();}
	}
}