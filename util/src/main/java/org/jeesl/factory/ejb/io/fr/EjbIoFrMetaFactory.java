package org.jeesl.factory.ejb.io.fr;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Meta;

public class EjbIoFrMetaFactory<CONTAINER extends JeeslFileContainer<?,META>,
								META extends JeeslFileMeta<?,CONTAINER,TYPE,?>,
								TYPE extends JeeslFileType<?,?,TYPE,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoFrMetaFactory.class);
	
	private final Class<META> cMeta;
    
	public EjbIoFrMetaFactory(final Class<META> cMeta)
	{       
        this.cMeta = cMeta;
	}
	
	public META build(CONTAINER container, String name, long size, Date record)
	{
		return build(container, name, size, record, null);
	}
	
	public META build(CONTAINER container, String fileName, long size, Date record, List<META> list)
	{
		META ejb = null;
		try
		{
			 ejb = cMeta.newInstance();
			 ejb.setContainer(container);
			 ejb.setCode(UUID.randomUUID().toString());
			 ejb.setFileName(fileName);
			 ejb.setSize(size);
			 ejb.setRecord(record);
			 ejb.setPosition(0);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public Date toLastDate(List<META> metas)
	{
		if(metas==null || metas.isEmpty()) {return null;}
		Date last = new Date(0);
		for(META meta : metas)
		{
			if(meta.getRecord().after(last)) {last = meta.getRecord();}
		}
		return last;
	}
	
	public List<META> toImages(List<META> metas)
	{
		List<META> images = new ArrayList<>();
		for(META meta : metas)
		{
			if(meta.getType().getCode().equals(JeeslFileType.Code.jpg.toString())
					|| meta.getType().getCode().equals(JeeslFileType.Code.png.toString()))
			{
				images.add(meta);
			}
		}
		return images;
	}
	
	public Map<CONTAINER,List<META>> toMapMeta(List<META> metas)
	{
		Map<CONTAINER,List<META>> map = new HashMap<>();
		for(META meta : metas)
		{
			if(!map.containsKey(meta.getContainer())){map.put(meta.getContainer(),new ArrayList<>());}
			map.get(meta.getContainer()).add(meta);
		}
		return map;
	}
}