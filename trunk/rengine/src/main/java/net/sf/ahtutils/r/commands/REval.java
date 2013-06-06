package net.sf.ahtutils.r.commands;

import java.io.Serializable;

import net.sf.ahtutils.controller.interfaces.r.RengineCommand;
import net.sf.ahtutils.r.RScript;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class REval implements Serializable,RengineCommand
{
	final static Logger logger = LoggerFactory.getLogger(RScript.class);
	private static final long serialVersionUID = 1L;
	
	private String eval;
	
	public REval(String eval)
	{
		this.eval=eval;
	}
	
	public void execute() throws Exception
	{
		RScript script = new RScript();
		script.addCommand(this);
		script.execute();
	}
	
	public String renderR()
	{
		return (eval);
	}
	
	public void debug()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Eval: ");
		sb.append(eval);
		logger.info(sb.toString());
	}
}
