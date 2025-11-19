package org.jeesl.controller.web.system.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.util.comparator.ejb.system.job.JobMaintenanceInfoComparator;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.factory.ejb.system.job.mnt.EjbJobMaintenanceInfoFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.processor.system.job.JeeslJobMaitenanceTupler;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenance;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenanceInfo;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslJobMaintenanceController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
													STATUS extends JeeslJobStatus<L,D,STATUS,?>,
													MNT extends JeeslJobMaintenance<L,D,MNT,?>,
													MNI extends JeeslJobMaintenanceInfo<D,STATUS,MNT>>
						extends AbstractJeeslLocaleWebController<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslJobMaintenanceController.class);
	
	protected JeeslJobFacade<?,?,?,?,?,?,?,STATUS,?,?,MNT,MNI,?,?> fJob;
	protected final JobFactoryBuilder<L,D,?,?,?,?,?,?,?,?,STATUS,?,?,MNT,MNI,?> fbJob;
	
	private final Comparator<MNI> cpInfo;
	
	private final EjbJobMaintenanceInfoFactory<STATUS,MNT,MNI> efInfo;
	
	private final JeeslJobMaitenanceTupler<STATUS,MNT> tupler;
	
	private final Map<MNT,JsonTuple1Handler<STATUS>> mapTh; public Map<MNT,JsonTuple1Handler<STATUS>> getMapTh() {return mapTh;}
	
	private final List<STATUS> stati; public List<STATUS> getStati() {return stati;}
	private final List<STATUS> statusHeader; public List<STATUS> getStatusHeader() {return statusHeader;}
	private final List<MNT> maintenances; public List<MNT> getMaintenances(){return maintenances;}
	private final List<MNI> infos; public List<MNI> getInfos(){return infos;}
	
	private MNT maintenance; public MNT getMaintenance() {return maintenance;} public void setMaintenance(MNT maintenance) {this.maintenance = maintenance;}
	private MNI info; public MNI getInfo() {return info;} public void setInfo(MNI info) {this.info = info;}

	public JeeslJobMaintenanceController(JeeslJobMaitenanceTupler<STATUS,MNT> tupler, final JobFactoryBuilder<L,D,?,?,?,?,?,?,?,?,STATUS,?,?,MNT,MNI,?> fbJob)
	{
		super(fbJob.getClassL(),fbJob.getClassD());
		this.tupler=tupler;
		this.fbJob=fbJob;
		
		cpInfo = fbJob.comparatorInfo(JobMaintenanceInfoComparator.Type.statusPosition);
		efInfo = fbJob.ejbMaintenanceInfo();
		
		mapTh = new HashMap<>();
		
		statusHeader = new ArrayList<>();
		stati = new ArrayList<>();
		maintenances = new ArrayList<>();
		infos = new ArrayList<>();
	}
	
	public void postConstructJobMaintenance(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
											JeeslJobFacade<?,?,?,?,?,?,?,STATUS,?,?,MNT,MNI,?,?> fJob)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fJob=fJob;
		
		stati.addAll(fJob.all(fbJob.getClassStatus()));
		maintenances.addAll(fJob.allOrderedPosition(fbJob.getClassMaintenance()));
		
		for(MNT m : maintenances)
		{
			mapTh.put(m, new JsonTuple1Handler<>(fbJob.getClassStatus()));
		}
				
		try
		{
			STATUS zeroStatus = fbJob.getClassStatus().newInstance();
			zeroStatus.setId(0);zeroStatus.setCode("null");
			statusHeader.add(zeroStatus);
		}
		catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
		statusHeader.addAll(stati);
		
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
		JsonTuples1<STATUS> tuples = tupler.calculateTuples(maintenance);
		if(Objects.isNull(tuples)) {logger.warn("Tuples not implemented");}
		else {mapTh.get(maintenance).load(tuples);}
		
		logger.info("Statistics for "+maintenance.getCode());
		JsonTuple1Handler<STATUS> th = mapTh.get(maintenance);
		for(STATUS s : stati)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(s.getCode()).append(": ");
			if(th.contains(s)) {sb.append(th.getMap1().get(s).getCount1());}
			else {sb.append("--");}
			logger.info(sb.toString());
		}
	}
	
	protected void reloadInfos()
	{
		this.reset(true,false);
		infos.addAll(fJob.allForParent(fbJob.getClassMaintenanceInfo(),maintenance));
		Collections.sort(infos,cpInfo);
	}
	
	public void addInfo()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbJob.getClassMaintenanceInfo()));}
		this.reset(false,true);
		info = efInfo.build(maintenance,null);
		info.setDescription(efDescription.buildEmpty(lp.getLocales()));
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