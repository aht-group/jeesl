package org.jeesl.interfaces.model.system.io.ssi.docker;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.io.ssi.data.JeeslIoSsiSystem;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public interface JeeslIoSsiContainer <SYSTEM extends JeeslIoSsiSystem,
									HOST extends JeeslIoSsiHost<?,?>>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable
{	
	public enum Attributes {entity,json}
	
	public SYSTEM getSystem();
	public void setSystem(SYSTEM system);
	
	HOST getHost();
	void setHost(HOST host);
}