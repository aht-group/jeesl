package org.jeesl.factory.json.module.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
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
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.json.module.attribute.JsonAttributeContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAttributeContainerFactory<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
	final static Logger logger = LoggerFactory.getLogger(JsonAttributeContainerFactory.class);
	
	private JeeslFacade facade;
	private IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute;

	private final JsonAttributeContainer q;
	
	private JsonAttributeDataFactory<L,D,R,CAT,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> jfData;
	
	public JsonAttributeContainerFactory(String localeCode, JsonAttributeContainer q)
	{
		this.q=q;
		
		if(q.getDatas()!=null && !q.getDatas().isEmpty()) {jfData = new JsonAttributeDataFactory<>(localeCode,q.getDatas().get(0));}
		
//		if(q.getType()!=null) {jfType = new JsonTypeFactory<>(localeCode,q.getType());}
//		if(q.getOptions()!=null && !q.getOptions().isEmpty()) {jfOption = new JsonAttributeOptionFactory<>(localeCode,q.getOptions().get(0));}
	}
	
	public void lazy(JeeslFacade facade, IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute)
	{
		this.facade=facade;
		this.fbAttribute=fbAttribute;
	}
	
	public static JsonAttributeContainer build() {return new JsonAttributeContainer();}
	public static JsonAttributeContainer build(boolean withList)
	{
		JsonAttributeContainer json = new JsonAttributeContainer();
		if(withList) {json.setDatas(new ArrayList<>());}
		return json;
	}
	public static JsonAttributeContainer build(Long id)
	{
		JsonAttributeContainer json = JsonAttributeContainerFactory.build();
		json.setId(0l);
		return json;
	}
	
	public JsonAttributeContainer build(CONTAINER container)
	{
		JsonAttributeContainer json = JsonAttributeContainerFactory.build();
		if(q.getId()!=null) {json.setId(container.getId());}
//		if(q.getCode()!=null) {json.setCode(criteria.getCode());}
//		if(q.getVisible()!=null) {json.setVisible(criteria.isVisible());}
//		if(q.getLabel()!=null && criteria.getName().containsKey(localeCode)) {json.setLabel(criteria.getName().get(localeCode).getLang());}
//		if(q.getDescription()!=null && criteria.getDescription().containsKey(localeCode)) {json.setDescription(criteria.getDescription().get(localeCode).getLang());}
//		if(q.getType()!=null) {json.setType(jfType.build(criteria.getType()));}
//		
		if(q.getDatas()!=null && !q.getDatas().isEmpty())
		{
			json.setDatas(new ArrayList<>());
			
			List<DATA> datas = new ArrayList<>();
			if(facade==null) {datas.addAll(container.getDatas());}
			else {datas.addAll(facade.allForParent(fbAttribute.getClassData(),container));}
			for(DATA d : datas)
			{
				json.getDatas().add(jfData.build(d));
			}
		}
		
		return json;
	}
}