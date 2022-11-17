package org.jeesl.interfaces.model.util.tree;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentId;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;

public interface JeeslTreeElement <P extends EjbWithId> extends EjbSaveable,EjbWithPosition,EjbWithParentId<P>
{

}