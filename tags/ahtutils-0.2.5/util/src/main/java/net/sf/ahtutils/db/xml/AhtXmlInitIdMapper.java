package net.sf.ahtutils.db.xml;

import java.util.Hashtable;
import java.util.Map;

public class AhtXmlInitIdMapper
{
	private Map<String,Map<Long,Long>> mIdMapper;
	
	public AhtXmlInitIdMapper()
	{
		mIdMapper = new Hashtable<String,Map<Long,Long>>();
	}
	
}
