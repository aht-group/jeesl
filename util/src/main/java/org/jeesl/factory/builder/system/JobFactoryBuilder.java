package org.jeesl.factory.builder.system;

import java.util.Comparator;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.system.job.EjbJobCacheFactory;
import org.jeesl.factory.ejb.system.job.EjbJobFactory;
import org.jeesl.factory.ejb.system.job.EjbJobRobotFactory;
import org.jeesl.factory.ejb.system.job.EjbJobTemplateFactory;
import org.jeesl.factory.ejb.system.job.mnt.EjbJobMaintenanceInfoFactory;
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
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.util.comparator.ejb.system.job.JobMaintenanceInfoComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								TEMPLATE extends JeeslJobTemplate<L,D,CATEGORY,TYPE,PRIORITY,EXPIRE>,
								CATEGORY extends JeeslJobCategory<L,D,CATEGORY,?>,
								TYPE extends JeeslJobType<L,D,TYPE,?>,
								EXPIRE extends JeeslJobExpiration<L,D,EXPIRE,?>,
								JOB extends JeeslJob<TEMPLATE,PRIORITY,STATUS,USER>,
								PRIORITY extends JeeslJobPriority<L,D,PRIORITY,?>,
								FB extends JeeslJobFeedback<JOB,FBT,USER>,	
								FBT extends JeeslJobFeedbackType<L,D,FBT,?>,
								STATUS extends JeeslJobStatus<L,D,STATUS,?>,
								ROBOT extends JeeslJobRobot<L,D>,
								CACHE extends JeeslJobCache<TEMPLATE,?>,
								MNT extends JeeslJobMaintenance<L,D,MNT,?>,
								MNI extends JeeslJobMaintenanceInfo<D,STATUS,MNT>,
								USER extends JeeslSimpleUser
								>
				extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(JobFactoryBuilder.class);
	
	private final Class<TEMPLATE> cTemplate; public Class<TEMPLATE> getClassTemplate(){return cTemplate;}
	private final Class<CATEGORY> cCategory; public Class<CATEGORY> getClassCategory(){return cCategory;}
	private final Class<TYPE> cType; public Class<TYPE> getClassType(){return cType;}
	private final Class<EXPIRE> cExpire; public Class<EXPIRE> getClassExpire(){return cExpire;}
	private final Class<JOB> cJob; public Class<JOB> getClassJob(){return cJob;}
	private final Class<PRIORITY> cPriority; public Class<PRIORITY> getClassPriority(){return cPriority;}
	private final Class<STATUS> cStatus; public Class<STATUS> getClassStatus(){return cStatus;}
	private final Class<ROBOT> cRobot; public Class<ROBOT> getClassRobot(){return cRobot;}
	private final Class<CACHE> cCache; public Class<CACHE> getClassCache(){return cCache;}
	private final Class<MNT> cMaintenance; public Class<MNT> getClassMaintenance() {return cMaintenance;}
	private final Class<MNI> cMaintenanceInfo; public Class<MNI> getClassMaintenanceInfo() {return cMaintenanceInfo;}

	public JobFactoryBuilder(final Class<L> cL, final Class<D> cD,
			final Class<TEMPLATE> cTemplate, final Class<CATEGORY> cCategory, final Class<TYPE> cType, final Class<EXPIRE> cExpire,
			final Class<JOB> cJob, final Class<PRIORITY> cPriority, final Class<STATUS> cStatus, final Class<ROBOT> cRobot, final Class<CACHE> cCache,
			final Class<MNT> cMaintenance, final Class<MNI> cMaintenanceInfo)
	{
		super(cL,cD);
		this.cTemplate = cTemplate;
		this.cCategory=cCategory;
		this.cType=cType;
		this.cExpire=cExpire;
		this.cJob = cJob;
		this.cPriority = cPriority;
		this.cStatus=cStatus;
		this.cRobot = cRobot;
		this.cCache = cCache;
		this.cMaintenance = cMaintenance;
		this.cMaintenanceInfo = cMaintenanceInfo;
	}
		
	public EjbJobTemplateFactory<L,D,TEMPLATE,CATEGORY,TYPE,EXPIRE,PRIORITY> template() {return new EjbJobTemplateFactory<>(cTemplate,cCategory,cType,cExpire,cPriority);}
	public EjbJobFactory<L,D,TEMPLATE,CATEGORY,TYPE,JOB,PRIORITY,FB,FBT,STATUS,ROBOT,CACHE,USER> job(){return new EjbJobFactory<>(cJob);}
	
	public EjbJobRobotFactory<ROBOT> robot(){return new EjbJobRobotFactory<>(cRobot);}
	public EjbJobCacheFactory<TEMPLATE,CACHE> cache() {return new EjbJobCacheFactory<>(cCache);}
	public EjbJobMaintenanceInfoFactory<STATUS,MNT,MNI> ejbMaintenanceInfo() {return new EjbJobMaintenanceInfoFactory<>(cStatus,cMaintenance,cMaintenanceInfo);}
	
	public Comparator<MNI> comparatorInfo(JobMaintenanceInfoComparator.Type type) {return (new JobMaintenanceInfoComparator<STATUS,MNI>()).factory(type);}
}