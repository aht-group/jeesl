package org.jeesl.factory.ejb.system.revision;

import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionScope;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionView;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.io.revision.core.JeeslRevisionCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public class EjbRevisionViewFactory<L extends UtilsLang,D extends UtilsDescription,
									RC extends JeeslRevisionCategory<L,D,RC,?>,
									RV extends JeeslRevisionView<L,D,RVM>,
									RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
									RS extends JeeslRevisionScope<L,D,RC,RA>,
									RST extends UtilsStatus<RST,L,D>,
									RE extends JeeslRevisionEntity<L,D,RC,REM,RA>,
									REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
									RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>, RER extends UtilsStatus<RER,L,D>,
									RAT extends UtilsStatus<RAT,L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbRevisionViewFactory.class);
	
	final Class<RV> cView;
    
	public EjbRevisionViewFactory(final Class<RV> cView)
	{       
        this.cView = cView;
	}
	
	public static <L extends UtilsLang,D extends UtilsDescription,
					RC extends JeeslRevisionCategory<L,D,RC,?>,
					RV extends JeeslRevisionView<L,D,RVM>,
					RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
					RS extends JeeslRevisionScope<L,D,RC,RA>,
					RST extends UtilsStatus<RST,L,D>,
					RE extends JeeslRevisionEntity<L,D,RC,REM,RA>,
					REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
					RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>, RER extends UtilsStatus<RER,L,D>,
					RAT extends UtilsStatus<RAT,L,D>>
	EjbRevisionViewFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> factory(final Class<RV> cView)
	{
		return new EjbRevisionViewFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT>(cView);
	}
    
	public RV build()
	{
		RV ejb = null;
		try
		{
			ejb = cView.newInstance();
			ejb.setPosition(1);
			ejb.setVisible(true);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}