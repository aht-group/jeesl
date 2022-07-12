package org.jeesl.util.query.json;

import org.jeesl.model.json.system.status.JsonCategory;
import org.jeesl.model.json.system.status.JsonInterval;
import org.jeesl.model.json.system.status.JsonSector;
import org.jeesl.model.json.system.status.JsonStatus;
import org.jeesl.model.json.system.status.JsonType;
import org.jeesl.model.json.system.status.JsonWorkspace;

public class JsonStatusQueryProvider
{
	public static JsonStatus statusIdCodeLabel()
	{				
		JsonStatus json = new JsonStatus();
		json.setId(Long.valueOf(0));
		json.setCode("");
		json.setLabel("");
		return json;
	}
	
	public static JsonCategory categoryId()
	{				
		JsonCategory json = new JsonCategory();
		json.setId(Long.valueOf(0));
		return json;
	}
	
	public static JsonCategory categoryCodeLabel()
	{				
		JsonCategory json = new JsonCategory();
		json.setCode("");
		json.setLabel("");
		return json;
	}
	
	public static JsonCategory categoryIdCodeLabel()
	{				
		JsonCategory json = new JsonCategory();
		json.setId(Long.valueOf(0));
		json.setCode("");
		json.setLabel("");
		return json;
	}
	
	public static JsonStatus statusId()
	{				
		JsonStatus xml = new JsonStatus();
		xml.setId(Long.valueOf(0));
		return xml;
	}
	
	public static JsonStatus statusLabel()
	{				
		JsonStatus xml = new JsonStatus();
//		xml.setId(Long.valueOf(0));
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static JsonSector sectorId()
	{				
		JsonSector xml = new JsonSector();
		xml.setId(Long.valueOf(0));
		return xml;
	}
	
	public static JsonSector sectorCodeLabel()
	{				
		JsonSector xml = new JsonSector();
		xml.setCode("");
		xml.setLabel("");
		return xml;
	}
	
	public static JsonSector sectorIdCodeLabel()
	{				
		JsonSector json = new JsonSector();
		json.setId(Long.valueOf(0));
		json.setCode("");
		json.setLabel("");
		return json;
	}
	
	public static JsonType typeCode()
	{				
		JsonType xml = new JsonType();
		xml.setCode("");
		return xml;
	}
	
	public static JsonInterval intervalCode()
	{				
		JsonInterval xml = new JsonInterval();
		xml.setCode("");
		return xml;
	}
	
	public static JsonWorkspace workspaceCode()
	{				
		JsonWorkspace json = new JsonWorkspace();
		json.setCode("");
		return json;
	}
}