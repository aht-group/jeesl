package org.jeesl.factory.json.extern.aceld;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jeesl.model.json.ssi.acled.JsonAcledAdmin1;
import org.jeesl.model.json.ssi.acled.JsonAcledContainer;
import org.jeesl.model.json.ssi.acled.JsonAcledCountry;
import org.jeesl.model.json.ssi.acled.JsonAcledData;
import org.jeesl.model.json.ssi.acled.JsonAcledResponse;
import org.jeesl.model.json.ssi.inform.JsonInformCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAdmin1Factory
{
	final static Logger logger = LoggerFactory.getLogger(JsonAdmin1Factory.class);

	public static JsonAcledAdmin1 build() {return new JsonAcledAdmin1();}

	public static JsonAcledContainer build(JsonAcledResponse response,JsonAcledCountry jsonCountry)
    {
		JsonAcledContainer json = JsonAcledContainerFactory.build();
		json.setAdmin1(new ArrayList<>());
    	Set<String> set = new HashSet<>();

		for(JsonAcledData data : response.getData())
		{
			JsonAcledAdmin1 a1 = JsonAdmin1Factory.build(data,jsonCountry);
			String ssiCode = JsonAdmin1Factory.toSsiCode(a1);
			if(!set.contains(ssiCode))
			{
				json.getAdmin1().add(a1);
				set.add(ssiCode);
			}
		}
    	return json;
    }

    public static JsonAcledAdmin1 build(JsonAcledData data)
    {
    	JsonAcledAdmin1 json = build();
    	json.setCountry(JsonCountryFactory.build(data));
    	json.setName(data.getAdmin1());
    	return json;
    }

    public static JsonAcledAdmin1 build(JsonAcledData data, JsonAcledCountry country)
    {
    	JsonAcledAdmin1 json = build();
    	json.setCountry(country);
    	json.setName(data.getAdmin1());
    	return json;
    }

    public static String toSsiCode(JsonAcledAdmin1 json)
    {
    	return json.getCountry().getIso3()+";"+json.getName();
    }

    public static String toSsiCode(JsonInformCollection json)
    {
    	return json.getLocationCountry()+";"+json.getLocationArea();
    }
}