package org.jeesl.api.facade.system;

import java.util.Date;
import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
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
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob1;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob2;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob3;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;

public interface JeeslJobFacade <L extends JeeslLang,D extends JeeslDescription,
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
			extends JeeslFacade
{	
	<E extends Enum<E>> TEMPLATE fJobTemplate(E type, String code) throws JeeslNotFoundException;
	List<JOB> fJobs(List<CATEGORY> categories, List<TYPE> type, List<STATUS> status, Date from, Date to);
	List<JOB> fJobs(TEMPLATE template, String code);
	
	JOB fActiveJob(TEMPLATE template, String code) throws JeeslNotFoundException;
	JOB cJob(USER user, List<FEEDBACK> feedbacks, TEMPLATE template, String code, String name, String jsonFilter) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException;
	CACHE fJobCache(TEMPLATE template, String code) throws JeeslNotFoundException;
	CACHE uJobCache(TEMPLATE template, String code, byte[] data) throws JeeslConstraintViolationException, JeeslLockingException;
	
	<T extends EjbWithMigrationJob1<STATUS>> List<T> fEntitiesWithPendingJob1(Class<T> c, int maxResult, boolean includeNull);
	<T extends EjbWithMigrationJob2<STATUS>> List<T> fEntitiesWithPendingJob2(Class<T> c, int maxResult, boolean includeNull);
	<T extends EjbWithMigrationJob3<STATUS>> List<T> fEntitiesWithPendingJob3(Class<T> c, int maxResult, boolean includeNull);
	
	<T extends EjbWithMigrationJob2<STATUS>> List<T> fEntitiesWithJob2In(Class<T> c, List<STATUS> list, Integer maxResults);
	
	<T extends EjbWithMigrationJob1<STATUS>> JsonTuples1<STATUS> tpcJob1Status(Class<T> c);
	<T extends EjbWithMigrationJob2<STATUS>> JsonTuples1<STATUS> tpcJob2Status(Class<T> c);
	<T extends EjbWithMigrationJob3<STATUS>> JsonTuples1<STATUS> tpcJob3Status(Class<T> c);
	
	
}