package org.jeesl.interfaces.model.module.hydro;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.date.ju.EjbWithValidFromUntil;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslHydroYear <L extends JeeslLang, D extends JeeslDescription,
								HD extends JeeslStatus<L,D,HD>,
								HY extends JeeslHydroYear<L,D,HD,HY>>
					extends Serializable,EjbSaveable,EjbRemoveable,
							EjbWithParentAttributeResolver,
							EjbWithLang<L>,EjbWithDescription<D>,
							EjbWithCode,EjbWithPosition,
							EjbWithValidFromUntil
{

	public enum Attributes{decade}

	void setDecade(HD decade);
	HD getDecade();
}