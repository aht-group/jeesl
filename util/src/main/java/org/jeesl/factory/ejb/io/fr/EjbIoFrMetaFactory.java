package org.jeesl.factory.ejb.io.fr;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public META build(CONTAINER container, String name, long size, LocalDateTime record)
	{
		return build(container, name, size, record, null);
	}

	public META build(CONTAINER container, String fileName, long size, LocalDateTime record, List<META> list)
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

	public LocalDateTime toLastDate(List<META> metas)
	{
		if(metas==null || metas.isEmpty()) {return null;}
		LocalDateTime last = DateUtil.toLocalDateTime(new Date(0));
		for(META meta : metas)
		{
			if(meta.getRecord().isAfter(last)) {last = meta.getRecord();}
		}
		return last;
	}
	
	public META toLast(List<META> metas)
	{
		if(ObjectUtils.isEmpty(metas)) {return null;}
		META last = null;
		
		for(META meta : metas)
		{
			if(Objects.isNull(last)) {last = meta;}
			else if(meta.getRecord().isAfter(last.getRecord())) {last = meta;}
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
	public List<CONTAINER> toContainers(List<META> metas)
	{
		Set<CONTAINER> set = new HashSet<>();
		for(META meta : metas)
		{
			set.add(meta.getContainer());
		}
		return new ArrayList<>(set);
	}
	public Map<Long,List<META>> toMapIdMeta(List<META> metas)
	{
		Map<Long,List<META>> map = new HashMap<>();
		for(META meta : metas)
		{
			if(!map.containsKey(meta.getContainer().getId())){map.put(meta.getContainer().getId(),new ArrayList<>());}
			map.get(meta.getContainer().getId()).add(meta);
		}
		return map;
	}
}