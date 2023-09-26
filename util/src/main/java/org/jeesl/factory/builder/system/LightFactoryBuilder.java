package org.jeesl.factory.builder.system;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
								LIGHT extends JeeslTrafficLight<L,D,SCOPE>,
								SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>>
				extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(LightFactoryBuilder.class);
	
	private final Class<LIGHT> cLight; public Class<LIGHT> getClassLight() {return cLight;}
	private final Class<SCOPE> cScope; public Class<SCOPE> getClassScope() {return cScope;}
	
	public LightFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<LIGHT> cLight, final Class<SCOPE> cScope)
	{
		super(cL,cD);
		this.cLight=cLight;
		this.cScope=cScope;
	}
}