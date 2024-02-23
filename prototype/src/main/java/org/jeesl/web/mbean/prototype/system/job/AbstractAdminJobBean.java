package org.jeesl.web.mbean.prototype.system.job;

import java.io.Serializable;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.feedback.JeeslJobFeedback;
import org.jeesl.interfaces.model.system.feedback.JeeslJobFeedbackType;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobExpiration;
import org.jeesl.interfaces.model.system.job.core.JeeslJob;
import org.jeesl.interfaces.model.system.job.core.JeeslJobPriority;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenance;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenanceInfo;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobRobot;
import org.jeesl.interfaces.model.system.job.template.JeeslJobCategory;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.job.template.JeeslJobType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAdminJobBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									TEMPLATE extends JeeslJobTemplate<L,D,CATEGORY,TYPE,PRIORITY,EXPIRE>,
									CATEGORY extends JeeslJobCategory<L,D,CATEGORY,?>,
									TYPE extends JeeslJobType<L,D,TYPE,?>,
									EXPIRE extends JeeslJobExpiration<L,D,EXPIRE,?>,
									JOB extends JeeslJob<TEMPLATE,PRIORITY,STATUS,USER>,
									PRIORITY extends JeeslJobPriority<L,D,PRIORITY,?>,
									FEEDBACK extends JeeslJobFeedback<FT,USER>,
									FT extends JeeslJobFeedbackType<L,D,FT,?>,
									STATUS extends JeeslJobStatus<L,D,STATUS,?>,
									ROBOT extends JeeslJobRobot<L,D>,
									CACHE extends JeeslJobCache<TEMPLATE,CONTAINER>,
									MNT extends JeeslJobMaintenance<L,D,MNT,?>,
									MNI extends JeeslJobMaintenanceInfo<D,STATUS,MNT>,
									CONTAINER extends JeeslFileContainer<?,?>,
									USER extends JeeslSimpleUser
									>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminJobBean.class);
	
	protected JeeslJobFacade<TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,CONTAINER,USER> fJob;
	protected final JobFactoryBuilder<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,USER> fbJob;
	
	protected SbMultiHandler<CATEGORY> sbhCategory; public SbMultiHandler<CATEGORY> getSbhCategory() {return sbhCategory;}
	protected SbMultiHandler<TYPE> sbhType; public SbMultiHandler<TYPE> getSbhType() {return sbhType;}
	protected final SbMultiHandler<STATUS> sbhStatus; public SbMultiHandler<STATUS> getSbhStatus() {return sbhStatus;}
	protected final SbMultiHandler<PRIORITY> sbhPriority; public SbMultiHandler<PRIORITY> getSbhPriority() {return sbhPriority;}

	public AbstractAdminJobBean(JobFactoryBuilder<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,USER> fbJob)
	{
		super(fbJob.getClassL(),fbJob.getClassD());
		this.fbJob=fbJob;
		
		sbhStatus = new SbMultiHandler<STATUS>(fbJob.getClassStatus(),this);
		sbhPriority = new SbMultiHandler<PRIORITY>(fbJob.getClassPriority(),this);
	}
	
	protected void postConstructAbstractJob(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
											JeeslJobFacade<TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,CONTAINER,USER> fJob)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fJob=fJob;
		
		sbhCategory = new SbMultiHandler<CATEGORY>(fbJob.getClassCategory(),fJob.allOrderedPositionVisible(fbJob.getClassCategory()),this);
		sbhCategory.selectAll();
		
		sbhType = new SbMultiHandler<TYPE>(fbJob.getClassType(),fJob.allOrderedPositionVisible(fbJob.getClassType()),this);
		sbhType.selectAll();
		
		sbhStatus.setList(fJob.allOrderedPositionVisible(fbJob.getClassStatus()));
		sbhPriority.setList(fJob.allOrderedPositionVisible(fbJob.getClassPriority()));
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c)
	{
		
	}
}