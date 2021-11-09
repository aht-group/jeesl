package org.jeesl.interfaces.model.module.attribute;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslAttributeCriteria<L extends JeeslLang, D extends JeeslDescription,
										R extends JeeslTenantRealm<L,D,R,?>,
										CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
										CATEGORY extends JeeslStatus<L,D,CATEGORY>,
										TYPE extends JeeslStatus<L,D,TYPE>,
										OPTION extends JeeslAttributeOption<L,D,?>>
			extends Serializable,EjbWithId,
					EjbSaveable,EjbRemoveable,
					EjbWithNonUniqueCode,EjbWithPositionVisible,EjbWithPositionParent,
					EjbWithLang<L>,EjbWithDescription<D>,
					JeeslWithTenantSupport<R>
{
	public enum Attributes{category,category2,refId,position,type}

	
	CATEGORY getCategory();
	void setCategory(CATEGORY category);
	
	CAT getCategory2();
	void setCategory2(CAT category2);
	
	
	
	Long getRefId();
	void setRefId(Long refId);
	
	TYPE getType();
	void setType(TYPE type);
	
	Boolean getAllowEmpty();
	void setAllowEmpty(Boolean allowEmpty);
	
	List<OPTION> getOptions();
	void setOptions(List<OPTION> options);
}