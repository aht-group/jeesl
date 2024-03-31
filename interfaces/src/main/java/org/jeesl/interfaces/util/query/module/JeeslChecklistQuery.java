package org.jeesl.interfaces.util.query.module;

import java.util.List;

import org.jeesl.interfaces.model.module.cl.JeeslClCheckItem;
import org.jeesl.interfaces.model.module.cl.JeeslClChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslClTracklist;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslChecklistQuery<CL extends JeeslClChecklist<?,?,?>,
								CI extends JeeslClCheckItem<?,CL,?>,
								TL extends JeeslClTracklist<?,?,CL>
								>
			extends JeeslCoreQuery
{
	public List<CL> getCheckLists();
	
}