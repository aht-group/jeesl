package org.jeesl.factory.json.io.ssi.core;

import java.util.Objects;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.model.json.io.ssi.core.JsonSsiHost;

public class JsonSsiHostFactory<HOST extends JeeslIoSsiHost<?,?,?>>
{
	private JsonSsiHost q;
	
	public JsonSsiHostFactory(JsonSsiHost q)
	{
		this.q=q;
	}
	
	public static JsonSsiHost build() {return new JsonSsiHost();}
	public static JsonSsiHost build(String code)
	{
		JsonSsiHost json = JsonSsiHostFactory.build();
		json.setCode(code);
		return json;
	}
	
	public JsonSsiHost build(HOST ejb)
	{
		JsonSsiHost json = JsonSsiHostFactory.build();
		
		if(Objects.nonNull(q.getCode())) {json.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getIp())) {json.setIp(ejb.getIpAddr());}
		
		return json;
	}
}