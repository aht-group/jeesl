package org.jeesl.interfaces.model.system.security.login;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDateTime;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;

public interface JeeslSecurityMfa <USER extends JeeslSecurityUser,
									TYPE extends JeeslSecurityMfaType<?,?,TYPE,?>>
			extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
					EjbWithParentAttributeResolver,EjbWithPositionVisible,JeeslWithRecordDateTime,
					JeeslWithType<TYPE>
{
	public enum Attributes{user,type}
	
	USER getUser();
	void setUser(USER user);
	
	String getJson();
	void setJson(String json);
}