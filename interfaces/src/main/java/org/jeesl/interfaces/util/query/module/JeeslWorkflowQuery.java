package org.jeesl.interfaces.util.query.module;

import java.util.List;

import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowProcess;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslOrderingQuery;

public interface JeeslWorkflowQuery <WP extends JeeslWorkflowProcess<?,?,?,?>>
		extends JeeslCoreQuery,JeeslOrderingQuery
{
	List<WP> getWorkflowProcesses();		
}