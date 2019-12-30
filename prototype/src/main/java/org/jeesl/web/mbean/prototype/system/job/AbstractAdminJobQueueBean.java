package org.jeesl.web.mbean.prototype.system.job;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.api.handler.sb.SbDateIntervalSelection;
import org.jeesl.controller.handler.sb.SbDateHandler;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.interfaces.model.system.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.job.JeeslJob;
import org.jeesl.interfaces.model.system.job.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.JeeslJobCategory;
import org.jeesl.interfaces.model.system.job.JeeslJobExpiration;
import org.jeesl.interfaces.model.system.job.JeeslJobFeedback;
import org.jeesl.interfaces.model.system.job.JeeslJobFeedbackType;
import org.jeesl.interfaces.model.system.job.JeeslJobPriority;
import org.jeesl.interfaces.model.system.job.JeeslJobRobot;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.job.JeeslJobType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.exception.ejb.UtilsConstraintViolationException;
import net.sf.ahtutils.exception.ejb.UtilsLockingException;
import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.interfaces.model.with.EjbWithEmail;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAdminJobQueueBean <L extends UtilsLang, D extends UtilsDescription, LOC extends UtilsStatus<LOC,L,D>,
									TEMPLATE extends JeeslJobTemplate<L,D,CATEGORY,TYPE,PRIORITY>,
									CATEGORY extends JeeslJobCategory<L,D,CATEGORY,?>,
									TYPE extends JeeslJobType<L,D,TYPE,?>,
									EXPIRE extends JeeslJobExpiration<L,D,EXPIRE,?>,
									JOB extends JeeslJob<TEMPLATE,PRIORITY,FEEDBACK,STATUS,USER>,
									PRIORITY extends JeeslJobPriority<L,D,PRIORITY,?>,
									FEEDBACK extends JeeslJobFeedback<JOB,FT,USER>,
									FT extends JeeslJobFeedbackType<L,D,FT,?>,
									STATUS extends JeeslJobStatus<L,D,STATUS,?>,
									ROBOT extends JeeslJobRobot<L,D>,
									CACHE extends JeeslJobCache<TEMPLATE,CONTAINER>,
									CONTAINER extends JeeslFileContainer<?,?>,
									USER extends EjbWithEmail
									>
					extends AbstractAdminJobBean<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,CONTAINER,USER>
					implements Serializable,SbDateIntervalSelection
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminJobQueueBean.class);
	
	private List<JOB> jobs; public List<JOB> getJobs() {return jobs;}
	
	private JOB job; public JOB getJob() {return job;} public void setJob(JOB job) {this.job = job;}
	
	private SbDateHandler sbhDate; public SbDateHandler getSbhDate() {return sbhDate;}

	public AbstractAdminJobQueueBean(JobFactoryBuilder<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,USER> fbJob){super(fbJob);}
	
	protected void postConstructJobQueue(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslJobFacade<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,CONTAINER,USER> fJob)
	{
		super.postConstructAbstractJob(bTranslation,bMessage,fJob);
		
		sbhDate = new SbDateHandler(this);
		sbhDate.initWeeksToNow(2);
		
		try
		{
			sbhStatus.select(fJob.fByCode(fbJob.getClassStatus(),JeeslJobStatus.Code.queue));
			sbhStatus.select(fJob.fByCode(fbJob.getClassStatus(),JeeslJobStatus.Code.timeout));
			sbhStatus.select(fJob.fByCode(fbJob.getClassStatus(),JeeslJobStatus.Code.failed));
			sbhStatus.select(fJob.fByCode(fbJob.getClassStatus(),JeeslJobStatus.Code.working));
		}
		catch (UtilsNotFoundException e) {logger.error(e.getMessage());}
		
		if(debugOnInfo)
		{
			logger.info(AbstractLogMessage.multiStatus(fbJob.getClassCategory(),sbhCategory.getSelected(),sbhCategory.getList()));
			logger.info(AbstractLogMessage.multiStatus(fbJob.getClassType(),sbhType.getSelected(),sbhType.getList()));
			logger.info(AbstractLogMessage.multiStatus(fbJob.getClassStatus(),sbhStatus.getSelected(),sbhStatus.getList()));
		}
		reloadJobs();
	}
	
	@Override public void toggled(Class<?> c)
	{
		logger.trace(AbstractLogMessage.toggled(c));
		super.toggled(c);
		if(fbJob.getClassCategory().isAssignableFrom(c)){logger.trace(fbJob.getClassCategory().getName());}
		else if(fbJob.getClassType().isAssignableFrom(c)){logger.trace(fbJob.getClassType().getName());}
		else if(fbJob.getClassStatus().isAssignableFrom(c)){logger.trace(fbJob.getClassStatus().getName());}
		reloadJobs();
		clear(true);
	}
	
	@Override public void callbackDateChanged()
	{
		reloadJobs();
		clear(true);
	}
	
	private void clear(boolean clearJob)
	{
		if(clearJob){job=null;}
	}
	
	private void reloadJobs()
	{
		jobs = fJob.fJobs(sbhCategory.getSelected(),sbhType.getSelected(),sbhStatus.getSelected(),sbhDate.getDate1(),sbhDate.getDate2());
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbJob.getClassJob(),jobs));}
//		Collections.sort(templates, comparatorTemplate);
	}
		
	public void selectJob()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(job));}
	}
	
	public void saveJob() throws UtilsConstraintViolationException, UtilsLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(job));}
		job.setStatus(fJob.find(fbJob.getClassStatus(),job.getStatus()));
		job = fJob.save(job);
		reloadJobs();
	}
}