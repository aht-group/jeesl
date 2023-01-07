package org.jeesl.factory.json.io.ssi;

import org.jeesl.model.json.io.ssi.core.JsonSsiVersion;

public class JsonSsiVersionFactory
{
	public static JsonSsiVersion build()
	{
		return new JsonSsiVersion();
	}
	
	public static JsonSsiVersion build(String code, String fqcn)
	{
		JsonSsiVersion json = build();
		json.setCode(code);
		json.setFqcn(fqcn);
		return json;
	}
}