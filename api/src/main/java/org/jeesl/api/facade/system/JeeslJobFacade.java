package org.jeesl.api.facade.system;

import java.util.Date;
import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.feedback.JeeslJobFeedback;
import org.jeesl.interfaces.model.system.feedback.JeeslJobFeedbackType;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.core.JeeslJob;
import org.jeesl.interfaces.model.system.job.core.JeeslJobPriority;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenance;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenanceInfo;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobRobot;
import org.jeesl.interfaces.model.system.job.template.JeeslJobCategory;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.job.template.JeeslJobType;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob1;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob2;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob3;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob4;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob5;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.util.query.system.JeeslJobQuery;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;

public interface JeeslJobFacade <TEMPLATE extends JeeslJobTemplate<?,?,CATEGORY,TYPE,PRIORITY,?>,
								CATEGORY extends JeeslJobCategory<?,?,CATEGORY,?>,
								TYPE extends JeeslJobType<?,?,TYPE,?>,
								
								JOB extends JeeslJob<TEMPLATE,PRIORITY,STATUS,USER>,
								PRIORITY extends JeeslJobPriority<?,?,PRIORITY,?>,
								FEEDBACK extends JeeslJobFeedback<FT,USER>,
								FT extends JeeslJobFeedbackType<?,?,FT,?>,
								STATUS extends JeeslJobStatus<?,?,STATUS,?>,
								ROBOT extends JeeslJobRobot<?,?>,
								CACHE extends JeeslJobCache<TEMPLATE,CONTAINER>,
								MNT extends JeeslJobMaintenance<?,?,MNT,?>,
								MNI extends JeeslJobMaintenanceInfo<?,STATUS,MNT>,
								CONTAINER extends JeeslFileContainer<?,?>,
								USER extends JeeslSimpleUser
								>
			extends JeeslFacade
{	
	<E extends Enum<E>> TEMPLATE fJobTemplate(E type, String code) throws JeeslNotFoundException;
	List<JOB> fJobs(List<CATEGORY> categories, List<TYPE> type, List<STATUS> status, Date from, Date to);
	List<JOB> fJobs(TEMPLATE template, String code);
	List<JOB> fJobs(JeeslJobQuery<TEMPLATE,STATUS> query);
	
	JOB fActiveJob(TEMPLATE template, String code) throws JeeslNotFoundException;
	JOB cJob(USER user, List<FEEDBACK> feedbacks, TEMPLATE template, String code, String name, String jsonFilter) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException;
	CACHE fJobCache(TEMPLATE template, String code) throws JeeslNotFoundException;
	CACHE uJobCache(TEMPLATE template, String code, byte[] data) throws JeeslConstraintViolationException, JeeslLockingException;
	
	<T extends EjbWithMigrationJob1<STATUS>> List<T> fEntitiesWithPendingJob1(Class<T> c, int maxResult, boolean includeNull);
	<T extends EjbWithMigrationJob2<STATUS>> List<T> fEntitiesWithPendingJob2(Class<T> c, int maxResult, boolean includeNull);
	<T extends EjbWithMigrationJob3<STATUS>> List<T> fEntitiesWithPendingJob3(Class<T> c, int maxResult, boolean includeNull);
	<T extends EjbWithMigrationJob4<STATUS>> List<T> fEntitiesWithPendingJob4(Class<T> c, int maxResult, boolean includeNull);
	<T extends EjbWithMigrationJob5<STATUS>> List<T> fEntitiesWithPendingJob5(Class<T> c, int maxResult, boolean includeNull);
	
	<T extends EjbWithMigrationJob2<STATUS>> List<T> fEntitiesWithJob2In(Class<T> c, List<STATUS> list, Integer maxResults);
	
	<T extends EjbWithMigrationJob1<STATUS>> JsonTuples1<STATUS> tpcJob1Status(Class<T> c);
	<T extends EjbWithMigrationJob2<STATUS>> JsonTuples1<STATUS> tpcJob2Status(Class<T> c);
	<T extends EjbWithMigrationJob3<STATUS>> JsonTuples1<STATUS> tpcJob3Status(Class<T> c);
	<T extends EjbWithMigrationJob4<STATUS>> JsonTuples1<STATUS> tpcJob4Status(Class<T> c);
	<T extends EjbWithMigrationJob5<STATUS>> JsonTuples1<STATUS> tpcJob5Status(Class<T> c);
	
	JsonTuples1<TEMPLATE> tpJobJobByTemplate(JeeslJobQuery<TEMPLATE,STATUS> query);
	JsonTuples1<TEMPLATE> tpJobCacheByTemplate(JeeslJobQuery<TEMPLATE,STATUS> query);
	
}