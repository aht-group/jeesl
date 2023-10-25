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
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeType;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;

public interface JeeslAttributeBean<
									R extends JeeslTenantRealm<?,?,R,?>,
									CAT extends JeeslAttributeCategory<?,?,R,CAT,?>,
									CRITERIA extends JeeslAttributeCriteria<?,?,R,CAT,TYPE,OPTION,SET>,
									TYPE extends JeeslAttributeType<?,?,TYPE,?>,
									OPTION extends JeeslAttributeOption<?,?,CRITERIA>,
									SET extends JeeslAttributeSet<?,?,R,CAT,ITEM>,
									ITEM extends JeeslAttributeItem<CRITERIA,SET>,
									CONTAINER extends JeeslAttributeContainer<SET,DATA>,
									DATA extends JeeslAttributeData<CRITERIA,OPTION,CONTAINER>>
	extends Serializable//,JeeslAttributeCriteriaCacheBean<CRITERIA,OPTION,SET>
{	
	void reloadCategories();
	
	List<TYPE> getTypes();
	void reloadTypes();
	
	void updateCriteria(CRITERIA c);
	void updateSet(SET set);
	
	Map<SET,List<CRITERIA>> getMapCriteria();
	Map<SET,List<CRITERIA>> getMapTableHeader();
	
	Map<CRITERIA,List<OPTION>> getMapOption();
}