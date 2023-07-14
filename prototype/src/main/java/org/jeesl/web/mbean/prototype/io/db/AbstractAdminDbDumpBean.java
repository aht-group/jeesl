package org.jeesl.web.mbean.prototype.io.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.factory.builder.io.IoDbFactoryBuilder;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDump;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpFile;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpStatus;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAdminDbDumpBean <L extends JeeslLang,D extends JeeslDescription,LOC extends JeeslStatus<L,D,LOC>,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										DUMP extends JeeslDbDump<SYSTEM,FILE>,
										FILE extends JeeslDbDumpFile<DUMP,HOST,STATUS>,
										HOST extends JeeslIoSsiHost<L,D,?>,
										STATUS extends JeeslDbDumpStatus<L,D,STATUS,?>>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminDbDumpBean.class);
	
	private JeeslIoDbFacade<SYSTEM,DUMP,FILE,HOST,?> fDb;
	private final IoDbFactoryBuilder<L,D,SYSTEM,DUMP,FILE,HOST,STATUS,?,?,?,?,?,?,?> fbDb;
	
	private List<DUMP> dumps; public List<DUMP> getDumps(){return dumps;}
	private List<HOST> hosts; public List<HOST> getHosts() {return hosts;}
	private Map<DUMP,Map<HOST,FILE>> mapFiles; public Map<DUMP, Map<HOST, FILE>> getMapFiles() {return mapFiles;}
	
	public AbstractAdminDbDumpBean(final IoDbFactoryBuilder<L,D,SYSTEM,DUMP,FILE,HOST,STATUS,?,?,?,?,?,?,?> fbDb)
	{
		this.fbDb=fbDb;
	}
	
	public void initSuper(JeeslIoDbFacade<SYSTEM,DUMP,FILE,HOST,?> fDb)
	{
		this.fDb=fDb;
		refreshList();
	}
	
	protected void refreshList()
	{
		dumps = fDb.allOrdered(fbDb.getClassDump(),JeeslDbDump.Attributes.record,false);
		hosts = fDb.all(fbDb.getClassDumpHost());
		
		mapFiles = new HashMap<DUMP,Map<HOST,FILE>>();
		for(DUMP d : dumps)
		{
			mapFiles.put(d,new HashMap<HOST,FILE>());
		}
		
		List<FILE> files = fDb.all(fbDb.getClassDumpFile());
		for(FILE f : files)
		{
			mapFiles.get(f.getDump()).put(f.getHost(),f);
		}
		
		logger.info(fbDb.getClassDump().getSimpleName()+": "+dumps.size());
		logger.info(fbDb.getClassDumpHost().getSimpleName()+": "+hosts.size());
		logger.info(fbDb.getClassDumpFile().getSimpleName()+": "+files.size());
	}
}