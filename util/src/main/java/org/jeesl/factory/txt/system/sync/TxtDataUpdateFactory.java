package org.jeesl.factory.txt.system.sync;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtDataUpdateFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtDataUpdateFactory.class);
		
	public static String debug(DataUpdate du)
	{
		StringBuffer sb = new StringBuffer();
		
		if(Objects.nonNull(du.getType()))
		{
			if(Objects.nonNull(du.getType().getLabel())) {sb.append(du.getType().getLabel());}
			if(Objects.nonNull(du.getType().getCode()))
			{
				try
				{
					Class<?> c;
					c = Class.forName(du.getType().getCode());
					sb.append(" ").append(c.getSimpleName());
				}
				catch (ClassNotFoundException e) {}
			}
		}
		
		if(Objects.nonNull(du.getResult()) && Objects.nonNull(du.getResult().getStatus()) && Objects.nonNull(du.getResult().getStatus().getCode()))
		{
			sb.append(" [");
			sb.append(du.getResult().getStatus().getCode());
			sb.append("]");
		}
		
		if(Objects.nonNull(du.getResult()) && Objects.nonNull(du.getResult().getSuccess()) && Objects.nonNull(du.getResult().getTotal()) && du.getResult().getTotal()>0)
		{
			sb.append(" ").append(du.getResult().getSuccess());
			sb.append("/"+du.getResult().getTotal());
			if(Objects.nonNull(du.getResult().getSkip())) {sb.append(" (skipped:").append(du.getResult().getSkip()).append(")");}
		}
		
		return sb.toString();
	}
}