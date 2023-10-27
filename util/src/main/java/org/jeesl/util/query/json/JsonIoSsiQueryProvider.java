package org.jeesl.util.query.json;

import org.jeesl.factory.json.io.ssi.core.JsonSsiHostFactory;
import org.jeesl.model.json.io.ssi.core.JsonSsiHost;
import org.jeesl.model.json.io.ssi.core.JsonSsiNat;

public class JsonIoSsiQueryProvider
{
	public static JsonSsiNat nat()
	{				
		JsonSsiNat json = new JsonSsiNat();
		json.setId(Long.valueOf(0));
		
		json.setListenPort(0);
		json.setListenHost(JsonIoSsiQueryProvider.natHost());
		
		json.setDestinationPort(0);
		json.setDestinationHost(JsonIoSsiQueryProvider.natHost());
		
		return json;
	}
	
	private static JsonSsiHost natHost()
	{
		JsonSsiHost json = JsonSsiHostFactory.build();
		json.setIp("");
		return json;
	}
}