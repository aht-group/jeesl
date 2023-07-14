package org.jeesl.factory.json.io.ssi.core;

import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;

public class JsonSsiSystemFactory
{
	public static JsonSsiSystem build() {return new JsonSsiSystem();}
	public static JsonSsiSystem build(String code)
	{
		JsonSsiSystem json = JsonSsiSystemFactory.build();
		json.setCode(code);
		return json;
	}
}