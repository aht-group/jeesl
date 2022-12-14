package org.jeesl.controller.web.io.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.factory.builder.io.IoDbFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbDateSelectionBean;
import org.jeesl.interfaces.bean.sb.handler.SbDateSelection;
import org.jeesl.interfaces.model.io.db.JeeslDbDump;
import org.jeesl.interfaces.model.io.db.JeeslDbDumpFile;
import org.jeesl.interfaces.model.io.db.JeeslDbDumpStatus;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.handler.sb.SbDateHandler;
import org.jeesl.util.comparator.ejb.RecordComparator;
import org.metachart.xml.chart.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.DateUtil;

public class JeeslDbBackupController <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									DUMP extends JeeslDbDump<SYSTEM,DF>,
									DF extends JeeslDbDumpFile<DUMP,DH,DS>,
									DH extends JeeslIoSsiHost<L,D,?>,
									DS extends JeeslDbDumpStatus<L,D,DS,?>>
						extends AbstractJeeslWebController<L,D,LOC>
						implements Serializable,SbDateSelectionBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslDbBackupController.class);
	
	private JeeslIoDbFacade<L,D,SYSTEM,DUMP,DF,DH,DS> fDb;
	private final IoDbFactoryBuilder<L,D,SYSTEM,DUMP,DF,DH,DS,?,?,?,?,?> fbDb;
	
	private SbDateHandler sbhDate; public SbDateHandler getSbhDate() {return sbhDate;}

	private List<DUMP> dumps; public List<DUMP> getDumps(){return dumps;}
	protected final List<DH> hosts; public List<DH> getHosts() {return hosts;}
	private Map<DUMP,Map<DH,DF>> mapFiles; public Map<DUMP, Map<DH,DF>> getMapFiles() {return mapFiles;}
	
	@SuppressWarnings("unused")
	private SYSTEM system;
	protected Chart chart; public void setChart(Chart chart) {this.chart = chart;} public Chart getChart() {return chart;}
	
	public JeeslDbBackupController(final IoDbFactoryBuilder<L,D,SYSTEM,DUMP,DF,DH,DS,?,?,?,?,?> fbDb)
	{
		super(fbDb.getClassL(),fbDb.getClassD());
		this.fbDb=fbDb;
		
		hosts = new ArrayList<>();
		sbhDate = SbDateHandler.instance(this);
		sbhDate.initWeeks(2,0);
	}
	
	public void postConstructDbBackup(JeeslIoDbFacade<L,D,SYSTEM,DUMP,DF,DH,DS> fDb, SYSTEM system)
	{
		this.fDb=fDb;
		this.system=system;
		
		if(jogger!=null) {jogger.start("postConstructDbBackup");}
		reloadHosts();
		refreshList();
		if(jogger!=null) {jogger.ofxMilestones(System.out);}
	}
	
	protected void reloadHosts()
	{
		if(hosts.isEmpty())
		{
			hosts.addAll(fDb.all(fbDb.getClassDumpHost()));
			if(jogger!=null) {jogger.milestone(fbDb.getClassDumpHost().getSimpleName(),"reloaded",hosts.size());}
		}
	}
	
	@Override
	public void callbackDateChanged(SbDateSelection handler)
	{
		refreshList();	
	}
	
	protected void refreshList()
	{		
		dumps = fDb.inInterval(fbDb.getClassDump(),DateUtil.toDate(sbhDate.getDateFrom()),DateUtil.toDate(sbhDate.getDateTo()));
		Collections.sort(dumps,new RecordComparator<DUMP>());
		Collections.reverse(dumps);
		if(jogger!=null) {jogger.milestone(fbDb.getClassDump().getSimpleName(),"reloaded",dumps.size());}
		
		mapFiles = new HashMap<DUMP,Map<DH,DF>>();
		for(DUMP d : dumps)
		{
			mapFiles.put(d,new HashMap<DH,DF>());
		}
		
		List<DF> files = fDb.allForParents(fbDb.getClassDumpFile(),dumps);
		if(jogger!=null) {jogger.milestone(fbDb.getClassDumpFile().getSimpleName(),"reloaded",files.size());}
		for(DF f : files)
		{
			if(mapFiles.containsKey(f.getDump()))
			{
				mapFiles.get(f.getDump()).put(f.getHost(),f);
			}
		}
		
		logger.info(fbDb.getClassDump().getSimpleName()+": "+dumps.size());
		logger.info(fbDb.getClassDumpHost().getSimpleName()+": "+hosts.size());
		logger.info(fbDb.getClassDumpFile().getSimpleName()+": "+files.size());
	}
}