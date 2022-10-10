package org.jeesl.interfaces.model.system.job.mnt;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;

public interface JeeslJobMaintenanceInfo<D extends JeeslDescription,
							STATUS extends JeeslJobStatus<?,D,STATUS,?>,
							MNT extends JeeslJobMaintenance <?,D,MNT,?>
>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithDescription<D>
{	
	public static enum Attributes{maintenance,status};
	
	MNT getMaintenance();
	void setMaintenance(MNT maintenance);
	
	STATUS getStatus();
	void setStatus(STATUS status);
}