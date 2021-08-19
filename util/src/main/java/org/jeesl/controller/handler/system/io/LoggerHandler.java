package org.jeesl.controller.handler.system.io;

import java.io.OutputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.controller.processor.arithmetic.NullCalculator;
import org.jeesl.controller.processor.finance.AmountRounder;
import org.jeesl.factory.builder.io.IoLogFactoryBuilder;
import org.jeesl.factory.ejb.io.log.EjbIoLogMilestoneFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.io.logging.JeeslIoLog;
import org.jeesl.interfaces.model.io.logging.JeeslIoLogEvent;
import org.jeesl.interfaces.model.io.logging.JeeslIoLogLoop;
import org.jeesl.interfaces.model.io.logging.JeeslIoLogMilestone;
import org.jeesl.interfaces.model.io.logging.JeeslIoLogRetention;
import org.jeesl.interfaces.model.io.logging.JeeslIoLogStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.openfuxml.factory.xml.table.XmlTableFactory;
import org.openfuxml.renderer.text.OfxTextSilentRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerHandler<L extends JeeslLang, D extends JeeslDescription,
							LOG extends JeeslIoLog<L,D,STATUS,RETENTION,USER>,
							STATUS extends JeeslIoLogStatus<L,D,STATUS,?>,
							RETENTION extends JeeslIoLogRetention<L,D,RETENTION,?>,
							MILESTONE extends JeeslIoLogMilestone<LOG>,
							LOOP extends JeeslIoLogLoop<LOG>,
							EVENT extends JeeslIoLogEvent<LOG>,
							USER extends JeeslSimpleUser>
				implements JeeslLogger
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(LoggerHandler.class);
	
	private final IoLogFactoryBuilder<L,D,LOG,MILESTONE,LOOP> fbLog;
	private final EjbIoLogMilestoneFactory<LOG,MILESTONE> efMilestone;
	
	private Instant timeStart;
	private Instant timeMilestone;
	
	private final List<MILESTONE> milestones;
	private final Map<String,LOOP> loops;
	
	private final Map<String,Instant> mapLoopInstant;
	private final Map<String,Integer> mapCount;
	
	private final Class<?> c;
	private LOG log;
	
	public LoggerHandler(IoLogFactoryBuilder<L,D,LOG,MILESTONE,LOOP> fbLog,
					   Class<?> c)
	{
		this.fbLog=fbLog;
		this.c=c;
		efMilestone = fbLog.ejbMilestone();
		
		milestones = new ArrayList<>();
		loops = new HashMap<>();
		
		mapLoopInstant = new HashMap<>();
		mapCount = new HashMap<>();
	}
	
	private void reset()
	{
		milestones.clear();
		loops.clear();
		mapLoopInstant.clear();
	}
	
	@Override public String start(String log) {return start(log,null,null);}
	public String start(String log, USER user) {return start(log,null,user);}
	public String start(String log, String message) {return start(log,message,null);}
	public String start(String log, String message, USER user)
	{
		reset();
		timeStart = Instant.now();
		timeMilestone = Instant.now();
		milestone(log,message);
		StringBuilder sb = new StringBuilder();
		sb.append("Starting logging in ").append(c.getSimpleName());
		sb.append(": ").append(log);
		if(user!=null) {sb.append(" by ").append(user.getEmail());}
		return sb.toString();
	}
	
	public String milestone(String milestone) {return milestone(milestone,null,null);}
	public String milestone(String milestone, String message){return milestone(milestone,message,null);}
	public String milestone(String milestone, Integer elements) {return milestone(milestone,null,elements);}
	public String milestone(String milestone, String message, Integer elements)
	{
		Instant timeNow = Instant.now();
		MILESTONE ejb = efMilestone.build(log);
		ejb.setRecord(Date.from(timeNow));
		ejb.setMilliTotal(ChronoUnit.MILLIS.between(timeStart,timeNow));
		ejb.setMilliStep(ChronoUnit.MILLIS.between(timeMilestone,timeNow));
		
		if(elements!=null && elements.intValue()!=0) {ejb.setMilliRelative(ejb.getMilliStep()/elements);}
		else {ejb.setMilliRelative(ejb.getMilliStep());}
		
		ejb.setName(milestone);
		ejb.setMessage(message);
		ejb.setElements(elements);
		
		milestones.add(ejb);
	
		StringBuilder sb = new StringBuilder();
		sb.append("Milestone ");
		sb.append(milestone);
		if(message!=null) {sb.append(": ").append(message);}
		
		sb.append(" (");
		if(elements!=null) {sb.append(elements).append(" ");}
		sb.append("in ").append(ejb.getMilliStep()).append("ms)");
		
		timeMilestone = timeNow;
		return sb.toString();
	}
	
	public <E extends Enum<E>> String loopStart(E code) {return loopStart(code.toString());}
	private String loopStart(String loopCode)
	{
		if(!loops.containsKey(loopCode))
		{
			loops.put(loopCode,fbLog.ejbLoop().build(log,loopCode));
		}
		mapLoopInstant.put(loopCode,Instant.now());
		return "";
	}
	public <E extends Enum<E>> String loopEnd(E code, Integer elements)
	{
		if(!loops.containsKey(code.toString())) {logger.warn("Loop not started");}
		else
		{
			LOOP loop = loops.get(code.toString());
			loop.setCounter(loop.getCounter()+1);
			loop.setElements(NullCalculator.add(loop.getElements(),elements));
			
			Instant timeBefore = mapLoopInstant.get(code.toString());
			Instant timeNow = Instant.now();
			loop.setMilliTotal(loop.getMilliTotal()+ChronoUnit.MILLIS.between(timeBefore,timeNow));
		}
		
		return "";
	}
	
	public void count(String code)
	{
		mapCount.putIfAbsent(code,0);
		mapCount.put(code,1+mapCount.get(code));
	}
	
	@Override public void ofxMilestones(OutputStream os)
	{
		List<String> header = new ArrayList<>();
		header.add("Time");
		
		header.add("Total");
		header.add("Step");
		header.add("Elements");
		header.add("Relative");
		
		header.add("Milestone");
		header.add("Message");
		
		List<Object[]> data = new ArrayList<>();
		
		for(MILESTONE ejb : milestones)
		{
			String[] cell = new String[7];
			cell[0] = ejb.getRecord().toString();
			
			cell[1] = Long.valueOf(ejb.getMilliTotal()).toString();
			cell[2] = Long.valueOf(ejb.getMilliStep()).toString();
			if(ejb.getElements()!=null) {cell[3] = ejb.getElements().toString();}else {cell[3]="";}
			cell[4] = Long.valueOf(ejb.getMilliRelative()).toString();
			
			cell[5] = ejb.getName();
			cell[6] = ejb.getMessage();
			data.add(cell);
		}
		
		OfxTextSilentRenderer.table(XmlTableFactory.build(c.getSimpleName(),header,data),os);
	}
	
	public void ofxLoops(OutputStream os)
	{
		List<String> header = new ArrayList<>();
		header.add("Count");
		header.add("Elements");
		header.add("Total");
		header.add("ms/loop");
		header.add("Loop");
		
		List<Object[]> data = new ArrayList<>();
		
		for(String key : loops.keySet())
		{
			LOOP loop = loops.get(key);
			String[] cell = new String[5];
			
			cell[0] = Integer.valueOf(loop.getCounter()).toString();
			if(loop.getElements()!=null) {cell[1] = loop.getElements().toString();} else {cell[1] ="-";}
			cell[2] = Long.valueOf(loop.getMilliTotal()).toString();
			cell[3] = Double.valueOf(AmountRounder.one(loop.getMilliTotal()/loop.getCounter())).toString();
			cell[4] = loop.getCode();
			
			data.add(cell);
		}
		
		OfxTextSilentRenderer.table(XmlTableFactory.build(c.getSimpleName(),header,data),os);
	}
	
	public void ofxCount(OutputStream os)
	{
		List<String> header = new ArrayList<>();
		header.add("Code");
		header.add("Count");
		
		List<Object[]> data = new ArrayList<>();
		
		for(String key : mapCount.keySet())
		{
			String[] cell = new String[2];
			
			cell[0] = key;
			cell[1] = Integer.valueOf(mapCount.get(key)).toString();
			
			data.add(cell);
		}
		
		OfxTextSilentRenderer.table(XmlTableFactory.build(c.getSimpleName(),header,data),os);
	}
}