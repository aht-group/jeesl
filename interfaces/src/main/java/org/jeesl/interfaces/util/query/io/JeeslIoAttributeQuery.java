package org.jeesl.interfaces.util.query.io;

import java.util.List;

import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslIoAttributeQuery<CRITERIA extends JeeslAttributeCriteria<?,?,?,?,?,?,?>,
								CONTAINER extends JeeslAttributeContainer<?,DATA>,
								DATA extends JeeslAttributeData<CRITERIA,?,CONTAINER>>
			extends JeeslCoreQuery
{
	List<CRITERIA> getCriterias();
	List<CONTAINER> getContainers();
	
//	void x();
}