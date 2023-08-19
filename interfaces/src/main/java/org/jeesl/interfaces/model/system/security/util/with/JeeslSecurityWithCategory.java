package org.jeesl.interfaces.model.system.security.util.with;

import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisibleParent;

public interface JeeslSecurityWithCategory<C extends JeeslSecurityCategory<?,?>>
				extends EjbWithId,EjbWithPositionVisibleParent,EjbWithPositionParent
{
	public C getCategory();
	public void setCategory(C category);
}