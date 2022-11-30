package org.jeesl.factory.json.io.ssi.domain.ssi.inform;

import org.jeesl.model.json.ssi.inform.JsonInformRating;

public class JsonInformRatingFactory
{
	public static JsonInformRating build() {return new JsonInformRating();}
	
	public static String toSsiCode(JsonInformRating json)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(json.getRisk().getYear());
		sb.append(";").append(json.getRisk().getLocationArea());
		sb.append(";").append(json.getCategory().getCode());
		
		return sb.toString();
	}
}