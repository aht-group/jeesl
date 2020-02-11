package org.jeesl.util.comparator.ejb.module.ts;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsSample;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.EjbWithLangDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public class TsScopeComparator<L extends JeeslLang, D extends JeeslDescription,
								CAT extends JeeslStatus<CAT,L,D>,
								SCOPE extends JeeslTsScope<L,D,CAT,?,UNIT,EC,INT>,
								UNIT extends JeeslStatus<UNIT,L,D>,
								TS extends JeeslTimeSeries<SCOPE,BRIDGE,INT>,
								TRANSACTION extends JeeslTsTransaction<SOURCE,DATA,USER,?>,
								SOURCE extends EjbWithLangDescription<L,D>, 
								BRIDGE extends JeeslTsBridge<EC>,
								EC extends JeeslTsEntityClass<L,D,CAT>,
								INT extends JeeslStatus<INT,L,D>,
								DATA extends JeeslTsData<TS,TRANSACTION,SAMPLE,WS>,
								SAMPLE extends JeeslTsSample, 
								USER extends EjbWithId, 
								WS extends JeeslStatus<WS,L,D>,
								QAF extends JeeslStatus<QAF,L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(TsScopeComparator.class);

    public enum Type {position};

    public TsScopeComparator()
    {
    	
    }
    
    public Comparator<SCOPE> factory(Type type)
    {
        Comparator<SCOPE> c = null;
        TsScopeComparator<L,D,CAT,SCOPE,UNIT,TS,TRANSACTION,SOURCE,BRIDGE,EC,INT,DATA,SAMPLE,USER,WS,QAF> factory = new TsScopeComparator<L,D,CAT,SCOPE,UNIT,TS,TRANSACTION,SOURCE,BRIDGE,EC,INT,DATA,SAMPLE,USER,WS,QAF>();
        switch (type)
        {
            case position: c = factory.new PositionCodeComparator();break;
        }

        return c;
    }

    private class PositionCodeComparator implements Comparator<SCOPE>
    {
        public int compare(SCOPE a, SCOPE b)
        {
			CompareToBuilder ctb = new CompareToBuilder();
			ctb.append(a.getCategory().getPosition(), b.getCategory().getPosition());
			ctb.append(a.getPosition(), b.getPosition());
			return ctb.toComparison();
        }
    }
}