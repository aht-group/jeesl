package org.jeesl.web.mbean.prototype.io.db;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.interfaces.model.io.db.JeeslDbPoolStatistic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractDbPoolBean <L extends JeeslLang, D extends JeeslDescription,
									STATISTIC extends JeeslDbPoolStatistic<L,D,STATISTIC,?>> 
implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractDbPoolBean.class);
	
	protected JeeslIoDbFacade<?,?,?,?,?,?> fDb;
	
	protected final Map<STATISTIC,Long> map; public Map<STATISTIC,Long> getMap() {return map;}

	protected final List<STATISTIC> statistics; public List<STATISTIC> getStatistics() {return statistics;}

	private final Class<STATISTIC> cStatistic;
	private final String dsName;
	
	public AbstractDbPoolBean(Class<STATISTIC> cStatistic, String dsName)
	{
		this.cStatistic=cStatistic;
		this.dsName=dsName;
		map = new HashMap<>();
		statistics = new ArrayList<>();
	}
	
	public void postConstruct(JeeslIoDbFacade<?,?,?,?,?,?> fDb)
	{
		this.fDb=fDb;
		statistics.addAll(fDb.allOrderedPositionVisible(cStatistic));
		
		try
		{
			// Creating the JMX connection
			MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();

			// Look for the DB pool
			ObjectName pool = new ObjectName("jboss.as:subsystem=datasources,data-source="+dsName+",statistics=pool");

			for(STATISTIC s : statistics)
			{
				Long value=null;
				Object o = platformMBeanServer.getAttribute(pool,s.getCode());
				if(o instanceof Integer) {value = Integer.valueOf((Integer)o).longValue();}
				else if(o instanceof Long) {value = (Long)o;}
				else
				{
					logger.warn("Unknown: "+s.getCode()+" "+o.getClass().getName()+" "+o.toString());
				}
				if(value!=null) {map.put(s, value);}
			}
		}
		catch (Exception e)
		{
			logger.error("Could not request JMX statistics on database connection pool: " +e.getClass().getCanonicalName() +" (" +e.getMessage() +")");
		}
	}
}