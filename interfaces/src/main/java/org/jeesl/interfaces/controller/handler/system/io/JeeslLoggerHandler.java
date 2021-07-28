package org.jeesl.interfaces.controller.handler.system.io;

import java.io.OutputStream;
import java.io.Serializable;

public interface JeeslLoggerHandler extends Serializable
{
	String start(String log);
	String milestone(String milestone, String message, Integer elements);
	
	void ofxMilestones(OutputStream os);
}