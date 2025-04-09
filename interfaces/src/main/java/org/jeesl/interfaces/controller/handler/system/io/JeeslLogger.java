package org.jeesl.interfaces.controller.handler.system.io;

import java.io.OutputStream;
import java.io.Serializable;

public interface JeeslLogger extends Serializable
{
//	public enum Level {TRACE, DEBUG, INFO, WARN, ERROR}
	
//	void x();
	void reset();
	boolean isActive();
	boolean isNotActive();
	
	String start(String log);
	String start(String log, String message);

	String milestone(Class<?> c);
	String milestone(String milestone);
	
	String milestone(Class<?> c, String message);
	String milestone(String milestone, String message);
	
//	void milestone(Level level, Logger logger, Class<?> c, String message, Integer elements);
	String milestone(Class<?> c, String message, Integer elements);
	String milestone(String milestone, String message, Integer elements);
	
	<E extends Enum<E>> String loopStart(E code);
	<E extends Enum<E>> String loopEnd(E code);
	<E extends Enum<E>> String loopEnd(E code, Integer elements);
	
	void ofxMilestones(OutputStream os);
	void ofxLoops(OutputStream os);
}