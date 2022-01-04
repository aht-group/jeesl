package org.jeesl.util.query.json;

import java.util.ArrayList;
import java.util.Date;

import org.jeesl.model.json.module.attribute.JsonAttributeContainer;
import org.jeesl.model.json.module.attribute.JsonAttributeCriteria;
import org.jeesl.model.json.module.attribute.JsonAttributeData;
import org.jeesl.model.json.module.attribute.JsonAttributeItem;
import org.jeesl.model.json.module.attribute.JsonAttributeOption;
import org.jeesl.model.json.module.attribute.JsonAttributeSet;
import org.jeesl.model.json.module.attribute.JsonAttributeType;
import org.jeesl.model.json.system.status.JsonType;

public class JsonAttributeQueryProvider
{
	public static JsonAttributeSet set()
	{				
		JsonAttributeSet json = new JsonAttributeSet();
		json.setId(0l);
		json.setItems(new ArrayList<>());
		json.getItems().add(item());
		return json;
	}
	
	private static JsonAttributeItem item()
	{
		JsonAttributeItem json = new JsonAttributeItem();
		json.setId(0l);
		json.setPosition(0);
		json.setVisible(true);
		json.setCriteria(criteria());
		return json;
	}

	private static JsonAttributeCriteria criteria()
	{
		JsonAttributeCriteria json = new JsonAttributeCriteria();
		json.setId(0l);
		json.setCode("");
		json.setVisible(true);
		json.setAllowEmpty(true);
		json.setLabel("");
		json.setDescription("");
		json.setType(JsonAttributeQueryProvider.typeCode());
		
		json.setOptions(new ArrayList<>());
		json.getOptions().add(option());
		return json;
	}
	
	public static JsonAttributeType typeCode()
	{				
		JsonAttributeType xml = new JsonAttributeType();
		xml.setCode("");
		return xml;
	}
	
	public static JsonAttributeOption option()
	{				
		JsonAttributeOption json = new JsonAttributeOption();
		json.setId(0l);
		json.setCode("");
		json.setPosition(0);
		json.setLabel("");
		json.setDescription("");
		return json;
	}
	
	public static JsonAttributeContainer container()
	{				
		JsonAttributeContainer json = new JsonAttributeContainer();
		json.setId(0l);
		json.setDatas(new ArrayList<>());
		json.getDatas().add(data());
		return json;
	}
	
	public static JsonAttributeData data()
	{
		JsonAttributeData json = new JsonAttributeData();
		json.setId(0l);
		json.setValueDate(new Date());
		json.setValueString("");
		json.setValueBoolean(Boolean.TRUE);
		json.setValueInteger(0);
		
		JsonAttributeCriteria criteria = new JsonAttributeCriteria();
		criteria.setId(0l);
		criteria.setCode("");
		criteria.setLabel("");
		json.setCriteria(criteria);
		
		JsonAttributeOption option = new JsonAttributeOption();
		option.setId(0l);
		option.setCode("");
		option.setLabel("");
		json.setValueOption(option);
		
		json.setValueOptions(new ArrayList<>());
		json.getValueOptions().add(option);
		
		return json;
	}
}