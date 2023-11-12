package org.jeesl.interfaces.model.module.ts.stat;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslTsCron <SCOPE extends JeeslTsScope<?,?,?,?,?,?,INT>,
									INT extends JeeslTsInterval<?,?,INT,?>,
									STAT extends JeeslTsStatistic<?,?,STAT,?>>
		extends EjbWithId,Serializable,EjbRemoveable,EjbPersistable,
				EjbWithVisible,EjbSaveable
{
	
	SCOPE getScope();
	void setScope(SCOPE scope);
	
	INT getIntervalExec();
	void setIntervalExec(INT intervalExec);

	INT getIntervalSrc();
	void setIntervalSrc(INT intervalSrc);

	INT getIntervalDst();
	void setIntervalDst(INT intervalDst);
	
	STAT getStatisticSrc();
	void setStatisticSrc(STAT statisticSrc);
	
	STAT getStatisticDst();
	void setStatisticDst(STAT statisticDst);
	
	int getFallbackSteps();
	void setFallbackSteps(int fallbackSteps);
}