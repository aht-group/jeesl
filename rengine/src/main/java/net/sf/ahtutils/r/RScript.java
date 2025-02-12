package net.sf.ahtutils.r;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.ahtutils.controller.interfaces.r.RengineCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rcaller.RCaller;
import rcaller.RCode;

public class RScript implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(RScript.class);
	private static final long serialVersionUID = 1L;
	
	private List<RengineCommand> commands;
	
	private String exeRscript;
	
	public RScript() {this("/usr/bin/Rscript");}
	public RScript(String exeRscript)
	{
		this.exeRscript=exeRscript;
		commands = new ArrayList<RengineCommand>();
	}

	public void addCommand(RengineCommand command)
	{
		commands.add(command);
	}
		
	public void execute()
	{
		RCaller rc = new RCaller();
		rc.setRscriptExecutable(exeRscript);
		rc.cleanRCode();
		RCode code = new RCode();
		for(RengineCommand cmd : commands)
		{
//			logger.debug("Adding code for execution in R: ");
			for(String s : cmd.render())
			{
				code.addRCode(s +"\n");
			}
		}
		rc.setRCode(code);
		rc.runOnly();
	}
	
	public void debug()
	{
		logger.debug("Debugging "+this.getClass().getSimpleName());
		logger.debug("********************************************");
		for(RengineCommand cmd : commands)
		{
			for(String s : cmd.render())
			{
				System.out.println(s);
			}
		}
		logger.debug("********************************************");
	}
}