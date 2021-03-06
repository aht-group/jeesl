package org.jeesl.interfaces.model.io.ssi.docker;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslIoDocker <SYSTEM extends JeeslIoSsiSystem<?,?>,
									HOST extends JeeslIoSsiHost<?,?,SYSTEM>>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable
{
	public SYSTEM getSystem();
	public void setSystem(SYSTEM system);

	HOST getHost();
	void setHost(HOST host);
}