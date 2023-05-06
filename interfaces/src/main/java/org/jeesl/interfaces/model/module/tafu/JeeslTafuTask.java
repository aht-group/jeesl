package org.jeesl.interfaces.model.module.tafu;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.model.with.system.status.JeeslWithScope;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslTafuTask <R extends JeeslTenantRealm<?,?,R,?>,
								TS extends JeeslTafuStatus<?,?,TS,?>,
								SC extends JeeslTafuScope<?,?,R,SC,?>,
								M extends JeeslIoMarkup<?>>
			extends Serializable,EjbSaveable,EjbRemoveable,
					JeeslWithTenantSupport<R>, JeeslWithStatus<TS>, JeeslWithScope<SC>, 
					EjbWithName
{
	public enum Attributes{realm,rref,status,recordShow,recordDue}
	
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
	
	M getMarkup();
	void setMarkup(M markup);
}