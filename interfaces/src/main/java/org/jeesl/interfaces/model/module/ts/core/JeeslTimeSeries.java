package org.jeesl.interfaces.model.module.ts.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslTimeSeries <SCOPE extends JeeslTsScope<?,?,?,?,?,?,INT>,
									TS extends JeeslTimeSeries<SCOPE,TS,?,INT,TYPE>,
									BRIDGE extends JeeslTsBridge<?>,
									INT extends JeeslTsInterval<?,?,INT,?>,
									TYPE extends JeeslTsStatistic<?,?,TYPE,?>
>
		extends EjbWithId,Serializable,EjbRemoveable,EjbPersistable
{
	public enum Attributes{id,scope,interval,statistic,bridge,tsSrc}
	
	SCOPE getScope();
	void setScope(SCOPE scope);
	
	INT getInterval();
	void setInterval(INT interval);
	
	TYPE getStatistic();
	void setStatistic(TYPE statistic);
	
	BRIDGE getBridge();
	void setBridge(BRIDGE bridge);
	
	TS getTsSrc();
	void setTsSrc(TS tsSrc);
}