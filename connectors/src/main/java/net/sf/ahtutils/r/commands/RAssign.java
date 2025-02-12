package net.sf.ahtutils.r.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.ahtutils.controller.interfaces.r.RengineCommand;
import net.sf.ahtutils.r.RScript;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RAssign implements Serializable,RengineCommand
{
	final static Logger logger = LoggerFactory.getLogger(RScript.class);
	private static final long serialVersionUID = 1L;
	
	private String var,value;
	
	public RAssign(String var)
	{
		this.var=var;
		this.value="NULL";
	}
	
	public RAssign(String var, String value)
	{
		this.var=var;
		this.value="\""+value+"\"";
	}
	
	public RAssign(String var, Integer value)
	{
		this.var=var;
		this.value=value.toString();
	}
	
	public RAssign(String var, Integer r1, Integer r2)
	{
		this.var=var;
		this.value=r1.toString()+":"+r2.toString();
	}
	
	public RAssign(String var, Boolean value)
	{
		this.var=var;
		this.value=value.toString().toUpperCase();
	}
	
	public void execute() throws Exception
	{
		RScript script = new RScript();
		script.addCommand(this);
		script.execute();
	}
	
	public List<String> render()
	{
		List<String> result = new ArrayList<String>();
		result.add(var +" <- " +value);
		return result;
	}
	
	public void debug()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Assign: ");
		sb.append(var);
		sb.append(" ");
		sb.append(value);
		logger.info(sb.toString());
	}
}