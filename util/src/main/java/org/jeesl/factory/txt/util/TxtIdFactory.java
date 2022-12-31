package org.jeesl.factory.txt.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public class TxtIdFactory
{
	public static <T extends EjbWithId> String ids(List<T> ids)
	{
		List<String> list = new ArrayList<String>();
		for(T id : ids) {list.add(Long.valueOf(id.getId()).toString());}
		return StringUtils.join(list, ", ");
	}
}