package org.jeesl.interfaces.model.system.io.dash;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslIoDashboard <L extends JeeslLang, D extends JeeslDescription,
DBR extends JeeslStatus<DBR,L,D>,DB extends JeeslIoDashboard<L,D,DBR,DB>>
extends Serializable,EjbSaveable,EjbRemoveable,
EjbWithParentAttributeResolver,
EjbWithLang<L>,EjbWithDescription<D>,
EjbWithCode,EjbWithPosition
{
public enum Attributes{resoloution}

public DBR getResolution();
public void setResolution(DBR resolution);
}