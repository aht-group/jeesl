package org.jeesl.interfaces.model.module.inventory.pc;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslInventorySample<L extends JeeslLang, D extends JeeslDescription,
										PC extends JeeslInventoryPc<L,D,PC,SAMPLE>,
										SAMPLE extends JeeslInventorySample<L,D,PC,SAMPLE>>
						extends EjbWithId, EjbSaveable, EjbWithRecord
{	
	
}