package org.jeesl.web.mbean.prototype.system.job;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.factory.ejb.system.job.EjbJobTemplateFactory;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

@Deprecated
public class AbstractAdminJobTemplateBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
					extends AbstractAdminJobBean<L,D,LOC,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,CONTAINER,USER>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminJobTemplateBean.class);
	
	private List<TEMPLATE> templates; public List<TEMPLATE> getTemplates() {return templates;}
	private List<EXPIRE> expirations; public List<EXPIRE> getExpirations() {return expirations;}
	
	private TEMPLATE template; public TEMPLATE getTemplate() {return template;} public void setTemplate(TEMPLATE template) {this.template = template;}

	private EjbJobTemplateFactory<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,PRIORITY> efTemplate;
	
	public AbstractAdminJobTemplateBean(JobFactoryBuilder<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,USER> fbJob) {super(fbJob);}
	
	protected void postConstructJobTemplate(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
											JeeslJobFacade<TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,CONTAINER,USER> fJob)
	{
		super.postConstructAbstractJob(bTranslation,bMessage,fJob);
		
		expirations = fJob.allOrderedPosition(fbJob.getClassExpire());
		
		efTemplate = fbJob.template();
		
		if(debugOnInfo)
		{
			logger.info(AbstractLogMessage.multiStatus(fbJob.getClassCategory(),sbhCategory.getSelected(),sbhCategory.getList()));
			logger.info(AbstractLogMessage.multiStatus(fbJob.getClassType(),sbhType.getSelected(),sbhType.getList()));
		}
		reloadTemplates();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
		super.toggled(handler,c);
		reloadTemplates();
		reset(true);
	}
	
	public void cancelTemplate(){reset(true);}
	private void reset(boolean rTemplate)
	{
		if(rTemplate){template=null;}
	}
	
	private void reloadTemplates()
	{
//		jobs = fJob.fJobs(sbhCategory.getSelected(),sbhType.getSelected(),sbhStatus.getSelected());
		templates = fJob.all(fbJob.getClassTemplate());
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbJob.getClassTemplate(),templates));}
//		Collections.sort(templates, comparatorTemplate);
	}
	
	public void addTemplate()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbJob.getClassTemplate()));}
		template = efTemplate.build(null,null);
		template.setName(efLang.createEmpty(localeCodes));
		template.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void selectTemplate()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(template));}
		template = efLang.persistMissingLangs(fJob, lp, template);
		template = efDescription.persistMissingLangs(fJob, lp.getLocales(), template);
	}
	
	public void saveTemplate() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(template));}
		efTemplate.converter(fJob,template);
		
		template = fJob.save(template);
		reloadTemplates();
	}
	
	public void deleteTemplate() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(template));}
		fJob.rm(template);
		reloadTemplates();
		reset(true);
	}
	
	public void reorderTemplates() throws JeeslConstraintViolationException, JeeslLockingException
	{
//		PositionListReorderer.reorder(fJob,templates);
	}
}