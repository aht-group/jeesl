package org.jeesl.controller.util.comparator.ejb.module.workflow;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowContext;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbWorkflowProcessComparator<WX extends JeeslWorkflowContext<?,?,WX,?>,
											WP extends JeeslWorkflowProcess<?,?,WX,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbWorkflowProcessComparator.class);

    public enum Type {position};
    
    public Comparator<WP> factory(Type type)
    {
        Comparator<WP> c = null;
        EjbWorkflowProcessComparator<WX,WP> factory = new EjbWorkflowProcessComparator<>();
        switch (type)
        {
            case position: c = factory.new PositionComparator();break;
        }

        return c;
    }

    private class PositionComparator implements Comparator<WP>
    {
        @Override public int compare(WP a, WP b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getContext().getPosition(), b.getContext().getPosition());
			  ctb.append(a.getPosition(),b.getPosition());
			  ctb.append(a.getId(),b.getId());
			  return ctb.toComparison();
        }
    }
}