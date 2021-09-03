package org.jeesl.interfaces.model.module.am;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeesAmProject<L extends JeeslLang, D extends JeeslDescription, REALM extends JeeslTenantRealm<L,D,REALM,?>,
								ACTIVITY extends JeeslAmActivity<L,D,REALM,ACTIVITY,PROJ>, PROJ extends JeesAmProject<L,D,REALM,ACTIVITY,PROJ>>
						extends Serializable,EjbSaveable,EjbRemoveable,
								EjbWithId,EjbWithLang<L>,EjbWithDescription<D>,EjbWithNonUniqueCode,EjbWithPosition
{

	ACTIVITY getRoot();
	void setRoot(ACTIVITY root);
	//public enum Attributes{}

}