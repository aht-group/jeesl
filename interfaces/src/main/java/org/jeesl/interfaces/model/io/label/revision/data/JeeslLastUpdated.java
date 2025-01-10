package org.jeesl.interfaces.model.io.label.revision.data;

import java.time.LocalDateTime;

import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslLastUpdated <STATUS extends JeeslJobStatus<?,?,STATUS,?>> extends EjbWithId
{
	STATUS getLastUpdateStatus();
	void setLastUpdateStatus(STATUS lastUpdateStatus);
	
	LocalDateTime getLastUpdatedAt();
	void setLastUpdatedAt(LocalDateTime lastUpdatedAt);
}