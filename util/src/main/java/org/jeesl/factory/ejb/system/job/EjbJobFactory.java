package org.jeesl.factory.ejb.system.job;

import java.util.Date;
import java.util.Objects;

import org.jeesl.interfaces.model.system.job.JeeslJob;
import org.jeesl.interfaces.model.system.job.JeeslJobCategory;
import org.jeesl.interfaces.model.system.job.JeeslJobPriority;
import org.jeesl.interfaces.model.system.job.JeeslJobRobot;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.JeeslJobType;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.feedback.JeeslJobFeedback;
import org.jeesl.interfaces.model.system.job.feedback.JeeslJobFeedbackType;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbJobFactory <L extends JeeslLang,D extends JeeslDescription,
									TEMPLATE extends JeeslJobTemplate<L,D,CATEGORY,TYPE,PRIORITY,?>,
									CATEGORY extends JeeslJobCategory<L,D,CATEGORY,?>,
									TYPE extends JeeslJobType<L,D,TYPE,?>,
									JOB extends JeeslJob<TEMPLATE,PRIORITY,STATUS,USER>,
									PRIORITY extends JeeslJobPriority<L,D,PRIORITY,?>,
									FEEDBACK extends JeeslJobFeedback<JOB,FT,USER>,
									FT extends JeeslJobFeedbackType<L,D,FT,?>,
									STATUS extends JeeslJobStatus<L,D,STATUS,?>,
									ROBOT extends JeeslJobRobot<L,D>,
									CACHE extends JeeslJobCache<TEMPLATE,?>,
									USER extends JeeslSimpleUser
									>
{
	final static Logger logger = LoggerFactory.getLogger(EjbJobFactory.class);
	
	private final Class<JOB> cJob;

	public EjbJobFactory(final Class<JOB> cJob)
	{
        this.cJob = cJob;
	}
 
	public JOB build(USER user, TEMPLATE template, STATUS status, String code, String name)
	{
		return this.build(user, template, status, code, name, null);
	}
	public JOB build(USER user, TEMPLATE template, STATUS status, String code, String name, String jsonFilter)
	{
		JOB ejb = null;
		try
		{
			ejb = cJob.newInstance();
			ejb.setUser(user);
			ejb.setTemplate(template);
			ejb.setStatus(status);
			if(Objects.nonNull(template)) {ejb.setPriority(template.getPriority());}
			ejb.setRecordCreation(new Date());
			ejb.setCode(code);
			ejb.setName(name);
			ejb.setJsonFilter(jsonFilter);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}