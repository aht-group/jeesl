package org.jeesl.factory.json.module.attribute;

import java.util.ArrayList;

import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.json.module.attribute.JsonAttributeCriteria;
import org.jeesl.model.json.module.attribute.JsonAttributeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAttributeDataFactory<L extends JeeslLang, D extends JeeslDescription,
										R extends JeeslTenantRealm<L,D,R,?>,
										CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
										CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,TYPE,OPTION,SET>,
										TYPE extends JeeslAttributeType<L,D,TYPE,?>,
										OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
										SET extends JeeslAttributeSet<L,D,R,CAT,ITEM>,
										ITEM extends JeeslAttributeItem<CRITERIA,SET>,
										CONTAINER extends JeeslAttributeContainer<SET,DATA>,
										DATA extends JeeslAttributeData<CRITERIA,OPTION,CONTAINER>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonAttributeDataFactory.class);
	
	private final JsonAttributeData q;
	
	private JsonAttributeCriteriaFactory<L,D,R,CAT,CRITERIA,TYPE,OPTION,SET,ITEM> jfCriteria;
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
}