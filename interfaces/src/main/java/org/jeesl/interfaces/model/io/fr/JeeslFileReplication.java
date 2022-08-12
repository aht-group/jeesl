package org.jeesl.interfaces.model.io.fr;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslFileReplication<L extends JeeslLang,D extends JeeslDescription,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										STORAGE extends JeeslFileStorage<L,D,SYSTEM,?,?>,
										RTYPE extends JeeslFileReplicationType<L,D,RTYPE,?>>
		extends Serializable,EjbWithId,
					EjbSaveable,EjbRemoveable,
					JeeslWithType<RTYPE>
{
	public enum Attributes{storageSrc}
	
	STORAGE getStorageSrc();
	void setStorageSrc(STORAGE storageSrc);
	
	STORAGE getStorageDst();
	void setStorageDst(STORAGE storageDst);
}