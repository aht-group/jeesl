package org.jeesl.web.mbean.prototype.io.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.api.handler.sb.SbDateIntervalSelection;
import org.jeesl.controller.handler.sb.SbDateHandler;
import org.jeesl.factory.builder.io.IoDbFactoryBuilder;
import org.jeesl.interfaces.model.io.db.JeeslDbDump;
import org.jeesl.interfaces.model.io.db.JeeslDbDumpFile;
import org.jeesl.interfaces.model.io.db.JeeslDbDumpStatus;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.util.comparator.ejb.RecordComparator;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.metachart.xml.chart.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractDbBackupBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SYSTEM extends JeeslIoSsiSystem<L,D>,
									DUMP extends JeeslDbDump<SYSTEM,DF>,
									DF extends JeeslDbDumpFile<DUMP,DH,DS>,
									DH extends JeeslIoSsiHost<L,D,?>,
									DS extends JeeslDbDumpStatus<L,D,DS,?>>
						extends AbstractAdminBean<L,D,LOC>
						implements Serializable,SbDateIntervalSelection
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractDbBackupBean.class);
	
	private JeeslIoDbFacade<L,D,SYSTEM,DUMP,DF,DH,DS> fDb;
	private final IoDbFactoryBuilder<L,D,SYSTEM,DUMP,DF,DH,DS,?,?,?,?,?> fbDb;
	
	private SbDateHandler sbDateHandler; public SbDateHandler getSbDateHandler() {return sbDateHandler;}
	protected Chart chart; public Chart getChart() {return chart;}

	private List<DUMP> dumps; public List<DUMP> getDumps(){return dumps;}
	protected final List<DH> hosts; public List<DH> getHosts() {return hosts;}
	private Map<DUMP,Map<DH,DF>> mapFiles; public Map<DUMP, Map<DH,DF>> getMapFiles() {return mapFiles;}
	
	@SuppressWarnings("unused")
	private SYSTEM system;
	
	public AbstractDbBackupBean(final IoDbFactoryBuilder<L,D,SYSTEM,DUMP,DF,DH,DS,?,?,?,?,?> fbDb)
	{
		super(fbDb.getClassL(),fbDb.getClassD());
		this.fbDb=fbDb;
		
		hosts = new ArrayList<>();
		sbDateHandler = new SbDateHandler(this);
		sbDateHandler.setEnforceStartOfDay(true);
		sbDateHandler.initWeeksToNow(2);
	}
	
	public void postConstructDbBackup(JeeslIoDbFacade<L,D,SYSTEM,DUMP,DF,DH,DS> fDb, SYSTEM system)
	{
		this.fDb=fDb;
		this.system=system;
		reloadHosts();
		refreshList(); 
	}
	
	protected void reloadHosts()
	{
		hosts.addAll(fDb.all(fbDb.getClassDumpHost()));
		logger.info(AbstractLogMessage.reloaded(fbDb.getClassDumpHost(),hosts)+" in abstract");
	}
	
	@Override public void callbackDateChanged()
	{
		refreshList();
	}
	
	protected void refreshList()
	{		
		dumps = fDb.inInterval(fbDb.getClassDump(),sbDateHandler.getDate1(),sbDateHandler.toDate2Plus1());
		Collections.sort(dumps,new RecordComparator<DUMP>());
		Collections.reverse(dumps);
		
		mapFiles = new HashMap<DUMP,Map<DH,DF>>();
		for(DUMP d : dumps)
		{
			mapFiles.put(d,new HashMap<DH,DF>());
		}
		
		List<DF> files = fDb.all(fbDb.getClassDumpFile());
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