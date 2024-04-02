package org.jeesl.interfaces.model.module.am;

import java.io.Serializable;
import java.util.Date;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.date.ju.EjbWithDateRange;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.parent.EjbWithParentId;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslAmActivity <L extends JeeslLang, D extends JeeslDescription,
							REALM extends JeeslTenantRealm<L,D,REALM,?>,
							ACTIVITY extends JeeslAmActivity<L,D,REALM,ACTIVITY,PROJ>,
							PROJ extends JeesAmProject<L,D,REALM,ACTIVITY,PROJ>>
			extends Serializable,EjbSaveable,EjbRemoveable,EjbWithCode,
					EjbWithPosition,EjbWithParentAttributeResolver,EjbWithParentId<ACTIVITY>,
					EjbWithLang<L>,EjbWithDescription<D>,EjbWithDateRange,
					EjbWithPositionParent

{
	public enum Attributes{realm,parent}

	// Will be implemented later
//	REALM getRealm();
//	void setRealm(REALM realm);
//
//	long getRealmIdentifier();
//	void setRealmIdentifier(long realmIdentifier);

	ACTIVITY getParent();
	void setParent(ACTIVITY parent);
	PROJ getProject();
	void setProject(PROJ project);
	public Boolean getStructural();
	public void setStructural(Boolean structural);
	Date getPlannedStartDate();
	void setPlannedStartDate(Date plannedStartDate);
	Date getPlannedEndDate();
	void setPlannedEndDate(Date plannedEndDate);
}