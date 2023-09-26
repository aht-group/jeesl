package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.map.EjbMapImplementationFactory;
import org.jeesl.factory.ejb.module.map.EjbMapStatisticalFactory;
import org.jeesl.interfaces.model.module.map.JeeslLocationLevel;
import org.jeesl.interfaces.model.module.map.JeeslStatisticMapStatus;
import org.jeesl.interfaces.model.module.map.JeeslStatisticalMap;
import org.jeesl.interfaces.model.module.map.JeeslStatisticalMapImplementation;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								MAP extends JeeslStatisticalMap<L,D>,
								IMP extends JeeslStatisticalMapImplementation<MAP,STATUS,LEVEL>,
								STATUS extends JeeslStatisticMapStatus<L,D,STATUS,?>,
								LEVEL extends JeeslLocationLevel<L,D,LEVEL,?>>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(MapFactoryBuilder.class);
	
	private final Class<MAP> cMap; public Class<MAP> getClassMap() {return cMap;}
	private final Class<IMP> cImp; public Class<IMP> getClassImplementation() {return cImp;}
	private final Class<STATUS> cStatus; public Class<STATUS> getClassStatus() {return cStatus;}
	private final Class<LEVEL> cLevel; public Class<LEVEL> getClassLevel() {return cLevel;}

	public MapFactoryBuilder(final Class<L> cL,final Class<D> cD,
								final Class<MAP> cMap,
								final Class<IMP> cImp,
								final Class<STATUS> cStatus,
								final Class<LEVEL> cLevel)
	{       
		super(cL,cD);
		this.cMap=cMap;
		this.cImp=cImp;
		this.cStatus=cStatus;
		this.cLevel=cLevel;
	}

	public EjbMapStatisticalFactory<MAP> ejbMap() {return new EjbMapStatisticalFactory<>(cMap);}
	public EjbMapImplementationFactory<MAP,IMP> ejbImplementation() {return new EjbMapImplementationFactory<>(cImp);}
}