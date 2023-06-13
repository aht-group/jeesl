package org.jeesl.factory.json.module.attribute;

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
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.json.module.attribute.JsonAttributeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAttributeItemFactory<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									R extends JeeslTenantRealm<L,D,R,?>,
									CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
									CATEGORY extends JeeslStatus<L,D,CATEGORY>,
									CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,TYPE,OPTION,SET>,
									TYPE extends JeeslAttributeType<L,D,TYPE,?>,
									OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
									SET extends JeeslAttributeSet<L,D,R,CAT,ITEM>,
									ITEM extends JeeslAttributeItem<CRITERIA,SET>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonAttributeItemFactory.class);
	
	private final JsonAttributeItem q;
	private JsonAttributeCriteriaFactory<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM> jfCriteria;
	
	public JsonAttributeItemFactory(String localeCode, JsonAttributeItem q)
	{
		this.q=q;
		if(q.getCriteria()!=null) {jfCriteria = new JsonAttributeCriteriaFactory<>(localeCode,q.getCriteria());}
	}
	
	public void lazy(JeeslFacade facade, IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,OPTION,SET,ITEM,?,?> fbAttribute)
	{
		if(jfCriteria!=null) {jfCriteria.lazy(facade,fbAttribute);}
	}
	
	public static JsonAttributeItem build(){return new JsonAttributeItem();}
	
	public JsonAttributeItem build(ITEM item)
	{
		JsonAttributeItem json = build();
		if(q.getId()!=null) {json.setId(item.getId());}
		if(q.getPosition()!=null) {json.setPosition(item.getPosition());}
		if(q.getVisible()!=null) {json.setVisible(item.isVisible());}
		if(q.getCriteria()!=null) {json.setCriteria(jfCriteria.build(item.getCriteria()));}
		
		return json;
	}
}