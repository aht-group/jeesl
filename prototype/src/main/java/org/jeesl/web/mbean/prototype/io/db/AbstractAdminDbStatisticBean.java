package org.jeesl.web.mbean.prototype.io.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDump;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpFile;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDumpStatus;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAdminDbStatisticBean <L extends JeeslLang, D extends JeeslDescription,
											SYSTEM extends JeeslIoSsiSystem<L,D>,
											DUMP extends JeeslDbDump<SYSTEM,FILE>,
											FILE extends JeeslDbDumpFile<DUMP,HOST,STATUS>,
											HOST extends JeeslIoSsiHost<L,D,?>,
											STATUS extends JeeslDbDumpStatus<L,D,STATUS,?>> 
implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminDbStatisticBean.class);
	
	protected JeeslIoDbFacade<SYSTEM,DUMP,FILE,HOST,?,?,?,?,?,?> fDb;
	
	protected List<Class<?>> list = new ArrayList<Class<?>>();
	public List<Class<?>> getList(){return list;}
	
	private Map<Class<?>,Long> map;
	public Map<Class<?>, Long> getMap(){return map;}

	public AbstractAdminDbStatisticBean()
	{
		list = new ArrayList<Class<?>>();
	}
	
	public void refresh()
	{
		map = fDb.count(list);
		for(Class<?> cl : list)
		{
			logger.info(cl.getSimpleName()+": "+map.get(cl));
		}
	}
}