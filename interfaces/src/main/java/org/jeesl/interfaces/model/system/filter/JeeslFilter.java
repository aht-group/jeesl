package org.jeesl.interfaces.model.system.filter;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.label.revision.data.JeeslLastModified;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.parent.EjbWithParentId;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslFilter<L extends JeeslLang,
								FILTER extends JeeslFilter<L,FILTER,TYPE,SCOPE,USER>,
								TYPE extends JeeslFilterType<?,?,TYPE,?>,
								SCOPE extends JeeslFilterScope<?,?,SCOPE,?>,
								USER extends JeeslSimpleUser
							>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithPositionVisible,
				EjbWithParentId<FILTER>,EjbWithLang<L>,
				JeeslLastModified<USER>
{
	TYPE getType();
	void setType(TYPE type);
	
	SCOPE getScope();
	void setScope(SCOPE scope);
	
	USER getUser();
	void setUser(USER user);
	
	String getJson();
	void setJson(String json);
}