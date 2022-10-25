package org.jeesl.factory.json.extern.aceld;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.model.json.ssi.acled.JsonAcledContainer;
import org.jeesl.model.json.ssi.acled.JsonAcledData;
import org.jeesl.model.json.ssi.acled.JsonAcledResponse;
import org.jeesl.model.json.ssi.acled.JsonAcledSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSourceFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonSourceFactory.class);

	public static JsonAcledSource build() {return new JsonAcledSource();}
	
	public static JsonAcledContainer build(JsonAcledResponse response)
    {
		JsonAcledContainer container = JsonAcledContainerFactory.build();
		container.setSources(new ArrayList<>());
    	Set<String> set = new HashSet<>();
		
		for(JsonAcledData data : response.getData())
		{
			List<JsonAcledSource> jsons = JsonSourceFactory.build(data);
			for(JsonAcledSource json : jsons)
			{
				String ssiCode = JsonSourceFactory.toSsiCode(json);
				if(!set.contains(ssiCode))
				{
					container.getSources().add(json);
					set.add(ssiCode);
				}
			}
		}
    	return container;
    }
	
    public static List<JsonAcledSource> build(JsonAcledData data)
    {
    	List<JsonAcledSource> list = new ArrayList<>();
    	if(data.getSource()!=null)
    	{
    		if(!data.getSource().contains(";")) {list.add(JsonSourceFactory.build(data.getSource()));}
    		else
    		{
    			String[] x = data.getSource().split(";");
    			for(String s : x) {list.add(JsonSourceFactory.build(s));}
    		}
    	}
    	return list;
    }
	
    public static JsonAcledSource build(String name)
    {
    	JsonAcledSource json = build();
    	json.setName(name.trim());
    	return json;
    }
    
    public static String toSsiCode(JsonAcledSource json)
    {
    	return json.getName();
    }
}