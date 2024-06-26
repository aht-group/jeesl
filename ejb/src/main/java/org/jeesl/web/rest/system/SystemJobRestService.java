package org.jeesl.web.rest.system;

import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.api.rest.rs.system.job.JeeslJobRest;
import org.jeesl.api.rest.rs.system.job.JeeslJobRestExport;
import org.jeesl.api.rest.rs.system.job.JeeslJobRestImport;
import org.jeesl.factory.xml.module.job.XmlJobsFactory;
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
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.model.xml.system.job.Jobs;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.web.rest.AbstractJeeslRestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

public class SystemJobRestService <L extends JeeslLang,D extends JeeslDescription,
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
					extends AbstractJeeslRestHandler<L,D>
					implements JeeslJobRestExport,JeeslJobRestImport,JeeslJobRest
{
	final static Logger logger = LoggerFactory.getLogger(SystemJobRestService.class);
	
	private JeeslJobFacade<TEMPLATE,CATEGORY,TYPE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,CONTAINER,USER> fJob;
	
	private final Class<CATEGORY> cCategory;
	private final Class<TYPE> cType;
	private final Class<STATUS> cStatus;
	private final Class<FT> cFeedbackType;
	
	private SystemJobRestService(JeeslJobFacade<TEMPLATE,CATEGORY,TYPE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,CONTAINER,USER> fJob, final Class<L> cL, final Class<D> cD, Class<CATEGORY> cCategory, final Class<TYPE> cType, final Class<FT> cFeedbackType, final Class<STATUS> cStatus)
	{
		super(fJob,cL,cD);
		this.fJob=fJob;
		this.cCategory=cCategory;
		this.cType=cType;
		this.cFeedbackType=cFeedbackType;
		this.cStatus=cStatus;
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
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
	SystemJobRestService<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,CONTAINER,USER>
		factory(JeeslJobFacade<TEMPLATE,CATEGORY,TYPE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,MNT,MNI,CONTAINER,USER> fJob,final Class<L> cL, final Class<D> cD, Class<CATEGORY> cCategory, final Class<TYPE> cType, final Class<FT> cFeedbackType, final Class<STATUS> cStatus)
	{
		return new SystemJobRestService<>(fJob,cL,cD,cCategory,cType,cFeedbackType,cStatus);
	}
	
	@Override public Container exportSystemJobCategories() {return xfContainer.build(fJob.allOrderedPosition(cCategory));}
	@Override public Container exportSystemJobType() {return xfContainer.build(fJob.allOrderedPosition(cType));}
	@Override public Container exportSystemJobStatus() {return xfContainer.build(fJob.allOrderedPosition(cStatus));}
	@Override public Container exportSystemJobFeedback() {return xfContainer.build(fJob.allOrderedPosition(cFeedbackType));}
	
	@Override public DataUpdate importSystemJobCategories(Container container){return importStatus(cCategory,container,null);}
	@Override public DataUpdate importSystemJobType(Container container){return importStatus(cType,container,null);}
	@Override public DataUpdate importSystemJobStatus(Container container){return importStatus(cStatus,container,null);}
	@Override public DataUpdate importSystemJobFeedback(Container container){return importStatus(cFeedbackType,container,null);}

	@Override public Jobs grab(String type, int max)
	{
		logger.info("TYEP: "+type+" MAX:"+max);
		Jobs xml = XmlJobsFactory.build();
		
		return xml;
	}
}