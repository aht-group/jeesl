package org.jeesl.web.controller.system.job;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.factory.ejb.system.job.EjbJobRobotFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.job.JeeslJob;
import org.jeesl.interfaces.model.system.job.JeeslJobRobot;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.web.controller.AbstractJeeslWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslJobRobotController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									JOB extends JeeslJob<?,?,?,STATUS,?>,
									STATUS extends JeeslJobStatus<L,D,STATUS,?>,
									ROBOT extends JeeslJobRobot<L,D>
									>
					extends AbstractJeeslWebController<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslJobRobotController.class);
	
	private JeeslJobFacade<L,D,?,?,?,?,JOB,?,?,?,STATUS,ROBOT,?,?,?,?,?> fJob;
	protected final JobFactoryBuilder<L,D,?,?,?,?,JOB,?,?,?,STATUS,ROBOT,?,?,?,?> fbJob;
	
	private List<ROBOT> robots; public List<ROBOT> getRobots() {return robots;}
	
	private ROBOT robot; public ROBOT getRobot() {return robot;} public void setRobot(ROBOT robot) {this.robot = robot;}
	
	private final EjbJobRobotFactory<ROBOT> efRobot;

	public JeeslJobRobotController(JobFactoryBuilder<L,D,?,?,?,?,JOB,?,?,?,STATUS,ROBOT,?,?,?,?> fbJob)
	{
		super(fbJob.getClassL(),fbJob.getClassD());
		this.fbJob=fbJob;
		
		efRobot = fbJob.robot();
	}
	
	public void postConstructJobMaintenance(JeeslJobFacade<L,D,?,?,?,?,JOB,?,?,?,STATUS,ROBOT,?,?,?,?,?> fJob,
											JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage)
	{
		super.postConstructWebController(lp,bMessage);
		this.fJob=fJob;
		
		reloadRobots();
	}
	
	public void cancelRobot(){reset(true);}
	private void reset(boolean rRobot)
	{
		if(rRobot){robot=null;}
	}
	
	private void reloadRobots()
	{
		robots = fJob.all(fbJob.getClassRobot());
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbJob.getClassRobot(),robots));}
	}
	
	public void addRobot()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbJob.getClassRobot()));}
		robot = efRobot.build();
		robot.setName(efLang.buildEmpty(lp.getLocales()));
		robot.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
		
	public void selectRobot()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(robot));}

		robot = efLang.persistMissingLangs(fJob,lp.getLocales(),robot);
		robot = efDescription.persistMissingLangs(fJob,lp.getLocales(),robot);
	}
	
	public void saveRobot() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(robot));}
		robot = fJob.save(robot);
		reloadRobots();
	}
}