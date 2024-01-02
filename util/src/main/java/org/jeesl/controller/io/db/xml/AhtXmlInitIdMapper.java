package org.jeesl.controller.io.db.xml;

import java.util.Hashtable;
import java.util.Map;

@Deprecated
public class AhtXmlInitIdMapper
{
	private Map<String,Map<Long,Long>> mIdMapper;
	
	public AhtXmlInitIdMapper()
	{
		mIdMapper = new Hashtable<String,Map<Long,Long>>();
	}
	
}
