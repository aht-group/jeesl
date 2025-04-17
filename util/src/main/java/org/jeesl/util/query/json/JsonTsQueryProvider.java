package org.jeesl.util.query.json;

import java.time.LocalDateTime;

import org.jeesl.model.json.module.ts.JsonTsData;
import org.jeesl.model.json.module.ts.JsonTsMultipoint;
import org.jeesl.model.json.module.ts.JsonTsPoint;
import org.jeesl.model.json.module.ts.JsonTsScope;

public class JsonTsQueryProvider
{
	public static JsonTsScope scope()
	{				
		JsonTsScope json = new JsonTsScope();
		json.setCode("");
		json.setType(JsonStatusQueryProvider.typeCode());
		return json;
	}
	
	public static JsonTsMultipoint multipointCode()
	{				
		JsonTsMultipoint json = new JsonTsMultipoint();
		json.setCode("");
		return json;
	}
	
	public static JsonTsData data()
	{				
		JsonTsData json = new JsonTsData();
		json.setLocalDateTime(LocalDateTime.now());
		json.setValue(0d);
		return json;
	}
	
	public static JsonTsPoint point()
	{				
		JsonTsPoint json = new JsonTsPoint();
		json.setMp(multipointCode());
		json.setValue(0d);
		
		return json;
	}
}