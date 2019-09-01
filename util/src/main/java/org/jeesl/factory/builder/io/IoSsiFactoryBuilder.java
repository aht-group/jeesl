package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.system.io.ssi.EjbIoSsiDataFactory;
import org.jeesl.factory.ejb.system.io.ssi.EjbIoSsiSystemFactory;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.io.ssi.JeeslIoSsiData;
import org.jeesl.interfaces.model.system.io.ssi.JeeslIoSsiMapping;
import org.jeesl.interfaces.model.system.io.ssi.JeeslIoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public class IoSsiFactoryBuilder<L extends UtilsLang,D extends UtilsDescription,
								SYSTEM extends JeeslIoSsiSystem,
								MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
								DATA extends JeeslIoSsiData<MAPPING,LINK>,
								LINK extends UtilsStatus<LINK,L,D>,
								ENTITY extends JeeslRevisionEntity<?,?,?,?,?>>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoSsiFactoryBuilder.class);
	
	private final Class<SYSTEM> cSystem; public Class<SYSTEM> getClassSystem(){return cSystem;}
	private final Class<MAPPING> cMapping; public Class<MAPPING> getClassMapping(){return cMapping;}
	private final Class<DATA> cData; public Class<DATA> getClassData(){return cData;}
	private final Class<LINK> cLink; public Class<LINK> getClassLink(){return cLink;}
	private final Class<ENTITY> cEntity; public Class<ENTITY> getClassEntity(){return cEntity;}
	
	public IoSsiFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<SYSTEM> cSystem, final Class<MAPPING> cMapping, final Class<DATA> cData, final Class<LINK> cLink, final Class<ENTITY> cEntity)
	{
		super(cL,cD);
		this.cSystem=cSystem;
		this.cMapping=cMapping;
		this.cData=cData;
		this.cLink=cLink;
		this.cEntity=cEntity;
	}
	
	public EjbIoSsiSystemFactory<SYSTEM> ejbSystem() {return new EjbIoSsiSystemFactory<SYSTEM>(cSystem);}
	public EjbIoSsiDataFactory<MAPPING,DATA,LINK> ejbData() {return new EjbIoSsiDataFactory<MAPPING,DATA,LINK>(cData);}
}