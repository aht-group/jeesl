package org.jeesl.interfaces.util.query.system;

import java.util.List;

import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;

public interface JeeslJobQuery <TEMPLATE extends JeeslJobTemplate<?,?,?,?,?,?>>
{
	public List<TEMPLATE> getSystemJobTemplates();

	Boolean getTupleLoad();
	
}