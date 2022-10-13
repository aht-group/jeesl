package org.jeesl.web.controller.system.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.factory.ejb.system.job.mnt.EjbJobMaintenanceInfoFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.mnt.JeeslJobMaintenance;
import org.jeesl.interfaces.model.system.job.mnt.JeeslJobMaintenanceInfo;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.util.comparator.ejb.system.job.JobMaintenanceInfoComparator;
import org.jeesl.web.controller.AbstractJeeslWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslSystemJobMaintenanceController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
													STATUS extends JeeslJobStatus<L,D,STATUS,?>,
													MNT extends JeeslJobMaintenance<L,D,MNT,?>,
													MNI extends JeeslJobMaintenanceInfo<D,STATUS,MNT>
>
						extends AbstractJeeslWebController<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSystemJobMaintenanceController.class);
	
	protected JeeslJobFacade<L,D,?,?,?,?,?,?,?,?,STATUS,?,?,MNT,MNI,?,?> fJob;
	protected final JobFactoryBuilder<L,D,?,?,?,?,?,?,?,?,STATUS,?,?,MNT,MNI,?> fbJob;
	
	private final Comparator<MNI> cpInfo;
	
	private final EjbJobMaintenanceInfoFactory<STATUS,MNT,MNI> efInfo;
	
	private final List<STATUS> stati; public List<STATUS> getStati(){return stati;}
	private final List<MNT> maintenances; public List<MNT> getMaintenances(){return maintenances;}
	private final List<MNI> infos; public List<MNI> getInfos(){return infos;}
	
	private MNT maintenance; public MNT getMaintenance() {return maintenance;} public void setMaintenance(MNT maintenance) {this.maintenance = maintenance;}
	private MNI info; public MNI getInfo() {return info;} public void setInfo(MNI info) {this.info = info;}

	public JeeslSystemJobMaintenanceController(final JobFactoryBuilder<L,D,?,?,?,?,?,?,?,?,STATUS,?,?,MNT,MNI,?> fbJob)
	{
		super(fbJob.getClassL(),fbJob.getClassD());
		this.fbJob=fbJob;
		
		cpInfo = fbJob.comparatorInfo(JobMaintenanceInfoComparator.Type.statusPosition);
		efInfo = fbJob.ejbMaintenanceInfo();
		
		stati = new ArrayList<>();
		maintenances = new ArrayList<>();
		infos = new ArrayList<>();
	}
	
	public void postConstructJobMaintenance(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage, JeeslJobFacade<L,D,?,?,?,?,?,?,?,?,STATUS,?,?,MNT,MNI,?,?> fJob)
	{
		super.postConstructWebController(lp,bMessage);
		this.fJob=fJob;
	
		stati.addAll(fJob.all(fbJob.getClassStatus()));
		maintenances.addAll(fJob.allOrderedPosition(fbJob.getClassMaintenance()));
		
		if(debugOnInfo)
		{
			logger.info(AbstractLogMessage.reloaded(fbJob.getClassStatus(), stati));
			logger.info(AbstractLogMessage.reloaded(fbJob.getClassMaintenance(), maintenances));
		}
	}
	
	private void reset(boolean rInfos, boolean rInfo)
	{
		if(rInfos) {infos.clear();}
		if(rInfo) {info=null;}
	}
	
	public void selectMaintenance()
	{
		this.reset(true,true);
		this.reloadInfos();
	}
	
	protected void reloadInfos()
	{
		this.reset(true,false);
		infos.addAll(fJob.allForParent(fbJob.getClassMaintenanceInfo(),maintenance));
		Collections.sort(infos,cpInfo);
	}
	
	public void addInfo()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.addEntity(fbJob.getClassMaintenanceInfo()));}
		this.reset(false,true);
		info = efInfo.build(maintenance,null);
		info.setDescription(efDescription.createEmpty(lp.getLocales()));
	}
	
	public void selectInfo()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(info));}
		info = fJob.find(fbJob.getClassMaintenanceInfo(),info);
		info = efDescription.persistMissingLangs(fJob,lp.getLocales(),info);
	}
	
	public void saveInfo() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(info));}
		efInfo.converter(fJob,info);
		info = fJob.save(info);
		this.reloadInfos();
	}
	
	public void deleteInfo() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.addEntity(info));}
		fJob.rm(info);
		this.reset(false,true);
		this.reloadInfos();
	}
}