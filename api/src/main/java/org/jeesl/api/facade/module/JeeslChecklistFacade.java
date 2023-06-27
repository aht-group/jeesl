package org.jeesl.api.facade.module;

import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.cl.JeeslClCheckItem;
import org.jeesl.interfaces.model.module.cl.JeeslClChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslClTracklist;
import org.jeesl.interfaces.util.query.module.EjbChecklistQuery;

public interface JeeslChecklistFacade <CL extends JeeslClChecklist<?,?,?>,
									CI extends JeeslClCheckItem<?,CL,?>,
									TL extends JeeslClTracklist<?,?,CL>
    								>
			extends JeeslFacade
{	
	TL load(TL tracklist);
	
	List<CI> fCheckItems(EjbChecklistQuery<CL,CI,TL> query);
}