package org.jeesl.factory.txt.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public class TxtIdFactory
{
	public static <T extends EjbWithId> String ids(List<T> ids)
	{
		List<String> list = new ArrayList<String>();
		for(T id : ids) {list.add(Long.valueOf(id.getId()).toString());}
		return StringUtils.join(list, ", ");
	}
	
	public static <T extends EjbWithId> String commaList(T[] ids)
	{
		List<String> list = new ArrayList<String>();
		for(T id : ids) {list.add(Long.valueOf(id.getId()).toString());}
		return StringUtils.join(list, ",");
	}
	public static <T extends EjbWithId> String idList(List<T> ids)
	{
		List<String> list = new ArrayList<String>();
		for(T id : ids) {list.add(Long.valueOf(id.getId()).toString());}
		return "["+StringUtils.join(list, ",")+"]";
	}
	
	public static <T extends EjbWithId> String lazy(Class<T> c, List<T> list, long total)
	{
		return TxtIdFactory.lazy(c, list, total, null);
	}
	public static <T extends EjbWithId> String lazy(Class<T> c, List<T> list, long total, ProcessingTimeTracker ptt)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(c.getSimpleName());
		sb.append(" ").append(list.size()).append("/").append(total);
		if(Objects.nonNull(ptt)) {sb.append(" ").append(ptt.toTotalTime());}
		return sb.toString();
	}
}