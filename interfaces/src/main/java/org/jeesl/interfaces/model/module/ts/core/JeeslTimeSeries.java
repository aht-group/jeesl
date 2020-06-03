package org.jeesl.interfaces.model.module.ts.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslTimeSeries <SCOPE extends JeeslTsScope<?,?,?,?,?,?,INT>,
									TS extends JeeslTimeSeries<SCOPE,TS,?,INT,STAT>,
									BRIDGE extends JeeslTsBridge<?>,
									INT extends JeeslStatus<INT,?,?>,
									STAT extends JeeslTsStatistic<?,?,STAT,?>
>
		extends EjbWithId,Serializable,EjbRemoveable,EjbPersistable
{
	public enum Attributes{scope,interval,statistic,bridge,tsSrc}
	
	SCOPE getScope();
	void setScope(SCOPE scope);
	
	INT getInterval();
	void setInterval(INT interval);
	
	STAT getStatistic();
	void setStatistic(STAT statistic);
	
	BRIDGE getBridge();
	void setBridge(BRIDGE bridge);
	
	TS getTsSrc();
	void setTsSrc(TS tsSrc);
}