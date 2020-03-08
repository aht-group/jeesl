package org.jeesl.factory.ejb.module.ts;

import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
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
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTsScopeFactory<L extends JeeslLang, D extends JeeslDescription,
								CAT extends JeeslTsCategory<L,D,CAT,?>,
								SCOPE extends JeeslTsScope<L,D,CAT,?,UNIT,EC,INT>,
								UNIT extends JeeslStatus<UNIT,L,D>,
								TS extends JeeslTimeSeries<SCOPE,BRIDGE,INT,?>,
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
	final static Logger logger = LoggerFactory.getLogger(EjbTsScopeFactory.class);
	
	final Class<SCOPE> cScope;
    
	public EjbTsScopeFactory(final Class<SCOPE> cScope)
	{       
        this.cScope = cScope;
	}
	    
	public SCOPE build(UNIT unit)
	{
		SCOPE ejb = null;
		try
		{
			ejb = cScope.newInstance();
			ejb.setPosition(1);
			ejb.setVisible(true);
			ejb.setUnit(unit);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}