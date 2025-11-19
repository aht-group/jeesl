package org.jeesl.controller.web.system.job;

import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.factory.ejb.system.job.EjbJobTemplateFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobExpiration;
import org.jeesl.interfaces.model.system.job.core.JeeslJobPriority;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.template.JeeslJobCategory;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.job.template.JeeslJobType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.util.query.system.JeeslJobQuery;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.util.query.ejb.system.EjbJobQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslJobTemplateController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										TEMPLATE extends JeeslJobTemplate<L,D,CATEGORY,TYPE,PRIORITY,EXPIRE>,
										CATEGORY extends JeeslJobCategory<L,D,CATEGORY,?>,
										TYPE extends JeeslJobType<L,D,TYPE,?>,
										EXPIRE extends JeeslJobExpiration<L,D,EXPIRE,?>,
										PRIORITY extends JeeslJobPriority<L,D,PRIORITY,?>,
										STATUS extends JeeslJobStatus<L,D,STATUS,?>,
										CACHE extends JeeslJobCache<TEMPLATE,?>
										>
						extends AbstractJeeslLocaleWebController<L,D,LOC>
						implements SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslJobTemplateController.class);
	
	private JeeslJobFacade<TEMPLATE,CATEGORY,TYPE,?,PRIORITY,?,?,STATUS,?,CACHE,?,?,?,?> fJob;
	
	private final JobFactoryBuilder<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,?,PRIORITY,?,?,?,?,CACHE,?,?,?> fbJob;
	
	private final SbMultiHandler<CATEGORY> sbhCategory; public SbMultiHandler<CATEGORY> getSbhCategory() {return sbhCategory;}
	private SbMultiHandler<TYPE> sbhType; public SbMultiHandler<TYPE> getSbhType() {return sbhType;}
	private final SbMultiHandler<PRIORITY> sbhPriority; public SbMultiHandler<PRIORITY> getSbhPriority() {return sbhPriority;}
	
	private final JsonTuple1Handler<TEMPLATE> thJob; public JsonTuple1Handler<TEMPLATE> getThJob() {return thJob;}
	private final JsonTuple1Handler<TEMPLATE> thCache; public JsonTuple1Handler<TEMPLATE> getThCache() {return thCache;}
	
	private List<TEMPLATE> templates; public List<TEMPLATE> getTemplates() {return templates;}
	private List<EXPIRE> expirations; public List<EXPIRE> getExpirations() {return expirations;}
	
	private TEMPLATE template; public TEMPLATE getTemplate() {return template;} public void setTemplate(TEMPLATE template) {this.template = template;}

	private EjbJobTemplateFactory<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,PRIORITY> efTemplate;
	
	public JeeslJobTemplateController(JobFactoryBuilder<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,?,PRIORITY,?,?,?,?,CACHE,?,?,?> fbJob)
	{
		super(fbJob.getClassL(),fbJob.getClassD());
		this.fbJob=fbJob;
		
		sbhCategory = SbMultiHandler.instance(fbJob.getClassCategory(), this);
		sbhType = new SbMultiHandler<>(fbJob.getClassType(),this);
		sbhPriority = new SbMultiHandler<>(fbJob.getClassPriority(),this);
		
		thJob = JsonTuple1Handler.instance(fbJob.getClassTemplate());
		thCache = JsonTuple1Handler.instance(fbJob.getClassTemplate());
	}
	
	public void postConstructJobTemplate(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
											JeeslJobFacade<TEMPLATE,CATEGORY,TYPE,?,PRIORITY,?,?,STATUS,?,CACHE,?,?,?,?> fJob)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fJob=fJob;
		
		efTemplate = fbJob.template();
		
		sbhCategory.setList(fJob.allOrderedPositionVisible(fbJob.getClassCategory()));
		sbhType.setList(fJob.allOrderedPositionVisible(fbJob.getClassType()));
		sbhPriority.setList(fJob.allOrderedPositionVisible(fbJob.getClassPriority()));
		expirations = fJob.allOrderedPosition(fbJob.getClassExpire());
		
		sbhCategory.selectAll();
		
		if(debugOnInfo)
		{
			logger.info(AbstractLogMessage.multiStatus(fbJob.getClassCategory(),sbhCategory.getSelected(),sbhCategory.getList()));
			logger.info(AbstractLogMessage.multiStatus(fbJob.getClassType(),sbhType.getSelected(),sbhType.getList()));
		}
		this.reloadTemplates();
		
		JeeslJobQuery<TEMPLATE,STATUS> query = new EjbJobQuery<TEMPLATE,STATUS>();
		
		thJob.load(fJob.tpJobJobByTemplate(query));
		thCache.load(fJob.tpJobCacheByTemplate(query));
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
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
		template.setName(efLang.buildEmpty(lp.getLocales()));
		template.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	
	public void selectTemplate()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(template));}
		template = efLang.persistMissingLangs(fJob, lp.getLocales(), template);
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