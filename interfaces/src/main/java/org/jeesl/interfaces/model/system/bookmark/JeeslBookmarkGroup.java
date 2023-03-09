package org.jeesl.interfaces.model.system.bookmark;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;

public interface JeeslBookmarkGroup<CTX extends JeeslSecurityContext<?,?>,
									USER extends JeeslSimpleUser>
		extends EjbWithId,EjbSaveable,EjbRemoveable,
					EjbWithParentAttributeResolver,EjbWithName,EjbWithPositionVisible
{	
	public static enum Attributes{user};
	
	CTX getContext();
	void setContext(CTX context);
	
	USER getUser();
	void setUser(USER user);
}