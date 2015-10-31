package net.sf.ahtutils.monitor;

import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BucketSizeCounter
{
	final static Logger logger = LoggerFactory.getLogger(BucketSizeCounter.class);
	
	private String category;
	private Map<String,Long> map;
	
	private long loop;
	
	public BucketSizeCounter(){this("Default Category");}
	public BucketSizeCounter(String category)
	{
		this.category=category;
		map = new Hashtable<String,Long>();
		loop=0;
	}
	
	public <E extends Enum<E>> void add(E event, long size){add(event.toString(),size);}
	public void add(String event, long size)
	{
		if(!map.containsKey(event)){map.put(event, 0l);}
		map.put(event, map.get(event)+size);
	}
	
	public <E extends Enum<E>> long events(E event, long size){return events(event.toString());}
	public long events(String event)
	{
		if(map.containsKey(event)){return map.get(event);}
		else{return 0;}
	}
	
	public void debugAsFileSize()
	{
		logger.info("Sizes in category "+category);
		for(String key : map.keySet())
		{
			logger.info("\t"+key+": "+FileUtils.byteCountToDisplaySize(map.get(key)));
		}
	}
	
	public void debugLoop(int modulo)
	{
		loop++;
		if(loop%modulo==0)
		{
			logger.info(category+": "+loop);
		}
		
	}
}
