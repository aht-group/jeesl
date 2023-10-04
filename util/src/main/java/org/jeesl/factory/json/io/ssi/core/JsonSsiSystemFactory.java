package org.jeesl.factory.json.io.ssi.core;

import java.util.ArrayList;
import java.util.Objects;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;

public class JsonSsiSystemFactory<SYSTEM extends JeeslIoSsiSystem<?,?>>
{
	private JsonSsiSystem q;
	private JsonSsiSystem json;
	
	public static <SYSTEM extends JeeslIoSsiSystem<?,?>> JsonSsiSystemFactory<SYSTEM> instance() {return new JsonSsiSystemFactory<SYSTEM>(null);}
	
	public JsonSsiSystemFactory<SYSTEM> fluent() {json = new JsonSsiSystem(); return this;}
	public JsonSsiSystemFactory<SYSTEM> credentials() {json.setCredentials(new ArrayList<>()); return this;}
	public JsonSsiSystemFactory<SYSTEM> code(String code) {json.setCode(code); return this;}
	public JsonSsiSystem json() {return json;}
	
	public JsonSsiSystemFactory(JsonSsiSystem q)
	{
		this.q=q;
	}
	
	public static JsonSsiSystem build() {return new JsonSsiSystem();}
	public static JsonSsiSystem build(String code)
	{
		JsonSsiSystem json = JsonSsiSystemFactory.build();
		json.setCode(code);
		return json;
	}
	
	public JsonSsiSystem build(SYSTEM ejb)
	{
		JsonSsiSystem json = JsonSsiSystemFactory.build();
		
		if(Objects.nonNull(q.getCode())) {json.setCode(ejb.getCode());}
		
		return json;
	}
}