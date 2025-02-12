package net.sf.ahtutils.controller.factory.r;

import java.io.Serializable;

import net.sf.ahtutils.controller.interfaces.r.RengineCommand;
import net.sf.ahtutils.r.RScript;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RGenericCommandFactory implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(RScript.class);
	private static final long serialVersionUID = 1L;
	
	public static RengineCommand clearWorkspace()
	{
		return RCommandFactory.eval("rm(list = ls())");
	}
}