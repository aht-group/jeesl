package org.jeesl.api.facade.io;

import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
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
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslIoAttributeFacade <L extends JeeslLang, D extends JeeslDescription,
										R extends JeeslTenantRealm<L,D,R,?>,
										CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
										CATEGORY extends JeeslStatus<L,D,CATEGORY>,
										CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,TYPE,OPTION>,
										TYPE extends JeeslStatus<L,D,TYPE>,
										OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
										SET extends JeeslAttributeSet<L,D,R,CAT,ITEM>,
										ITEM extends JeeslAttributeItem<CRITERIA,SET>,
										CONTAINER extends JeeslAttributeContainer<SET,DATA>,
										DATA extends JeeslAttributeData<CRITERIA,OPTION,CONTAINER>>
			extends JeeslFacade
{	
	SET load(SET set);
	
	List<CRITERIA> fAttributeCriteria(List<CATEGORY> categories, long refId);
	<RREF extends EjbWithId> List<CRITERIA> fAttributeCriteria(R realm, RREF rref, List<CAT> categories);
	
	List<CRITERIA> fAttributeCriteria(SET set);
	List<OPTION> fAttributeOption(SET set);
	
	List<SET> fAttributeSets(List<CATEGORY> categories, long refId);
	<RREF extends EjbWithId> List<SET> fAttributeSets(R realm, RREF rref, List<CAT> categories);
	
	List<DATA> fAttributeData(CONTAINER container);
	List<DATA> fAttributeData(CRITERIA criteria, List<CONTAINER> containers);
	
	DATA fAttributeData(CRITERIA criteria, CONTAINER container) throws JeeslNotFoundException;
	
	CONTAINER copy(CONTAINER container) throws JeeslConstraintViolationException, JeeslLockingException;
}