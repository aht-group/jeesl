package org.jeesl.interfaces.model.module.tafu;

import java.io.Serializable;
import java.util.Date;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;

public interface JeeslTafuTask <R extends JeeslTenantRealm<?,?,R,?>,
								TS extends JeeslTafuStatus<?,?,TS,?>>
			extends Serializable,EjbSaveable,EjbRemoveable,
					JeeslWithTenantSupport<R>, JeeslWithStatus<TS>,
					EjbWithName
{
	public enum Attributes{realm,rref,status}
	
	Date getRecordCreated();
	void setRecordCreated(Date recordCreated);
		
	Date getRecordUpdated();
	void setRecordUpdated(Date recordUpdated);

	Date getRecordShow();
	void setRecordShow(Date recordShow);
	
	Date getRecordDue();
	void setRecordDue(Date recordDue);
	
	Date getRecordResolved();
	void setRecordResolved(Date recordResolved);
}