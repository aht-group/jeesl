package org.jeesl.api.facade.core;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.system.io.db.JeeslSync;

import net.sf.ahtutils.interfaces.facade.UtilsFacade;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public interface JeeslSyncFacade <L extends UtilsLang,
									D extends UtilsDescription,
									STATUS extends UtilsStatus<STATUS,L,D>,
									CATEGORY extends UtilsStatus<CATEGORY,L,D>,
									SYNC extends JeeslSync<L,D,STATUS,CATEGORY>>
					extends UtilsFacade
{	
	SYNC fSync(Class<SYNC> cSync, CATEGORY category, String code) throws JeeslNotFoundException;
	SYNC fcSync(Class<SYNC> cSync, Class<STATUS> cStatus, CATEGORY category, String code);
	
	boolean checkValid(Class<SYNC> cSync, SYNC sync, long seconds);
}