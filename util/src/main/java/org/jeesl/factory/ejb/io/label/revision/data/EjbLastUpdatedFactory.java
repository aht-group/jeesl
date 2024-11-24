package org.jeesl.factory.ejb.io.label.revision.data;

import java.time.LocalDateTime;

import org.jeesl.interfaces.model.io.label.revision.data.JeeslLastUpdated;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbLastUpdatedFactory
{
	final static Logger logger = LoggerFactory.getLogger(EjbLastUpdatedFactory.class);
	
	public static <T extends JeeslLastUpdated<S>, S extends JeeslJobStatus<?,?,S,?>> void updated(T ejb, S status)
	{       
       ejb.setLastUpdateStatus(status);
       ejb.setLastUpdatedAt(LocalDateTime.now());
	}
}