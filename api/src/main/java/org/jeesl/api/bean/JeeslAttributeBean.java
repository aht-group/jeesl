package org.jeesl.api.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;

public interface JeeslAttributeBean<L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,?>,
									CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
									CATEGORY extends JeeslStatus<L,D,CATEGORY>,
									CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,CATEGORY,TYPE,OPTION>,
									TYPE extends JeeslStatus<L,D,TYPE>,
									OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
									SET extends JeeslAttributeSet<L,D,R,CAT,ITEM>,
									ITEM extends JeeslAttributeItem<CRITERIA,SET>,
									CONTAINER extends JeeslAttributeContainer<SET,DATA>,
									DATA extends JeeslAttributeData<CRITERIA,OPTION,CONTAINER>>
	extends Serializable//,JeeslAttributeCriteriaCacheBean<CRITERIA,OPTION,SET>
{	
	List<CATEGORY> getCategories();
	void reloadCategories();
	
	List<TYPE> getTypes();
	void reloadTypes();
	
	void updateCriteria(CRITERIA c);
	void updateSet(SET set);
	
	Map<SET,List<CRITERIA>> getMapCriteria();
	Map<SET,List<CRITERIA>> getMapTableHeader();
	
	Map<CRITERIA,List<OPTION>> getMapOption();
}