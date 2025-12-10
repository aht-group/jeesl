package org.jeesl.factory.json.io.db;

import java.io.IOException;

import org.jeesl.model.json.io.db.tag.JsonDbTag;

public class JsonDbTagFactory
{
	public static JsonDbTag build(String code)
	{
		JsonDbTag json = new JsonDbTag();
		json.setCode(code);
		return json;
	}
	
}