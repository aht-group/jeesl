package org.jeesl.controller.handler.sb.decade;

import java.io.Serializable;

import org.jeesl.interfaces.controller.handler.tree.store.JeeslTree2Store;
import org.jeesl.interfaces.model.module.hydro.JeeslHydroDecade;
import org.jeesl.interfaces.model.module.hydro.JeeslHydroYear;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class SbDecadeTreeStore<HD extends JeeslHydroDecade, HY extends JeeslHydroYear>  implements Serializable,JeeslTree2Store<HD,HY>
{
	final static Logger logger = LoggerFactory.getLogger(SbDecadeTreeStore.class);
	private static final long serialVersionUID = 1L;

	public SbDecadeTreeStore()
	{

	}

	@Override public void storeTreeLevel1(HD l1) {}
	@Override public void storeTreeLevel2(HY l2) {}
	
//	@Override public void setLastLevel(int level) {}
}