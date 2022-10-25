package org.jeesl.factory.json.extern.aceld;

import java.util.ArrayList;

import org.jeesl.model.json.ssi.acled.JsonAcledContainer;
import org.jeesl.model.json.ssi.acled.JsonAcledData;
import org.jeesl.model.json.ssi.acled.JsonAcledIncident;
import org.jeesl.model.json.ssi.acled.JsonAcledResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonIncidentFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonIncidentFactory.class);
	
	public static JsonAcledIncident build() {return new JsonAcledIncident();}
	
	public static JsonAcledContainer build(JsonAcledResponse response)
    {
		JsonAcledContainer container = JsonAcledContainerFactory.build();
		container.setIncidents(new ArrayList<>());
		
		for(JsonAcledData data : response.getData())
		{
			container.getIncidents().add(JsonIncidentFactory.build(data));
		}
    	return container;
    }
	
    public static JsonAcledIncident build(JsonAcledData data)
    {
    	JsonAcledIncident json = build();
    	json.setId(data.getId());
    	json.setMainType(data.getMainType());
    	json.setSubType(data.getSubType());
    	json.setLocation(data.getLocation());
    	json.setDescription(data.getNotes());
    	json.setDate(data.getDate());
    	json.setLatitude(data.getLatitude());
    	json.setLongitude(data.getLongitude());
    	
    	if(data.getActor1()!=null && data.getActor1().trim().length()>1) {json.setActor1(JsonActorFactory.build(data.getActor1().trim()));}
    	if(data.getActor2()!=null && data.getActor2().trim().length()>1) {json.setActor2(JsonActorFactory.build(data.getActor2().trim()));}
    	
    	json.setSources(JsonSourceFactory.build(data));
    	
    	json.setCountry(JsonCountryFactory.build(data));
    	json.setAdmin1(JsonAdmin1Factory.build(data));
    	
    	return json;
    }
    
    public static String toSsiCode(JsonAcledIncident json)
    {
    	return json.getId().toString();
    }
}