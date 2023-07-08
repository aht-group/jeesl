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
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeType;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.EjbAttributeQuery;

public interface JeeslIoAttributeFacade <R extends JeeslTenantRealm<?,?,R,?>,
										CAT extends JeeslAttributeCategory<?,?,R,CAT,?>,
										CRITERIA extends JeeslAttributeCriteria<?,?,R,CAT,TYPE,OPTION,SET>,
										TYPE extends JeeslAttributeType<?,?,TYPE,?>,
										OPTION extends JeeslAttributeOption<?,?,CRITERIA>,
										SET extends JeeslAttributeSet<?,?,R,CAT,ITEM>,
										ITEM extends JeeslAttributeItem<CRITERIA,SET>,
										CONTAINER extends JeeslAttributeContainer<SET,DATA>,
										DATA extends JeeslAttributeData<CRITERIA,OPTION,CONTAINER>>
			extends JeeslFacade
{	
	SET load(SET set);
	
	<RREF extends EjbWithId> List<CRITERIA> fAttributeCriteria(R realm, RREF rref, List<CAT> categories);
	
	List<CRITERIA> fAttributeCriteria(SET set);
	List<OPTION> fAttributeOption(SET set);
	
	<RREF extends EjbWithId> List<SET> fAttributeSets(R realm, RREF rref, List<CAT> categories);
	
	List<DATA> fAttributeData(CONTAINER container);
	List<DATA> fAttributeData(CRITERIA criteria, List<CONTAINER> containers);
	List<DATA> fAttributeData(EjbAttributeQuery<CRITERIA,CONTAINER,DATA> query);
	
	DATA fAttributeData(CRITERIA criteria, CONTAINER container) throws JeeslNotFoundException;
	
	CONTAINER copy(CONTAINER container) throws JeeslConstraintViolationException, JeeslLockingException;
}