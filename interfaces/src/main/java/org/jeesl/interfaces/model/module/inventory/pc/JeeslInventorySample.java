package org.jeesl.interfaces.model.module.inventory.pc;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.model.interfaces.with.EjbWithRecord;

public interface JeeslInventorySample<L extends JeeslLang, D extends JeeslDescription,
										PC extends JeeslInventoryPc<L,D,PC,SAMPLE>,
										SAMPLE extends JeeslInventorySample<L,D,PC,SAMPLE>>
						extends EjbWithId, EjbSaveable, EjbWithRecord
{	
	
}