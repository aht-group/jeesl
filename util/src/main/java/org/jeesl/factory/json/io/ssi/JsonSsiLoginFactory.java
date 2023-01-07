package org.jeesl.factory.json.io.ssi;

import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;

public class JsonSsiLoginFactory
{
	public static JsonSsiUpdate build()
	{
		return new JsonSsiUpdate();
	}
}