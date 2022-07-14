package org.jeesl.factory.builder.io.iot;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotMatrixFactoryBuilder<L extends JeeslLang, D extends JeeslDescription
									>
				extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IotMatrixFactoryBuilder.class);

	    
	public IotMatrixFactoryBuilder(final Class<L> cL, final Class<D> cD)
	{
		super(cL,cD);
		
	}
	
	
}