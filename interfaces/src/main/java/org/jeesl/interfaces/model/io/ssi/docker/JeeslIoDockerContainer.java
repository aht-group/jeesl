package org.jeesl.interfaces.model.io.ssi.docker;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslIoDockerContainer <SYSTEM extends JeeslIoSsiSystem<?,?>,
									HOST extends JeeslIoSsiHost<?,?,SYSTEM>>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable
{	
	public enum Attributes {entity,json}
	
	public SYSTEM getSystem();
	public void setSystem(SYSTEM system);
	

}