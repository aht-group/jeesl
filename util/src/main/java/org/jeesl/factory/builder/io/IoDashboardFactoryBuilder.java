package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.dashboard.EjbDashComponentFactory;
import org.jeesl.factory.ejb.io.dashboard.EjbDashComponentPositionFactory;
import org.jeesl.factory.ejb.io.dashboard.EjbDashboardFactory;
import org.jeesl.interfaces.model.io.dash.JeeslIoDashComponent;
import org.jeesl.interfaces.model.io.dash.JeeslIoDashComponentPosition;
import org.jeesl.interfaces.model.io.dash.JeeslIoDashboard;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDashboardFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
									DBR extends JeeslStatus<L,D,DBR>,
									DBC extends JeeslIoDashComponent<L,D,DBC>,
									DBCP extends JeeslIoDashComponentPosition<L,D,DBR,DBC,DB,DBCP>,
									DB extends JeeslIoDashboard<L,D,DBR,DBCP,DB>>
				extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoDashboardFactoryBuilder.class);

	private final Class<DBR> cResolution; public Class<DBR> getClassResolution() {return cResolution;}
	private final Class<DB> cDashboard; public Class<DB> getClassDashboard() {return cDashboard;}
	private final Class<DBC> cDashComponent; public Class<DBC> getClassDashComponent() {return cDashComponent;}
	private final Class<DBCP> cDashComponentPosition; public Class<DBCP> getClassDashComponentPosition() {return cDashComponentPosition;}

	public IoDashboardFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<DBR> cResolution, final Class<DBC> cDashComponent, final Class<DBCP> cDashComponentPosition, final Class<DB> cDashboard)
	{
		super(cL,cD);
		this.cResolution=cResolution;
		this.cDashboard=cDashboard;
		this.cDashComponent = cDashComponent;
		this.cDashComponentPosition = cDashComponentPosition;
	}

	public EjbDashboardFactory<L,D,DB> dashboard(){return new EjbDashboardFactory<L,D,DB>(cDashboard);}
	public EjbDashComponentFactory<L,D,DBC> dashComponent(){return new EjbDashComponentFactory<L,D,DBC>(cDashComponent);}

	public EjbDashComponentPositionFactory<DBC,DBCP> dashComponentPosition(){return new EjbDashComponentPositionFactory<DBC,DBCP>(cDashComponentPosition);}
}