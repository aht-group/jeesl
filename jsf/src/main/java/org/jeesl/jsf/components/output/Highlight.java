package org.jeesl.jsf.components.output;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent(value="org.jeesl.jsf.components.output.Highlight")
public class Highlight extends UINamingContainer
{
	final static Logger logger = LoggerFactory.getLogger(Highlight.class);
	
	private static final String id ="//jeesl.highlight:"; 
	
	public String between(String input, String between, boolean relativeTab)
	{
		InputStream is = this.getClass().getResourceAsStream(input);
		
		Integer from = null;
		Integer to = null;
		List<String> list;
		
		if(is!=null)
		{
			
				list = IOUtils.readLines(is, "UTF-8");
				for(int i=0;i<list.size();i++)
				{
					String s = list.get(i).trim();
					if(s.startsWith(id))
					{
						String[] token = s.split(":");
						if(token[1].equals(between))
						{
							if(from==null){from=i+2;}
							else{to=i;}
						}
					}
				}
			
			
		}
		else {return resourceNotFound(input);}
		
		if(from==null || to==null)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("Not all markers");
			sb.append(" '").append(between).append("'");
			sb.append(" found in: ").append(input).append(IOUtils.LINE_SEPARATOR_UNIX);
			sb.append("from: ").append(from).append(IOUtils.LINE_SEPARATOR_UNIX);
			sb.append("to: ").append(to);
			return sb.toString();
		}
		else
		{
			return build(list,from,to);
		}
	}
	
	public String text(String input, int from, int to, boolean relativeTab)
	{
		logger.trace("Looking for Resource "+input);
		logger.trace("from:"+from+" "+"to:"+to);
		InputStream is = this.getClass().getResourceAsStream(input);
		
		if(is!=null)
		{
			StringWriter writer = new StringWriter();
			
			if(from!=0 || to !=-1)
			{
				
					List<String> list = IOUtils.readLines(is, "UTF-8");
					writer.write(build(list, from, to));
				
			}
			else
			{			
				try {IOUtils.copy(is, writer, "UTF-8");}
				catch (IOException e){logger.error(e.getMessage());e.printStackTrace();}
			}		
			String s = writer.toString();
			try {writer.close();} catch (IOException e) {e.printStackTrace();}
			return s;
		}
		else
		{
			logger.warn("Resource not found "+input);
			return resourceNotFound(input);
		}
	}
	
	private String build(List<String> list, int from, int to)
	{
		StringWriter writer = new StringWriter();
		for(int i=1;i<=list.size();i++)
		{
			if(i>=from && i<=to)
			{
				String s = list.get(i-1);
				writer.write(s+IOUtils.LINE_SEPARATOR_UNIX);
			}
		}
		return writer.toString();
	}
	
	public String tab(int tab)
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<tab;i++)
		{
			sb.append(" ");
		}
		return sb.toString();
	}
	
	private String resourceNotFound(String input)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Resource not found: ").append(IOUtils.LINE_SEPARATOR_UNIX);
		sb.append(input).append(IOUtils.LINE_SEPARATOR_UNIX);
		sb.append(" must be available in the classpath");
		return sb.toString();
	}
}