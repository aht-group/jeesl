package org.jeesl.web.rest.system;

import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.api.rest.system.job.JeeslJobRest;
import org.jeesl.api.rest.system.job.JeeslJobRestExport;
import org.jeesl.api.rest.system.job.JeeslJobRestImport;
import org.jeesl.factory.xml.module.job.XmlJobsFactory;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
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
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.model.xml.module.job.Jobs;
import org.jeesl.web.rest.AbstractJeeslRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.sync.DataUpdate;

public class SystemJobRestService <L extends JeeslLang,D extends JeeslDescription,
							TEMPLATE extends JeeslJobTemplate<L,D,CATEGORY,TYPE,PRIORITY,EXPIRE>,
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
					extends AbstractJeeslRestService<L,D>
					implements JeeslJobRestExport,JeeslJobRestImport,JeeslJobRest
{
	final static Logger logger = LoggerFactory.getLogger(SystemJobRestService.class);
	
	private JeeslJobFacade<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,CONTAINER,USER> fJob;
	
	private final Class<CATEGORY> cCategory;
	private final Class<TYPE> cType;
	private final Class<STATUS> cStatus;
	private final Class<FT> cFeedbackType;
	
	private SystemJobRestService(JeeslJobFacade<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,CONTAINER,USER> fJob, final Class<L> cL, final Class<D> cD, Class<CATEGORY> cCategory, final Class<TYPE> cType, final Class<FT> cFeedbackType, final Class<STATUS> cStatus)
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
	SystemJobRestService<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,CONTAINER,USER>
		factory(JeeslJobFacade<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,CONTAINER,USER> fJob,final Class<L> cL, final Class<D> cD, Class<CATEGORY> cCategory, final Class<TYPE> cType, final Class<FT> cFeedbackType, final Class<STATUS> cStatus)
	{
		return new SystemJobRestService<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,CONTAINER,USER>(fJob,cL,cD,cCategory,cType,cFeedbackType,cStatus);
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