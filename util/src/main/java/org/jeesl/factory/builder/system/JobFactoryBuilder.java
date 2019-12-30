package org.jeesl.factory.builder.system;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.system.job.EjbJobCacheFactory;
import org.jeesl.factory.ejb.system.job.EjbJobFactory;
import org.jeesl.factory.ejb.system.job.EjbJobRobotFactory;
import org.jeesl.factory.ejb.system.job.EjbJobTemplateFactory;
import org.jeesl.interfaces.model.system.job.JeeslJob;
import org.jeesl.interfaces.model.system.job.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.JeeslJobCategory;
import org.jeesl.interfaces.model.system.job.JeeslJobFeedback;
import org.jeesl.interfaces.model.system.job.JeeslJobFeedbackType;
import org.jeesl.interfaces.model.system.job.JeeslJobPriority;
import org.jeesl.interfaces.model.system.job.JeeslJobRobot;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.job.JeeslJobType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.with.EjbWithEmail;

public class JobFactoryBuilder<L extends UtilsLang,D extends UtilsDescription,
								TEMPLATE extends JeeslJobTemplate<L,D,CATEGORY,TYPE,PRIORITY>,
								CATEGORY extends JeeslJobCategory<L,D,CATEGORY,?>,
								TYPE extends JeeslJobType<L,D,TYPE,?>,
								JOB extends JeeslJob<TEMPLATE,PRIORITY,FEEDBACK,STATUS,USER>,
								PRIORITY extends JeeslJobPriority<L,D,PRIORITY,?>,
								FEEDBACK extends JeeslJobFeedback<JOB,FT,USER>,	
								FT extends JeeslJobFeedbackType<L,D,FT,?>,
								STATUS extends JeeslJobStatus<L,D,STATUS,?>,
								ROBOT extends JeeslJobRobot<L,D>,
								CACHE extends JeeslJobCache<TEMPLATE,?>,
								USER extends EjbWithEmail
								>
				extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(JobFactoryBuilder.class);
	
	private final Class<TEMPLATE> cTemplate; public Class<TEMPLATE> getClassTemplate(){return cTemplate;}
	private final Class<CATEGORY> cCategory; public Class<CATEGORY> getClassCategory(){return cCategory;}
	private final Class<TYPE> cType; public Class<TYPE> getClassType(){return cType;}
	private final Class<JOB> cJob; public Class<JOB> getClassJob(){return cJob;}
	private final Class<PRIORITY> cPriority; public Class<PRIORITY> getClassPriority(){return cPriority;}
	private final Class<STATUS> cStatus; public Class<STATUS> getClassStatus(){return cStatus;}
	private final Class<ROBOT> cRobot; public Class<ROBOT> getClassRobot(){return cRobot;}
	private final Class<CACHE> cCache; public Class<CACHE> getClassCache(){return cCache;}
	
	public JobFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<TEMPLATE> cTemplate, final Class<CATEGORY> cCategory, final Class<TYPE> cType, final Class<JOB> cJob, final Class<PRIORITY> cPriority, final Class<STATUS> cStatus, final Class<ROBOT> cRobot, final Class<CACHE> cCache)
	{
		super(cL,cD);
		this.cTemplate = cTemplate;
		this.cCategory=cCategory;
		this.cType=cType;
		this.cJob = cJob;
		this.cPriority = cPriority;
		this.cStatus=cStatus;
		this.cRobot = cRobot;
		this.cCache = cCache;
	}
		
	public EjbJobTemplateFactory<L,D,TEMPLATE,CATEGORY,TYPE,PRIORITY> template()
	{
		return new EjbJobTemplateFactory<L,D,TEMPLATE,CATEGORY,TYPE,PRIORITY>(cTemplate);
	}
	
	public EjbJobFactory<L,D,TEMPLATE,CATEGORY,TYPE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,USER> job()
	{
		return new EjbJobFactory<L,D,TEMPLATE,CATEGORY,TYPE,JOB,PRIORITY,FEEDBACK,FT,STATUS,ROBOT,CACHE,USER>(cJob);
	}
	
	public EjbJobRobotFactory<ROBOT> robot(){return new EjbJobRobotFactory<>(cRobot);}
	public EjbJobCacheFactory<TEMPLATE,CACHE> cache() {return new EjbJobCacheFactory<>(cCache);}
}