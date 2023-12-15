package org.jeesl.util.filter.xml.io.locale;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.factory.txt.system.locale.TranslationFactory;
import org.jeesl.model.xml.io.locale.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlStatusFilter
{
	final static Logger logger = LoggerFactory.getLogger(TranslationFactory.class);
	
	public static List<Status> filterForParentCode(Status parent, List<Status> list)
	{
		List<Status> result = new ArrayList<Status>();
		for(Status s : list)
		{
			if(s.getParent().getCode().equals(parent.getCode())){result.add(s);}
		}
		return result;
	}
}