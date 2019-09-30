package org.jeesl.util.comparator.ejb.system.io.revision;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionScope;
import org.jeesl.interfaces.model.system.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.system.io.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.system.io.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntityMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public class RevisionScopeComparator<L extends UtilsLang,D extends UtilsDescription,
										RC extends JeeslRevisionCategory<L,D,RC,?>,	
										RV extends JeeslRevisionView<L,D,RVM>,
										RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
										RS extends JeeslRevisionScope<L,D,RC,RA>,
										RST extends UtilsStatus<RST,L,D>,
										RE extends JeeslRevisionEntity<L,D,RC,REM,RA,?>,
										REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
										RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
										RER extends UtilsStatus<RER,L,D>,
										RAT extends UtilsStatus<RAT,L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(RevisionScopeComparator.class);

    public enum Type {position};

    public RevisionScopeComparator()
    {
    	
    }
    
    public Comparator<RS> factory(Type type)
    {
        Comparator<RS> c = null;
        RevisionScopeComparator<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> factory = new RevisionScopeComparator<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<RS>
    {
        public int compare(RS a, RS b)
        {
			CompareToBuilder ctb = new CompareToBuilder();
			ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());
			ctb.append(a.getPosition(), b.getPosition());
			return ctb.toComparison();
        }
    }
}