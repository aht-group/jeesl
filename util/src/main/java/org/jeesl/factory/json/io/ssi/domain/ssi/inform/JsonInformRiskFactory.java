package org.jeesl.factory.json.io.ssi.domain.ssi.inform;

import org.jeesl.model.json.ssi.inform.JsonInformRisk;

public class JsonInformRiskFactory
{
	public static JsonInformRisk build()
	{
		JsonInformRisk json = new JsonInformRisk();
		
		return json;
	}
	
	public static JsonInformRisk build(String locationName, String locationCountry, String locationArea, Integer year)
	{
		JsonInformRisk json = build();
		json.setLocationArea(locationArea);
		json.setLocationCountry(locationCountry);
		json.setLocationName(locationName);
		json.setYear(year);
		return json;
	}
}