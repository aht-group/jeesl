
package net.sf.ahtutils.controller.facade;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jsoup.helper.StringUtil;
import org.openfuxml.content.table.Table;
import org.openfuxml.factory.xml.table.OfxTableFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.facade.UtilsDbFacade;

public class UtilsDbFacadeBean extends UtilsFacadeBean implements UtilsDbFacade
{
	final static Logger logger = LoggerFactory.getLogger(UtilsDbFacadeBean.class);
	
	public UtilsDbFacadeBean(EntityManager em){this(em,false);}
	public UtilsDbFacadeBean(EntityManager em, boolean handleTransaction)
	{
		super(em,handleTransaction);
	}
	
	@Override public  long count(Class<?> cl)
	{
		Query q = em.createQuery("select count(*) FROM "+ cl.getSimpleName());

		Object o = q.getSingleResult();
		return (Long)o;
	}
	
	@Override
	public Map<Class<?>, Long> count(List<Class<?>> list)
	{
		Map<Class<?>, Long> result = new Hashtable<Class<?>,Long>();
		for(Class<?> c : list)
		{
			result.put(c,count(c));
		}
		return result;
	}
	
	@Override
	public Table connections(String dbName)
	{
		List<String> fileds = new ArrayList<String>();
		fileds.add("pid");
//		fileds.add("datname");
//		fileds.add("client_addr");
		fileds.add("state");
		fileds.add("xact_start");
		fileds.add("query");
		
		List<Object[]> data = new ArrayList<Object[]>();
		for(Object o : em.createNativeQuery("SELECT "+StringUtil.join(fileds, ",")+" FROM pg_stat_activity WHERE datname='"+dbName+"'").getResultList())
		{
			Object[] array = (Object[])o;
			data.add(array);
//			debugDataTypes(array);
		}
		
		Table table = OfxTableFactory.build(fileds,data);
		
		return table;
	}
	
	public static void debugDataTypes(Object[] array)
	{
		logger.info("*****************************");
		int i=0;
		for(Object o : array)
		{
			if(o!=null)
			{
				logger.info(i+" "+o.getClass().getName());
			}
			else
			{
				logger.info(i+" NULL");
			}
			i++;
		}
	}
}