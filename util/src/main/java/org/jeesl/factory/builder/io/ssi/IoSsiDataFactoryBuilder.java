package org.jeesl.factory.builder.io.ssi;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.ssi.data.EjbIoSsiAttributeFactory;
import org.jeesl.factory.ejb.io.ssi.data.EjbIoSsiDataFactory;
import org.jeesl.factory.ejb.io.ssi.data.EjbIoSsiMappingFactory;
import org.jeesl.factory.txt.io.ssi.data.TxtIoSsiMappingFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiCleaning;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiMapping;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoSsiDataFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
								ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
								DATA extends JeeslIoSsiData<MAPPING,LINK,?>,
								LINK extends JeeslIoSsiLink<L,D,LINK,?>,
								ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
								CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
								JOB extends JeeslJobStatus<L,D,JOB,?>>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoSsiDataFactoryBuilder.class);
	
	private final Class<SYSTEM> cSystem; public Class<SYSTEM> getClassSystem(){return cSystem;}
	private final Class<MAPPING> cMapping; public Class<MAPPING> getClassMapping(){return cMapping;}
	private final Class<ATTRIBUTE> cAttribute; public Class<ATTRIBUTE> getClassAttribute(){return cAttribute;}
	private final Class<DATA> cData; public Class<DATA> getClassData(){return cData;}
	private final Class<LINK> cLink; public Class<LINK> getClassLink(){return cLink;}
	private final Class<ENTITY> cEntity; public Class<ENTITY> getClassEntity(){return cEntity;}
	private final Class<CLEANING> cCleaning; public Class<CLEANING> getClassCleaning(){return cCleaning;}
	private final Class<JOB> cJob; public Class<JOB> getClassJob(){return cJob;}
	
	public IoSsiDataFactoryBuilder(final Class<L> cL, final Class<D> cD,
								final Class<SYSTEM> cSystem,
								final Class<MAPPING> cMapping, final Class<ATTRIBUTE> cAttribute,
								final Class<DATA> cData, final Class<LINK> cLink, final Class<ENTITY> cEntity,
								final Class<CLEANING> cCleaning, final Class<JOB> cJob
								)
	{
		super(cL,cD);
		this.cSystem=cSystem;
		this.cMapping=cMapping;
		this.cAttribute=cAttribute;
		this.cData=cData;
		this.cLink=cLink;
		this.cEntity=cEntity;
		this.cCleaning=cCleaning;
		this.cJob=cJob;
	}

	public EjbIoSsiMappingFactory<SYSTEM,MAPPING,ENTITY> ejbMapping() {return new EjbIoSsiMappingFactory<>(this);}
	public EjbIoSsiAttributeFactory<MAPPING,ATTRIBUTE,ENTITY> ejbAttribute() {return new EjbIoSsiAttributeFactory<>(cAttribute);}
	public EjbIoSsiDataFactory<MAPPING,DATA,LINK> ejbData() {return new EjbIoSsiDataFactory<>(cData);}
	
	public TxtIoSsiMappingFactory<SYSTEM,MAPPING,ENTITY> txtMapping(String localeCode) {return new TxtIoSsiMappingFactory<>(localeCode);}
}