package org.jeesl.interfaces.model.module.tafu;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
	public enum Attributes{realm,rref,status,recordShow}
	
	LocalDateTime getRecordCreated();
	void setRecordCreated(LocalDateTime recordCreated);
		
	LocalDateTime getRecordUpdated();
	void setRecordUpdated(LocalDateTime recordUpdated);

	LocalDate getRecordShow();
	void setRecordShow(LocalDate recordShow);
	
	LocalDate getRecordDue();
	void setRecordDue(LocalDate recordDue);
	
	LocalDateTime getRecordResolved();
	void setRecordResolved(LocalDateTime recordResolved);
}