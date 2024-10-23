package org.jeesl.factory.json.io.fr;

import org.jeesl.model.json.io.fr.JsonFrContainer;

public class JsonFrContainerFactory
{
	public static JsonFrContainer build(long id)
	{
		JsonFrContainer json = new JsonFrContainer();
		json.setId(id);
		return json;
	}
}