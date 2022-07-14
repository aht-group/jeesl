package org.jeesl.controller.monitoring.counter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.factory.txt.util.TxtPeriodFactory;
import org.jeesl.interfaces.controller.ProgressTimeTracker;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessingTimeTracker implements ProgressTimeTracker
{
	final static Logger logger = LoggerFactory.getLogger(ProcessingTimeTracker.class);
	
	private long start;
	private long stop;
	private long previousEvent;
	private int counter;
	private int delayOnTick; public void setDelayOnTick(int delayOnTick) {this.delayOnTick = delayOnTick;}

	private final TxtPeriodFactory tfPeriod;
	
	private Map<String,Long> buckets;
	private Map<String,Long> bucketStart;
	
	public ProcessingTimeTracker() {this(false);}
	private ProcessingTimeTracker(boolean autoStart)
	{
		counter=0;
		start=0;
		previousEvent=0;
		stop=0;
		delayOnTick=0;
		
		tfPeriod = new TxtPeriodFactory();
		tfPeriod.setUnits(TxtPeriodFactory.UNITS.minuteSecondMilli);
		
		buckets = new HashMap<String,Long>();
		bucketStart = new HashMap<String,Long>();
		
		if(autoStart){start();}
	}
	
	public static ProcessingTimeTracker instance() {return new ProcessingTimeTracker();}
	
	public ProcessingTimeTracker start() {start = System.currentTimeMillis();previousEvent=start;return this;}
	public void stop() {stop = System.currentTimeMillis();}
	
	public void round()
	{
		counter++;
	}
	
	public int getCounter() {return counter;}
	
	public String toTotalTime()
	{
		if(stop==0){stop();}
		StringBuilder sb = new StringBuilder();
		sb.append(stop-start);
		sb.append(" ms");
		return sb.toString();
	}
	
	public String debugEvent(String event)
	{
		long now = System.currentTimeMillis();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		sb.append(tfPeriod.debugMillis(now-previousEvent));
		sb.append("]  [total: ");
		sb.append(tfPeriod.debugMillis(now-start));
		sb.append("] for ");
		sb.append(event);
		previousEvent = now;
		return sb.toString();
	}
	
	public String toTotalPeriod()
	{
		if(stop==0){stop();}	
		Period period = new Period(new DateTime(start), new DateTime());
		return PeriodFormat.getDefault().print(period);
	}
	
	public String buildDefaultDebug(String prefix)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		sb.append(" ").append(counter).append(" times");
		sb.append(" in ").append(toTotalTime());
		if(counter>0)
		{
			long total = stop-start;
			sb.append(" avg: "+(total/counter));
		}
		return sb.toString();
	}
	
	private List<Long> ticksTime;
	private List<String> ticksMarker;
	private String ticker;
	
	public void startTicker(String ticker)
	{
		this.ticker=ticker;
		ticksTime = new ArrayList<Long>();
		ticksMarker = new ArrayList<String>();
		start = System.currentTimeMillis();
	}
	
	public void tick(String s)
	{
		ticksTime.add(System.currentTimeMillis());
		ticksMarker.add(s);
		if(delayOnTick>0)
		{
			logger.info(s);
			try {Thread.sleep(delayOnTick*1000);}
			catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public void debugTicker(boolean print) {if(print) {debugTicker();}}
	public void debugTicker()
	{
		logger.info("Debugging Ticker ("+ticksTime.size()+"): "+ticker);
		for(int i=0;i<ticksTime.size();i++)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("\t");
			long ms = ticksTime.get(i)-start;
			sb.append(ms);
			sb.append("\t");
			sb.append(ticksMarker.get(i));
			
			logger.info(sb.toString());
		}
	}
	
	public <E extends Enum<E>> void pB(E code)
	{
		bucketStart.put(code.toString(), System.currentTimeMillis());
	}
	
	public <E extends Enum<E>> void rB(E code)
	{
		String key = code.toString();
		if(bucketStart.containsKey(key))
		{
			if(!buckets.containsKey(key)) {buckets.put(key,0l);}
			long start = bucketStart.get(key);
			
			long diff = System.currentTimeMillis()-start;
			buckets.put(key,buckets.get(key)+diff);
		}
	}
	
	public <E extends Enum<E>> void debugBucket(E... codes)
	{
		for(E code : codes)
		{
			logger.info(code.toString()+" "+buckets.get(code.toString()));
		}
	}
}