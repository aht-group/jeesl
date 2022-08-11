package org.jeesl.factory.ejb.io.label;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScopeType;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbLabelScopeFactory<L extends JeeslLang,D extends JeeslDescription,
									RC extends JeeslRevisionCategory<L,D,RC,?>,
									RV extends JeeslRevisionView<L,D,RVM>,
									RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
									RS extends JeeslRevisionScope<L,D,RC,RA>,
									RST extends JeeslRevisionScopeType<L,D,RST,?>,
									RE extends JeeslRevisionEntity<L,D,RC,REM,RA,?>,
									REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
									RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>, RER extends JeeslStatus<L,D,RER>,
									RAT extends JeeslStatus<L,D,RAT>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbLabelScopeFactory.class);
	
	final Class<RS> cScope;
    
	public EjbLabelScopeFactory(final Class<RS> cScope)
	{       
        this.cScope = cScope;
	}
    
	public RS build()
	{
		RS ejb = null;
		try
		{
			ejb = cScope.newInstance();
			ejb.setPosition(1);
			ejb.setVisible(true);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}