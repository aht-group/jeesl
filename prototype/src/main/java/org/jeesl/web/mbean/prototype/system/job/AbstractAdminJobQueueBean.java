package org.jeesl.web.mbean.prototype.system.job;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.api.handler.sb.SbDateIntervalSelection;
import org.jeesl.controller.handler.sb.SbDateIntervalHandler;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.job.JeeslJob;
import org.jeesl.interfaces.model.system.job.JeeslJobCategory;
import org.jeesl.interfaces.model.system.job.JeeslJobPriority;
import org.jeesl.interfaces.model.system.job.JeeslJobRobot;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.JeeslJobType;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobExpiration;
import org.jeesl.interfaces.model.system.job.feedback.JeeslJobFeedback;
import org.jeesl.interfaces.model.system.job.feedback.JeeslJobFeedbackType;
import org.jeesl.interfaces.model.system.job.mnt.JeeslJobMaintenance;
import org.jeesl.interfaces.model.system.job.mnt.JeeslJobMaintenanceInfo;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAdminJobQueueBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									TEMPLATE extends JeeslJobTemplate<L,D,CATEGORY,TYPE,PRIORITY,EXPIRE>,
									CATEGORY extends JeeslJobCategory<L,D,CATEGORY,?>,
									TYPE extends JeeslJobType<L,D,TYPE,?>,
									EXPIRE extends JeeslJobExpiration<L,D,EXPIRE,?>,
									JOB extends JeeslJob<TEMPLATE,PRIORITY,STATUS,USER>,
									PRIORITY extends JeeslJobPriority<L,D,PRIORITY,?>,
									FEEDBACK extends JeeslJobFeedback<JOB,FT,USER>,
									FT extends JeeslJobFeedbackType<L,D,FT,?>,
									STATUS extends JeeslJobStatus<L,D,STATUS,?>,
									ROBOT extends JeeslJobRobot<L,D>,
									CACHE extends JeeslJobCache<TEMPLATE,CONTAINER>,
									MNT extends JeeslJobMaintenance<L,D,MNT,?>,
									MNI extends JeeslJobMaintenanceInfo<D,STATUS,MNT>,
									CONTAINER extends JeeslFileContainer<?,?>,
									USER extends JeeslSimpleUser
									>
					extends AbstractAdminJobBean<L,D,LOC,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,CONTAINER,USER>
					implements Serializable,SbDateIntervalSelection
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminJobQueueBean.class);
	
	private List<JOB> jobs; public List<JOB> getJobs() {return jobs;}
	
	private JOB job; public JOB getJob() {return job;} public void setJob(JOB job) {this.job = job;}
	
	private SbDateIntervalHandler sbhDate; public SbDateIntervalHandler getSbhDate() {return sbhDate;}

	public AbstractAdminJobQueueBean(JobFactoryBuilder<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,USER> fbJob){super(fbJob);}
	
	protected void postConstructJobQueue(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslJobFacade<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,CONTAINER,USER> fJob)
	{
		super.postConstructAbstractJob(bTranslation,bMessage,fJob);
		
		sbhDate = new SbDateIntervalHandler(this);
		sbhDate.initWeeksToNow(2);
		
		sbhStatus.select(fJob.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.queue));
		sbhStatus.select(fJob.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.deferred));
		sbhStatus.select(fJob.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.timeout));
		sbhStatus.select(fJob.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.failed));
		sbhStatus.select(fJob.fByEnum(fbJob.getClassStatus(),JeeslJobStatus.Code.working));
		
		if(debugOnInfo)
		{
			logger.info(AbstractLogMessage.multiStatus(fbJob.getClassCategory(),sbhCategory.getSelected(),sbhCategory.getList()));
			logger.info(AbstractLogMessage.multiStatus(fbJob.getClassType(),sbhType.getSelected(),sbhType.getList()));
			logger.info(AbstractLogMessage.multiStatus(fbJob.getClassStatus(),sbhStatus.getSelected(),sbhStatus.getList()));
		}
		this.reloadJobs();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c)
	{
		logger.trace(AbstractLogMessage.toggled(c));
		super.toggled(handler,c);
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
	
	
	public void cancelJob() {clear(true);}
	private void clear(boolean rJob)
	{
		if(rJob){job=null;}
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
	
	public void saveJob() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(job));}
		job.setStatus(fJob.find(fbJob.getClassStatus(),job.getStatus()));
		job = fJob.save(job);
		reloadJobs();
	}
}