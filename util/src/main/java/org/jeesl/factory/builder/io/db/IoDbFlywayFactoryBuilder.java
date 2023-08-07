package org.jeesl.factory.builder.io.db;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.interfaces.model.io.db.flyway.JeeslIoDbFlyway;
import org.jeesl.interfaces.model.io.db.flyway.JeeslIoDbFlywayType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoDbFlywayFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								FLY extends JeeslIoDbFlyway,
								FT extends JeeslIoDbFlywayType<L,D,FT,?>
								
>
			extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoDbFlywayFactoryBuilder.class);
		
	private final Class<FLY> cFlyway; public Class<FLY> getClassFlyway() {return cFlyway;}
	private final Class<FT> cType; public Class<FT> getClassType() {return cType;}
	
	public IoDbFlywayFactoryBuilder(final Class<L> cL, final Class<D> cD,
							final Class<FLY> cFlyway,
							final Class<FT> cType)
	{
		super(cL,cD);
		this.cFlyway=cFlyway;
		this.cType=cType;
	}
}