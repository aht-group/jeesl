package org.jeesl.web.controller.system.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.api.facade.system.JeeslJobFacade;
import org.jeesl.factory.builder.io.IoDbFactoryBuilder;
import org.jeesl.factory.builder.system.JobFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbDateSelectionBean;
import org.jeesl.interfaces.bean.sb.handler.SbDateSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.db.JeeslDbDump;
import org.jeesl.interfaces.model.io.db.JeeslDbDumpFile;
import org.jeesl.interfaces.model.io.db.JeeslDbDumpStatus;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.job.JeeslJob;
import org.jeesl.interfaces.model.system.job.JeeslJobCategory;
import org.jeesl.interfaces.model.system.job.JeeslJobPriority;
import org.jeesl.interfaces.model.system.job.JeeslJobRobot;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.job.JeeslJobType;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobCache;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobExpiration;
import org.jeesl.interfaces.model.system.job.feedback.JeeslJobFeedback;
import org.jeesl.interfaces.model.system.job.feedback.JeeslJobFeedbackType;
import org.jeesl.interfaces.model.system.job.mnt.JeeslJobMaintenance;
import org.jeesl.interfaces.model.system.job.mnt.JeeslJobMaintenanceInfo;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;
import org.jeesl.jsf.handler.sb.SbDateHandler;
import org.jeesl.util.comparator.ejb.RecordComparator;
import org.jeesl.web.controller.AbstractJeeslWebController;
import org.metachart.xml.chart.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.DateUtil;

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
	
	private List<MNT> maintenances; public List<MNT> getMaintenances(){return maintenances;}
	
	private MNT maintenance; public MNT getMaintenance() {return maintenance;} public void setMaintenance(MNT maintenance) {this.maintenance = maintenance;}

	public JeeslSystemJobMaintenanceController(final JobFactoryBuilder<L,D,?,?,?,?,?,?,?,?,STATUS,?,?,MNT,MNI,?> fbJob)
	{
		super(fbJob.getClassL(),fbJob.getClassD());
		this.fbJob=fbJob;
		
		maintenances = new ArrayList<>();
	}
	
	public void postConstructJobMaintenance(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage, JeeslJobFacade<L,D,?,?,?,?,?,?,?,?,STATUS,?,?,MNT,MNI,?,?> fJob)
	{
		this.fJob=fJob;
	
		reloadHosts();
	}
	
	protected void reloadHosts()
	{
		maintenances.clear();
//		maintenances.addAll(fJob.all(fbJob.getClass));
	}
	
}