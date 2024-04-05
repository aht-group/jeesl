package org.jeesl.factory.json.module.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.json.module.attribute.JsonAttributeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAttributeCriteriaFactory<L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,?>,
									CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
									
									CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,TYPE,OPTION,?>,
									TYPE extends JeeslAttributeType<L,D,TYPE,?>,
									OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
									
									ITEM extends JeeslAttributeItem<CRITERIA,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonAttributeCriteriaFactory.class);
	
	private JeeslFacade facade;
	private IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,OPTION,?,ITEM,?,?> fbAttribute;
	
	private final String localeCode;
	private final JsonAttributeCriteria q;
	
	private JsonAttributeTypeFactory<L,D,TYPE> jfType;
	private JsonAttributeOptionFactory<L,D,OPTION> jfOption;
	
	public JsonAttributeCriteriaFactory(String localeCode, JsonAttributeCriteria q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(q.getType()!=null) {jfType = new JsonAttributeTypeFactory<>(localeCode,q.getType());}
		if(q.getOptions()!=null && !q.getOptions().isEmpty()) {jfOption = new JsonAttributeOptionFactory<>(localeCode,q.getOptions().get(0));}
	}
	
	public void lazy(JeeslFacade facade, IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,OPTION,?,ITEM,?,?> fbAttribute)
	{
		this.facade=facade;
		this.fbAttribute=fbAttribute;
	}
	
	public static JsonAttributeCriteria build(){return new JsonAttributeCriteria();}
	public static JsonAttributeCriteria build(long id){JsonAttributeCriteria json = build(); json.setId(id); return json;}
	
	public JsonAttributeCriteria build(CRITERIA criteria)
	{
		JsonAttributeCriteria json = build();
		if(q.getId()!=null) {json.setId(criteria.getId());}
		if(q.getCode()!=null) {json.setCode(criteria.getCode());}
		if(q.getVisible()!=null) {json.setVisible(criteria.isVisible());}
		if(q.getAllowEmpty()!=null) {json.setAllowEmpty(criteria.getAllowEmpty());}
		if(q.getLabel()!=null && criteria.getName().containsKey(localeCode)) {json.setLabel(criteria.getName().get(localeCode).getLang());}
		if(q.getDescription()!=null && criteria.getDescription().containsKey(localeCode)) {json.setDescription(criteria.getDescription().get(localeCode).getLang());}
		if(q.getType()!=null) {json.setType(jfType.build(criteria.getType()));}
		
		if(q.getOptions()!=null && !q.getOptions().isEmpty())
		{
			json.setOptions(new ArrayList<>());
			
			List<OPTION> options = new ArrayList<>();
			if(facade==null) {options.addAll(criteria.getOptions());}
			else {options.addAll(facade.allForParent(fbAttribute.getClassOption(),criteria));}
			for(OPTION o : options)
			{
				json.getOptions().add(jfOption.build(o));
			}
		}
		
		return json;
	}
}