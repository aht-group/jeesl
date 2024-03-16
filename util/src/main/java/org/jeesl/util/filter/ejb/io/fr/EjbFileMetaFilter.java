package org.jeesl.util.filter.ejb.io.fr;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbFileMetaFilter
{
	final static Logger logger = LoggerFactory.getLogger(EjbFileMetaFilter.class);
	
	public static <META extends JeeslFileMeta<?,?,?,?>> List<META> filterImages(List<META> metas)
	{	
		List<META> images = new ArrayList<META>();
				
		for(META meta : metas)
		{
			if(meta.getType()!=null && meta.getType().getCode()!=null)
			{
				if(meta.getType().getCode().equals(JeeslFileType.Code.jpg.toString())
						|| meta.getType().getCode().equals(JeeslFileType.Code.png.toString()))
				{
					images.add(meta);
				}
			}
		}
		return images;
	}
}