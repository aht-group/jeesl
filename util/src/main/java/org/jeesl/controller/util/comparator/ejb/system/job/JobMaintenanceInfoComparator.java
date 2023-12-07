package org.jeesl.controller.util.comparator.ejb.system.job;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobMaintenanceInfoComparator<STATUS extends JeeslJobStatus<?,?,STATUS,?>,
											MNI extends JeeslJobMaintenanceInfo<?,STATUS,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JobMaintenanceInfoComparator.class);

    public enum Type {statusPosition};

    public JobMaintenanceInfoComparator()
    {
    	
    }
    
    public Comparator<MNI> factory(Type type)
    {
        Comparator<MNI> c = null;
        JobMaintenanceInfoComparator<STATUS,MNI> factory = new JobMaintenanceInfoComparator<>();
        switch (type)
        {
            case statusPosition: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<MNI>
    {
        @Override public int compare(MNI a, MNI b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			 
			  ctb.append(a.getStatus().getPosition(), b.getStatus().getPosition());
			  return ctb.toComparison();
        }
    }
}