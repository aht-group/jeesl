package org.jeesl.interfaces.util.query.system;

import java.util.List;

import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;

public interface JeeslJobQuery <TEMPLATE extends JeeslJobTemplate<?,?,?,?,?,?>,
								STATUS extends JeeslJobStatus<?,?,STATUS,?>>
{
	public List<TEMPLATE> getSystemJobTemplates();
	public List<STATUS> getSystemJobStatus();
	
	Boolean getTupleLoad();
}