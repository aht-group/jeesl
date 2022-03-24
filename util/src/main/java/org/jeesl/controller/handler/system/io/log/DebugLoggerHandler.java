package org.jeesl.controller.handler.system.io.log;

import java.io.OutputStream;

import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugLoggerHandler implements JeeslLogger
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(DebugLoggerHandler.class);
	

	public static JeeslLogger instance(Class<?> c) {return new DebugLoggerHandler(c);}
	private DebugLoggerHandler(Class<?> c)
	{
		
	}
	
	@Override public String start(String log) {return log;}
	
	@Override public String milestone(String milestone) {return milestone;}
	@Override public String milestone(String milestone, String message) {return milestone;}
	@Override public String milestone(String milestone, String message, Integer elements) {return milestone;}
	
	@Override public <E extends Enum<E>> String loopStart(E code) {return code.toString();}
	@Override public <E extends Enum<E>> String loopEnd(E code, Integer elements) {return code.toString();}
	
	@Override public void ofxMilestones(OutputStream os) {}

	@Override public void ofxLoops(OutputStream os) {}
	
}