package org.jeesl.interfaces.model.system.job;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslJobRobot<L extends JeeslLang,D extends JeeslDescription>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
					EjbWithLang<L>,EjbWithDescription<D>
{	

}