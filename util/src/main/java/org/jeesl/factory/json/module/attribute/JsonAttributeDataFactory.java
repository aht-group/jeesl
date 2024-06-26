package org.jeesl.factory.json.module.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.ListUtils;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.json.module.attribute.JsonAttributeContainer;
import org.jeesl.model.json.module.attribute.JsonAttributeCriteria;
import org.jeesl.model.json.module.attribute.JsonAttributeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAttributeDataFactory<L extends JeeslLang, D extends JeeslDescription,
										CAT extends JeeslAttributeCategory<L,D,?,CAT,?>,
										CRITERIA extends JeeslAttributeCriteria<L,D,?,CAT,TYPE,OPTION,?>,
										TYPE extends JeeslAttributeType<L,D,TYPE,?>,
										OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
										
										DATA extends JeeslAttributeData<CRITERIA,OPTION,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonAttributeDataFactory.class);
	
	private final JsonAttributeData q;
	
	private JsonAttributeCriteriaFactory<L,D,CAT,CRITERIA,TYPE,OPTION> jfCriteria;
	private JsonAttributeOptionFactory<L,D,OPTION> jfOption;
	private JsonAttributeOptionFactory<L,D,OPTION> jfOptions;

	public JsonAttributeDataFactory(String localeCode, JsonAttributeData q)
	{
		this.q=q;
		if(q.getCriteria()!=null) {jfCriteria = new JsonAttributeCriteriaFactory<>(localeCode,q.getCriteria());}
		if(q.getValueOption()!=null) {jfOption = new JsonAttributeOptionFactory<>(localeCode,q.getValueOption());}
		if(q.getValueOptions()!=null && !q.getValueOptions().isEmpty()) {jfOptions = new JsonAttributeOptionFactory<>(localeCode,q.getValueOptions().get(0));}
	}
	
	public static JsonAttributeData build(){return new JsonAttributeData();}
	public static JsonAttributeData build(JsonAttributeCriteria criteria)
	{
		JsonAttributeData json = build();
		json.setCriteria(criteria);
		return json;
	}
	
	public JsonAttributeData build(DATA data)
	{
		JsonAttributeData json = build();
		if(q.getId()!=null) {json.setId(data.getId());}
		if(q.getCriteria()!=null) {json.setCriteria(jfCriteria.build(data.getCriteria()));}
		
		if(q.getValueDate()!=null && data.getValueRecord()!=null) {json.setValueDate(data.getValueRecord());}
		if(q.getValueString()!=null && data.getValueString()!=null) {json.setValueString(data.getValueString());}
		if(q.getValueBoolean()!=null && data.getValueBoolean()!=null) {json.setValueBoolean(data.getValueBoolean());}
		if(q.getValueInteger()!=null && data.getValueInteger()!=null) {json.setValueInteger(data.getValueInteger());}
		if(q.getValueDouble()!=null && data.getValueDouble()!=null) {json.setValueDouble(data.getValueDouble());}
		
		if(q.getValueOption()!=null && data.getValueOption()!=null) {json.setValueOption(jfOption.build(data.getValueOption()));}
		
		if(q.getValueOptions()!=null && !q.getValueOptions().isEmpty() && data.getValueOptions()!=null && !data.getValueOptions().isEmpty())
		{
			json.setValueOptions(new ArrayList<>());
			for(OPTION o : data.getValueOptions())
			{
				json.getValueOptions().add(jfOptions.build(o));
			}
		}
		
		return json;
	}
	
	public static Map<Long,JsonAttributeData> toMapCriteriaId(JsonAttributeContainer container)
	{
		return JsonAttributeDataFactory.toMapCriteriaId(container.getDatas());
	}
	public static Map<Long,JsonAttributeData> toMapCriteriaId(List<JsonAttributeData> list)
	{
		Map<Long,JsonAttributeData> map = new HashMap<>();
		for(JsonAttributeData d : ListUtils.emptyIfNull(list))
		{
			if(Objects.nonNull(d.getCriteria()) && Objects.nonNull(d.getCriteria().getId()))
			{
				map.put(d.getCriteria().getId(),d);
			}
		}
		return map;
	}
}