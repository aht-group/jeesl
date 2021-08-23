package org.jeesl.factory.txt.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;

public class TxtCodeFactory
{
	public static <C extends EjbWithCode> String codes(List<C> codes)
	{
		List<String> list = new ArrayList<String>();
		for(C code : codes) {list.add(code.getCode());}
		return StringUtils.join(list, ", ");
	}
}