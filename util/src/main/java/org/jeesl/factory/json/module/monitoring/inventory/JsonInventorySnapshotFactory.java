package org.jeesl.factory.json.module.monitoring.inventory;

import java.util.ArrayList;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.model.xml.tool.inventory.Obj;
import org.exlp.model.xml.tool.inventory.Objs;
import org.jeesl.model.json.module.monitoring.inventory.JsonInventorySnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonInventorySnapshotFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonInventorySnapshotFactory.class);
	
	public static JsonInventorySnapshot build(){return new JsonInventorySnapshot();}
	
	public static JsonInventorySnapshot build(Objs xml)
	{
		JsonInventorySnapshot json = JsonInventorySnapshotFactory.build();
		json.setAttributes(new ArrayList<>());
		
		if(ObjectUtils.isNotEmpty(xml.getObj()))
		{
			for(Obj obj : xml.getObj())
			{
				json.getAttributes().add(JsonInventoryAttributeFactory.build(obj));
			}
		}
		
		return json;
	}
}