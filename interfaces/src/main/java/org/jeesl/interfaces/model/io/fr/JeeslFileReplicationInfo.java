package org.jeesl.interfaces.model.io.fr;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslFileReplicationInfo<META extends JeeslFileMeta<?,?,?,?>,
										REPLICATION extends JeeslFileReplication<?,?,?,?,?>,
										RSTATUS extends JeeslFileStatus<?,?,RSTATUS,?>>
		extends Serializable,
					EjbSaveable,EjbRemoveable,
					EjbWithId,EjbWithNonUniqueCode,
					JeeslWithStatus<RSTATUS>
{
	public enum Attributes{storageSrc}
	
	
	REPLICATION getReplication();
	void setReplication(REPLICATION replication);
	
	META getMeta();
	void setMeta(META meta);
}