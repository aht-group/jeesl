package org.jeesl.interfaces.model.module.attribute;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslAttributeSet <L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,?>,
									CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
									CATEGORY extends JeeslStatus<L,D,CATEGORY>,
									ITEM extends JeeslAttributeItem<?,?>>
		extends Serializable,EjbSaveable,EjbRemoveable,
				EjbWithPosition,EjbWithPositionParent,EjbWithCode,
				EjbWithLang<L>,EjbWithDescription<D>
{
	public enum Attributes{category,refId,position}
	
	CATEGORY getCategory();
	void setCategory(CATEGORY category);
	
	Long getRefId();
	void setRefId(Long refId);
	
	List<ITEM> getItems();
	void setItems(List<ITEM> items);
}