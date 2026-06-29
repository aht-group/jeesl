package org.jeesl.util.db.updater;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithRid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslRidMerger <RID extends EjbWithRid>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslRidMerger.class);
	
	private final Map<Long,RID> mapAvailable;

	private final Class<RID> cRid;
	
	public JeeslRidMerger(Class<RID> cRid)
	{
		this.cRid = cRid;
		
		mapAvailable = new HashMap<>();
	}

	
	public void loadExisting(JeeslFacade facade)
	{
		List<RID> list = facade.all(cRid);
		logger.info("Existing: "+list.size());
	}
	
	public void handled()
	{
		
	}
}