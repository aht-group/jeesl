package org.jeesl.interfaces.controller.handler.system.io;

import java.io.OutputStream;
import java.io.Serializable;

public interface JeeslLogger extends Serializable
{
	String start(String log);
	String milestone(String milestone, String message, Integer elements);
	
	
	<E extends Enum<E>> String loopStart(E code);
	<E extends Enum<E>> String loopEnd(E code, Integer elements);
	
	void ofxMilestones(OutputStream os);
	void ofxLoops(OutputStream os);
}