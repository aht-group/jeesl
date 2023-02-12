package org.jeesl.factory.json.system.io.label;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.model.json.system.io.revision.JsonRevision;

public class JsonRevisionFactory
{
	public static JsonRevision build(Object[] array)
	{
		long id = ((BigInteger)array[0]).longValue();
        long revision = ((BigInteger)array[1]).longValue();
        int type = ((BigInteger)array[1]).intValue();
        long user = ((BigInteger)array[3]).longValue();
        
		return build(revision,id,type,user);
	}
	
	public static JsonRevision build(long revision, long id)
	{
		JsonRevision json = new JsonRevision();
		json.setRevision(revision);
		json.setId(id);
		return json;
	}
	
	public static JsonRevision build(long revision, long id, int type, long userId)
	{
		JsonRevision json = build(revision,id);
		json.setType(type);
		json.setUserId(userId);
		return json;
	}
	
	public static List<Long> toIds(List<JsonRevision> list)
	{
		Set<Long> set = new HashSet<Long>();
		for(JsonRevision r : list){set.add(r.getId());}
		return new ArrayList<Long>(set);
	}
}