package org.jeesl.factory.json.io.ssi.domain.ssi.inform;

import org.jeesl.model.json.ssi.inform.JsonInformRisk;

public class JsonInformRatingFactory
{
	public static JsonInformRisk build() {return new JsonInformRisk();}
	
	public static String toSsiCode(JsonInformRisk json)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(json.getCollection().getYear());
		sb.append(";").append(json.getCollection().getLocationArea());
		sb.append(";").append(json.getCategory().getCode());
		
		return sb.toString();
	}
}