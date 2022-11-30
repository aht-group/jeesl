package org.jeesl.factory.json.io.ssi.domain.ssi.inform;

import org.jeesl.model.json.ssi.inform.JsonInformCollection;

public class JsonInformRiskFactory
{
	public static JsonInformCollection build()
	{
		JsonInformCollection json = new JsonInformCollection();
		
		return json;
	}
	
	public static JsonInformCollection build(String locationName, String locationCountry, String locationArea, Integer year)
	{
		JsonInformCollection json = build();
		json.setLocationArea(locationArea);
		json.setLocationCountry(locationCountry);
		json.setLocationName(locationName);
		json.setYear(year);
		return json;
	}
}