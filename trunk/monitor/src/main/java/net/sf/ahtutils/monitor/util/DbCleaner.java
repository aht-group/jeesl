package net.sf.ahtutils.monitor.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import net.sf.ahtutils.controller.facade.UtilsMonitoringFacadeBean;
import net.sf.ahtutils.interfaces.facade.UtilsMonitoringFacade;
import net.sf.ahtutils.model.interfaces.with.EjbWithRecord;
import net.sf.ahtutils.monitor.result.net.IcmpResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbCleaner
{
	final static Logger logger = LoggerFactory.getLogger(DbCleaner.class);
	
	private EntityManager em;
	private UtilsMonitoringFacade fUm;
	private Map<String,List<Long>> map;
	
	public DbCleaner(EntityManager em)
	{
		fUm = new UtilsMonitoringFacadeBean(em);
		map = new Hashtable<String,List<Long>>();
	}
	
	public void add(String t, long id)
	{
		getList(t).add(id);
	}
	
	public void clear()
	{
		map.clear();
	}
	
	private List<Long> getList(String t)
	{
		if(!map.containsKey(t))
		{
			map.put(t, new ArrayList<Long>());
		}
		return map.get(t);
	}
	
	public <T extends EjbWithRecord> void clean()
	{
		for(String t : map.keySet())
		{
			logger.info("Cleaning "+t+" with "+map.get(t).size());
			if(t.equals(IcmpResult.class.getName())){clean(IcmpResult.class, map.get(t));}
		}
	}
	
	private <T extends EjbWithRecord> void clean(Class<T> t, List<Long> list)
	{
//		 em.getTransaction().begin();
		for(Long id : list)
		{
//			logger.info("Deleting "+id);
		}
	}
}