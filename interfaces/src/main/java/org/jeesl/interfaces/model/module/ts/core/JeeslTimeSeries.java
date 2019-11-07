package org.jeesl.interfaces.model.module.ts.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;

import net.sf.ahtutils.interfaces.model.crud.EjbPersistable;
import net.sf.ahtutils.interfaces.model.crud.EjbRemoveable;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public interface JeeslTimeSeries <SCOPE extends JeeslTsScope<?,?,?,?,?,?,INT>,
									BRIDGE extends JeeslTsBridge<?>,
									INT extends UtilsStatus<INT,?,?>
//									,STAT extends JeeslTsStatistic<?,?,STAT,?>
>
		extends EjbWithId,Serializable,EjbRemoveable,EjbPersistable
{
	public enum Attributes{scope,interval,bridge}
	
	SCOPE getScope();
	void setScope(SCOPE scope);
	
	INT getInterval();
	void setInterval(INT interval);
	
	BRIDGE getBridge();
	void setBridge(BRIDGE bridge);
}