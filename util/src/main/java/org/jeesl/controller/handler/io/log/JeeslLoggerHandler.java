package org.jeesl.controller.handler.io.log;

import java.io.OutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.openfuxml.factory.xml.table.XmlTableFactory;
import org.openfuxml.renderer.text.OfxTextRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslLoggerHandler<L extends JeeslLang, D extends JeeslDescription,
							LOG extends JeeslIoLog<STATUS,RETENTION,USER>,
							STATUS extends JeeslIoLogStatus<L,D,STATUS,?>,
							RETENTION extends JeeslIoLogRetention<L,D,RETENTION,?>,
							MILESTONE extends JeeslIoLogMilestone<LOG>,
							LOOP extends JeeslIoLogLoop<LOG>,
							EVENT extends JeeslIoLogEvent<LOG>,
							USER extends JeeslSecurityUser>
				implements Serializable,JeeslLogger
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslLoggerHandler.class);
	
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
	private static final String msgNotActive = "Jogger is not active";
	
	public JeeslLoggerHandler(IoLogFactoryBuilder<L,D,LOG,MILESTONE,LOOP> fbLog, Class<?> c)
	{
		this.fbLog=fbLog;
		this.c=c;
		efMilestone = fbLog.ejbMilestone();
		
		milestones = new ArrayList<>();
		loops = new HashMap<>();
		
		mapLoopInstant = new HashMap<>();
		mapCount = new HashMap<>();
	}
	
	@Override public void reset()
	{
		timeStart = null;
		milestones.clear();
		loops.clear();
		mapLoopInstant.clear();
		mapCount.clear();
	}
	
	@Override public boolean isActive() {return Objects.nonNull(timeStart);}
	@Override public boolean isNotActive() {return Objects.isNull(timeStart);}
	
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
		if(Objects.nonNull(message)) {sb.append(" (").append(message).append(")");}
		if(Objects.nonNull(user)) {sb.append(" by ").append(user);}
		return sb.toString();
	}
	
	public String milestone(Class<?> c) {return milestone(c.getSimpleName(),null,null);}
	public String milestone(String milestone) {return milestone(milestone,null,null);}
	
	@Override public String milestone(Class<?> c, String message) {return milestone(c.getSimpleName(),message,null);}
	@Override public String milestone(String milestone, String message) {return milestone(milestone,message,null);}
	
	public String milestone(String milestone, Integer elements) {return milestone(milestone,null,elements);}
	public String milestone(Class<?> c, String message, Integer elements) {return milestone(c.getSimpleName(),message,elements);}
	public String milestone(String milestone, String message, Integer elements)
	{
		if(isActive())
		{
			Instant timeNow = Instant.now();
			MILESTONE ejb = efMilestone.build(log);
			ejb.setRecord(LocalDateTime.ofInstant(timeNow, ZoneOffset.UTC));
			if(Objects.isNull(timeStart)) {this.start("Implicit start");}
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
		else {return msgNotActive;}
	}
	
//	@Override public void milestone(Level level, Logger logger, Class<?> c, String message, Integer elements)
//	{
//		if(isActive())
//		{
//			if(level.equals(JeeslLogger.Level.TRACE) && logger.isTraceEnabled()) {logger.trace(this.milestone(c, message, elements));}
//			else if(level.equals(JeeslLogger.Level.DEBUG) && logger.isDebugEnabled()) {logger.debug(this.milestone(c, message, elements));}
//			else if(level.equals(JeeslLogger.Level.INFO) && logger.isInfoEnabled()) {logger.info(this.milestone(c, message, elements));}
//			else if(level.equals(JeeslLogger.Level.WARN) && logger.isWarnEnabled()) {logger.warn(this.milestone(c, message, elements));}
//			else if(level.equals(JeeslLogger.Level.ERROR) && logger.isErrorEnabled()) {logger.error(this.milestone(c, message, elements));}
//		}
//		else {logger.warn(msgNotActive);}
//	}
	
	public <E extends Enum<E>> String loopStart(E code) {return loopStart(code.toString());}
	private String loopStart(String loopCode)
	{
		if(isActive())
		{
			if(!loops.containsKey(loopCode))
			{
				loops.put(loopCode,fbLog.ejbLoop().build(log,loopCode));
			}
			mapLoopInstant.put(loopCode,Instant.now());
		}
		
		return "";
	}
	public <E extends Enum<E>> String loopEnd(E code) {return loopEnd(code,null);}
	public <E extends Enum<E>> String loopEnd(E code, Integer elements)
	{
		if(isActive())
		{
			if(!loops.containsKey(code.toString())) {logger.warn("Loop not started"); return "Loop not started";}
			else
			{
				LOOP loop = loops.get(code.toString());
				loop.setCounter(loop.getCounter()+1);
				loop.setElements(NullCalculator.add(loop.getElements(),elements));
				
				Instant timeBefore = mapLoopInstant.get(code.toString());
				Instant timeNow = Instant.now();
				long duration = ChronoUnit.MILLIS.between(timeBefore,timeNow);
				loop.setMilliTotal(loop.getMilliTotal()+duration);
				
				StringBuilder sb = new StringBuilder();
				sb.append("Loop");
				sb.append(" ").append(loop.getCode());
				if(elements!=null) {sb.append(" ").append(elements);}
				sb.append(" in ").append(duration);
				
				return sb.toString();
			}
		}
		else {return msgNotActive;}
	}
	
	public void count(String code)
	{
		mapCount.putIfAbsent(code,0);
		mapCount.put(code,1+mapCount.get(code));
	}
	
	@Override public void ofxMilestones(OutputStream os)
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		 
		   
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
			cell[0] = dtf.format(ejb.getRecord());
			
			cell[1] = Long.valueOf(ejb.getMilliTotal()).toString();
			cell[2] = Long.valueOf(ejb.getMilliStep()).toString();
			if(ejb.getElements()!=null) {cell[3] = ejb.getElements().toString();}else {cell[3]="";}
			cell[4] = Long.valueOf(ejb.getMilliRelative()).toString();
			
			cell[5] = ejb.getName();
			cell[6] = ejb.getMessage();
			data.add(cell);
		}
		
		OfxTextRenderer.silent(XmlTableFactory.build(c.getSimpleName(),header,data),os);
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
		
		OfxTextRenderer.silent(XmlTableFactory.build(c.getSimpleName(),header,data),os);
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
		
		OfxTextRenderer.silent(XmlTableFactory.build(c.getSimpleName(),header,data),os);
	}
}