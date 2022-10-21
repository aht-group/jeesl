package org.jeesl.factory.json.extern.aceld;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.model.json.ssi.acled.JsonAcledActor;
import org.jeesl.model.json.ssi.acled.JsonAcledContainer;
import org.jeesl.model.json.ssi.acled.JsonAcledData;
import org.jeesl.model.json.ssi.acled.JsonAcledResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonActorFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonActorFactory.class);

	public static JsonAcledActor build() {return new JsonAcledActor();}
	
	public static JsonAcledContainer build(JsonAcledResponse response)
    {
		JsonAcledContainer container = JsonAcledContainerFactory.build();
		container.setActors(new ArrayList<>());
    	Set<String> set = new HashSet<>();
		
		for(JsonAcledData data : response.getData())
		{
			List<JsonAcledActor> jsons = JsonActorFactory.build(data);
			for(JsonAcledActor json : jsons)
			{
				String ssiCode = JsonActorFactory.toSsiCode(json);
				if(!set.contains(ssiCode))
				{
					container.getActors().add(json);
					set.add(ssiCode);
				}
			}
		}
    	return container;
    }
	
    private static List<JsonAcledActor> build(JsonAcledData data)
    {
    	List<JsonAcledActor> list = new ArrayList<>(); 
    	if(data.getActor1()!=null && !data.getActor1().trim().isEmpty()) {list.add(JsonActorFactory.build(data.getActor1()));}
    	if(data.getActor1()!=null && !data.getActor2().trim().isEmpty()) {list.add(JsonActorFactory.build(data.getActor2()));}
    	return list;
    }
    
    public static JsonAcledActor build(String name)
    {
    	JsonAcledActor json = build();
    	json.setName(name);
    	return json;
    }
    
    public static String toSsiCode(JsonAcledActor json)
    {
    	return json.getName();
    }
}