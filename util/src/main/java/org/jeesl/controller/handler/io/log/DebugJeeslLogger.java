package org.jeesl.controller.handler.io.log;

import java.io.OutputStream;
import java.util.Objects;

import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugJeeslLogger implements JeeslLogger
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(DebugJeeslLogger.class);
	

	public static JeeslLogger instance(Class<?> c) {return new DebugJeeslLogger(c);}
	private DebugJeeslLogger(Class<?> c)
	{
		
	}
	
	@Override public void reset() {}
	@Override public boolean isActive() {return true;}
	@Override public boolean isNotActive() {return false;}
	
	@Override public String start(String log) {return log;}
	@Override public String start(String log, String message) {return log+" - "+message;}

	@Override public String milestone(Class<?> c) {return milestone(c.getSimpleName(),null,null);}
	@Override public String milestone(String milestone) {return milestone;}
	
	@Override public String milestone(Class<?> c, String message){return milestone(c.getSimpleName(),message,null);}
	@Override public String milestone(String milestone, String message) {return milestone;}
	
	@Override public String milestone(Class<?> c, String message, Integer elements) {return milestone(c.getSimpleName(), message, elements);}
	@Override public String milestone(String milestone, String message, Integer elements)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(milestone);
		sb.append(" ").append(Objects.nonNull(message) ? message : "-");
		sb.append(" ").append(Objects.nonNull(elements) ? elements : "-");
		return sb.toString();
	}
//	@Override public void milestone(Level level, Logger logger, Class<?> c, String message, Integer elements) {}
	
	@Override public <E extends Enum<E>> String loopStart(E code) {return code.toString();}
	@Override public <E extends Enum<E>> String loopEnd(E code) {return code.toString();}
	@Override public <E extends Enum<E>> String loopEnd(E code, Integer elements) {return code.toString();}
	
	@Override public void ofxMilestones(OutputStream os) {}

	@Override public void ofxLoops(OutputStream os) {}
	
}