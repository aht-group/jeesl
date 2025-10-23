package org.jeesl.factory.json.extern.aceld;

import java.util.ArrayList;
import java.util.Objects;

import org.jeesl.model.json.ssi.acled.JsonAcledContainer;
import org.jeesl.model.json.ssi.acled.JsonAcledCountry;
import org.jeesl.model.json.ssi.acled.JsonAcledData;
import org.jeesl.model.json.ssi.acled.JsonAcledIncident;
import org.jeesl.model.json.ssi.acled.JsonAcledResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonIncidentFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonIncidentFactory.class);

	public static JsonAcledIncident build() {return new JsonAcledIncident();}

	public static JsonAcledContainer build(JsonAcledResponse response, JsonAcledCountry jsonAcledCountry)
    {
		JsonAcledContainer container = JsonAcledContainerFactory.build();
		container.setIncidents(new ArrayList<>());

		for(JsonAcledData data : response.getData())
		{
			container.getIncidents().add(JsonIncidentFactory.build(data, jsonAcledCountry));
		}
    	return container;
    }

    public static JsonAcledIncident build(JsonAcledData data, JsonAcledCountry jsonAcledCountry)
    {
    	JsonAcledIncident json = build();
    	json.setId(Long.valueOf(Objects.hash(data.getEventId(),data.getCount(), data.getDate())));
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

    	json.setCountry(jsonAcledCountry);
    	json.setAdmin1(JsonAdmin1Factory.build(data, jsonAcledCountry));
    	json.setAdmin2(data.getAdmin2());
    	json.setAdmin3(data.getAdmin3());

    	return json;
    }

    public static String toSsiCode(JsonAcledIncident json)
    {
    	return json.getId().toString();
    }
}