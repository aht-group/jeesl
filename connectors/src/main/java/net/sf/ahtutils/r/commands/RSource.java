package net.sf.ahtutils.r.commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.ahtutils.controller.factory.r.RCommandFactory;
import net.sf.ahtutils.controller.interfaces.r.RengineCommand;
import net.sf.ahtutils.r.RScript;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSource implements Serializable,RengineCommand
{
	final static Logger logger = LoggerFactory.getLogger(RScript.class);
	private static final long serialVersionUID = 1L;
	
	private MultiResourceLoader mrl;
	private String source;
	private String encoding = "UTF-8";
	
	public RSource(String source)
	{
		this.source=source;
	}
	
	public RSource(String resource, MultiResourceLoader mrl)
	{
		this.source=resource;
		this.mrl=mrl;
	}
	
	public void execute() throws Exception
	{
		RScript script = new RScript();
		script.addCommand(this);
		script.execute();
	}
	
	public List<String> render()
	{
		if(mrl==null){return renderSource();}
		else {return renderResource();}
	}
	
	public List<String> renderSource()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("source(");
		sb.append("\"").append(source).append("\"");
		sb.append(", encoding=\""+encoding+"\"");
		sb.append(")");
		
		List<String> result = new ArrayList<String>();
		REval eval = RCommandFactory.eval(sb.toString());
		result.addAll(eval.render());
		return (result);
	}
	
	private List<String> renderResource()
	{
		List<String> result = new ArrayList<String>();
		InputStream is;
		try
		{
			is = mrl.searchIs(source);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while(null != (line = br.readLine()))
			{
				result.add(line);
			}
			br.close();
			isr.close();
			is.close();
		}
		catch (FileNotFoundException e) {}
		catch (IOException e) {e.printStackTrace();}
		
		return result;
	}
	
	public void debug()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Source: ");
		sb.append(source);
		logger.info(sb.toString());
	}
}
