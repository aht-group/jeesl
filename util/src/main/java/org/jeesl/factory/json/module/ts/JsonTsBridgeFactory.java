package org.jeesl.factory.json.module.ts;

import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.model.json.module.ts.JsonTsBridge;
import org.jeesl.model.json.system.status.JsonEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTsBridgeFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonTsBridgeFactory.class);
	
	public static JsonTsBridge build()
	{
		return new JsonTsBridge();
	}
	
	public static JsonTsBridge build(Class<?> c, String code)
	{
		JsonTsBridge bridge = build();
		
		JsonEntity entity = new JsonEntity();
		entity.setCode(c.getName());
		
		bridge.setEntity(entity);
		bridge.setCode(code);
		
		return bridge;
	}
}