package org.jeesl.factory.json.module.monitoring.inventory;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.model.xml.tool.inventory.Obj;
import org.exlp.model.xml.tool.inventory.S;
import org.jeesl.factory.json.system.status.JsonCategoryFactory;
import org.jeesl.factory.json.system.status.JsonTypeFactory;
import org.jeesl.model.json.module.monitoring.inventory.JsonInventoryAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonInventoryAttributeFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonInventoryAttributeFactory.class);
	
	public static JsonInventoryAttribute build(){return new JsonInventoryAttribute();}
	
	public static JsonInventoryAttribute build(Obj xml)
	{
		JsonInventoryAttribute json = JsonInventoryAttributeFactory.build();
		json.setCategory(JsonCategoryFactory.build(null,null));
		json.setType(JsonTypeFactory.build(null,null));
		
		if(Objects.nonNull(xml.getMS()) && ObjectUtils.isNotEmpty(xml.getMS().getS()))
		{
			for(S s : xml.getMS().getS())
			{
				if(s.getN().equals("CategoryID")) {json.getCategory().setCode(s.getValue());}
				if(s.getN().equals("CategoryName")) {json.getCategory().setLabel(s.getValue());}
				
				if(s.getN().equals("ItemID")) {json.getType().setCode(s.getValue());}
				if(s.getN().equals("ItemName")) {json.getType().setLabel(s.getValue());}
			}
		}
		
		return json;
	}
}