package org.jeesl.interfaces.model.module.attribute;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslAttributeSet <L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,?>,
									CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
									ITEM extends JeeslAttributeItem<?,?>>
		extends Serializable,EjbSaveable,EjbRemoveable,
				EjbWithPosition,EjbWithPositionParent,EjbWithCode,EjbWithNonUniqueCode,
				EjbWithLang<L>,EjbWithDescription<D>,
				JeeslWithTenantSupport<R>
{
	public enum Attributes{category,category2,refId,position}
	
//	CATEGORY getCategory();
//	void setCategory(CATEGORY category);
	
//	Long getRefId();
//	void setRefId(Long refId);

	CAT getCategory2();
	void setCategory2(CAT category2);
	
	List<ITEM> getItems();
	void setItems(List<ITEM> items);
}