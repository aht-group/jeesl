package org.jeesl.interfaces.controller.processor.system.job;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenance;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;

public interface JeeslJobMaitenanceTupler <STATUS extends JeeslJobStatus<?,?,STATUS,?>,
											MNT extends JeeslJobMaintenance<?,?,MNT,?>>
						extends Serializable
{
	JsonTuples1<STATUS> calculateTuples(MNT maintenance);
}